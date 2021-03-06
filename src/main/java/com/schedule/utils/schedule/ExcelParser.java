package com.schedule.utils.schedule;

import com.aspose.cells.ImageType;
import com.schedule.modal.DaySchedule;
import com.schedule.modal.StudentGroup;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExcelParser {

    private String scheduleFilename;

    private List<StudentGroup> groups;
    private List<DaySchedule> schedules;

    private List<String> rangesForImage;
    private List<String> numbersOfLesson;
    private List<Integer> worksheets;

    private boolean nextDaySoon;
    private boolean endOfDay;
    private boolean firstLessonForThisGroup;
    private int startRow;
    private int startColumn;
    private int sheetNumber;

    public List<StudentGroup> getGroups() {
        return groups;
    }

    public List<DaySchedule> getSchedules() {
        return schedules;
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
            return cell.getStringCellValue();
        }
        return "";
    }

    private CellRangeAddress findRegionWithCell(Sheet sheet, Cell cell) {
        for (CellRangeAddress region : sheet.getMergedRegions()) {
            if (region.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
                return region;
            }
        }
        return null;
    }

    public void parseGroupsAndSchedulesToList(String scheduleFilename) throws Exception {
        this.scheduleFilename = scheduleFilename;
        this.groups = new ArrayList<>();
        this.schedules = new ArrayList<>();
        this.rangesForImage = new ArrayList<>();
        this.numbersOfLesson = new ArrayList<>();
        this.worksheets = new ArrayList<>();

        int row = 0;
        int column = 2;

        Workbook workbook = new XSSFWorkbook(OPCPackage.open(new FileInputStream(this.scheduleFilename)));
        for (int i = 0; i < 8; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            sheet.getRow(0).setHeight((short) (80 * 20));//60
            int r = 1;
            while (sheet.getRow(r) != null) {
                sheet.getRow(r).setHeight((short) (20 * 20));//15
                r++;
            }
        }
        for (int i = 0; i < 8; i++) {
            sheetNumber = i;
            Sheet sheet = workbook.getSheetAt(i);
            while (true) {
                Cell cell = sheet.getRow(row).getCell(column);
                if (getStringCellValue(cell).matches(".*\\d.*")) {
                    groups.add(new StudentGroup(null, trim(getStringCellValue(cell))));
                } else {
                    break;
                }
                row++;
                firstLessonForThisGroup = true;
                nextDaySoon = true;
                while (sheet.getRow(row) != null && sheet.getRow(row + 2) != null &&
                        sheet.getRow(row).getCell(column) != null && sheet.getRow(row + 2).getCell(column) != null) {
                    CellRangeAddress regionUpLeft = findRegionWithCell(sheet, sheet.getRow(row).getCell(column));
                    CellRangeAddress regionUpRight = findRegionWithCell(sheet, sheet.getRow(row).getCell(column + 1));
                    CellRangeAddress regionDownLeft = findRegionWithCell(sheet, sheet.getRow(row + 2).getCell(column));
                    CellRangeAddress regionDownRight = findRegionWithCell(sheet, sheet.getRow(row + 2).getCell(column + 1));
                    if (Objects.equals(regionUpLeft, regionDownLeft) && Objects.equals(regionUpLeft, regionUpRight) && Objects.equals(regionUpLeft, regionDownRight)) {
                        unmergeCell(regionUpLeft, sheet, row, column);
                    } else if (Objects.equals(regionUpLeft, regionUpRight) && Objects.equals(regionDownLeft, regionDownRight)) {
                        unmergeHorizontalSplittedCells(regionUpLeft, regionDownLeft, sheet, row, column);
                    } else if (Objects.equals(regionDownLeft, regionDownRight)) {
                        unmergeUpSplittedCells(regionDownLeft, sheet, row, column);
                    } else if (Objects.equals(regionUpLeft, regionUpRight)) {
                        unmergeDownSplittedCells(regionUpLeft, sheet, row, column);
                    }
                    createNewDaySchedule(sheet, row, column);
                    row += 4;
                    firstLessonForThisGroup = false;
                }
                row = 0;
                column += 3;
            }
            row = 0;
            column = 2;
        }
        FileOutputStream fos = new FileOutputStream(this.scheduleFilename);
        workbook.write(fos);
        fos.close();
        workbook.close();
        addImageInDayScheduleList();
    }

    private DayOfWeek parseDayOfWeek(Sheet sheet, int row) throws Exception {
        if (sheet.getRow(row).getCell(0) == null) throw new Exception();
        CellRangeAddress region = findRegionWithCell(sheet, sheet.getRow(row).getCell(0));
        if (region == null) throw new Exception();

        String day = getStringCellValue(sheet.getRow(region.getFirstRow()).getCell(0)).trim();
        if (day.indexOf('*') != -1) {
            day = day.split("\\*")[0];
        }
        switch (day) {
            case "??????????????????":
                return DayOfWeek.MONDAY;
            case "????????????????":
                return DayOfWeek.TUESDAY;
            case "????????????":
                return DayOfWeek.WEDNESDAY;
            case "????????????":
                return DayOfWeek.THURSDAY;
            case "??'????????????":
                return DayOfWeek.FRIDAY;
            case "????????????":
                return DayOfWeek.SATURDAY;
            default:
                throw new Exception();//
        }
    }

    private void unmergeHorizontalSplittedCells(CellRangeAddress regionUpLeft, CellRangeAddress regionDownLeft, Sheet sheet, int row, int column) {
        unmergeDownRange(regionDownLeft, sheet, row, column);
        unmergeUpRange(regionUpLeft, sheet, row, column);
    }

    private void unmergeDownSplittedCells(CellRangeAddress regionUpLeft, Sheet sheet, int row, int column) {
        unmergeUpRange(regionUpLeft, sheet, row, column);
    }

    private void unmergeUpSplittedCells(CellRangeAddress regionDownLeft, Sheet sheet, int row, int column) {
        unmergeDownRange(regionDownLeft, sheet, row, column);
    }

    private void unmergeUpRange(CellRangeAddress regionUpLeft, Sheet sheet, int row, int column) {
        if (regionUpLeft != null && !getStringCellValue(sheet.getRow(regionUpLeft.getFirstRow()).getCell(regionUpLeft.getFirstColumn())).isEmpty()) {
            unmerge(sheet, regionUpLeft, 2, 4, row, column);
        }
    }

    private void unmergeDownRange(CellRangeAddress regionDownLeft, Sheet sheet, int row, int column) {
        if (regionDownLeft != null && !getStringCellValue(sheet.getRow(regionDownLeft.getFirstRow()).getCell(regionDownLeft.getFirstColumn())).isEmpty()) {
            unmerge(sheet, regionDownLeft, 2, 4, row + 2, column);
        }
    }

    private void unmergeCell(CellRangeAddress regionUpLeft, Sheet sheet, int row, int column) {
        if (regionUpLeft != null && !getStringCellValue(sheet.getRow(regionUpLeft.getFirstRow()).getCell(regionUpLeft.getFirstColumn())).isEmpty()) {
            unmerge(sheet, regionUpLeft, 4, 8, row, column);
        }
    }

    private void unmerge(Sheet sheet, CellRangeAddress region, int height, int standartCellSize, int row, int column) {
        if (region.getNumberOfCells() > standartCellSize) {
            int numberOfGroupOnLesson;
            int width = region.getNumberOfCells() / height;
            numberOfGroupOnLesson = 1;
            width -= 2;
            while (width != 0) {
                width -= 3;
                numberOfGroupOnLesson++;
            }
            for (int j = 0; j < sheet.getNumMergedRegions(); j++) {
                if (sheet.getMergedRegions().get(j).containsRow(row) && sheet.getMergedRegions().get(j).containsColumn(column)) {
                    sheet.removeMergedRegion(j);
                    break;
                }
            }
            String value = trim(getStringCellValue(sheet.getRow(row).getCell(column)));
            for (int j = 0; j < numberOfGroupOnLesson; j++) {
                System.out.print(sheet.getSheetName());
                sheet.addMergedRegion(new CellRangeAddress(row, row + height - 1, column, column + 1));
                sheet.getRow(row).getCell(column).setCellValue(value);
                column += 3;
            }
            Cell[] cells = new Cell[height];
            for (int j = 0; j < height; j++) {
                cells[j] = sheet.getRow(row + j).getCell(column - 1);
            }
            for (int k = 1; k < numberOfGroupOnLesson; k++) {
                for (int j = 0; j < height; j++) {
                    sheet.getRow(row + j).getCell(column - k * 3 - 1).setCellValue(getStringCellValue(cells[j]));
                }
            }
        }
    }

    private void createNewDaySchedule(Sheet sheet, int row, int column) {
        DayOfWeek dayOfWeek = null;
        try {
            dayOfWeek = parseDayOfWeek(sheet, row);
            if (dayOfWeek != parseDayOfWeek(sheet, row + 4)) {
                nextDaySoon = true;
                endOfDay = true;
            }
        } catch (Exception e) {
            nextDaySoon = true;
            endOfDay = true;
        }
        if (endOfDay) {
            boolean b = false;
            for (int i = startRow + 4; i < row + 3; i++) {
                for (int j = startColumn; j < column + 1; j++) {
                    if (sheet.getRow(i).getCell(j) != null && !getStringCellValue(sheet.getRow(i).getCell(j)).isEmpty()) {
                        b = true;
                        break;
                    }
                }
            }
            if (b) {
                rangesForImage.add(sheet.getRow(startRow).getCell(startColumn).getAddress().toString() + ":"
                        + sheet.getRow(row + 3).getCell(column + 2).getAddress().toString());
                schedules.add(new DaySchedule(null, null, dayOfWeek, groups.get(groups.size() - 1)));
                worksheets.add(sheetNumber);
                numbersOfLesson.add(sheet.getRow(startRow).getCell(1).getAddress().toString() + ":"
                        + sheet.getRow(row + 3).getCell(1).getAddress().toString());
            }
            startRow = 0;
            startColumn = 0;
            endOfDay = false;
        } else if (nextDaySoon) {
            int upRow = row;
            if (firstLessonForThisGroup) {
                upRow--;
            } else {
                upRow -= 4;
            }
            startRow = upRow;
            startColumn = column;
            nextDaySoon = false;
        }
    }

    private void addImageInDayScheduleList() throws Exception {
        com.aspose.cells.Workbook workbook = new com.aspose.cells.Workbook(scheduleFilename);

        for (int i = 0; i < worksheets.size(); i++) {
            com.aspose.cells.Worksheet worksheet = workbook.getWorksheets().get(worksheets.get(i));

            ByteArrayOutputStream scheduleStream = toImageCellsRange(worksheet, rangesForImage.get(i));
            ByteArrayOutputStream numberOfLessonStream = toImageCellsRange(worksheet, numbersOfLesson.get(i));
            ByteArrayInputStream temp = new ByteArrayInputStream(scheduleStream.toByteArray());
            BufferedImage schedule = ImageIO.read(temp);
            temp = new ByteArrayInputStream(numberOfLessonStream.toByteArray());
            BufferedImage numberOfLesson = ImageIO.read(temp);

            BufferedImage image = new BufferedImage(schedule.getWidth() + numberOfLesson.getWidth() + 1, schedule.getHeight(), BufferedImage.TYPE_INT_ARGB);
            image.getGraphics().drawImage(numberOfLesson, 0, 0, null);
            image.getGraphics().drawImage(schedule, numberOfLesson.getWidth() + 1, 0, null);
            BufferedImage dest = image.getSubimage(0, 105, image.getWidth(), image.getHeight() - 105);//-75
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(dest, "png", output);
            schedules.get(i).setSchedule(output.toByteArray());
        }
        workbook.save(scheduleFilename);
    }

    private ByteArrayOutputStream toImageCellsRange(com.aspose.cells.Worksheet worksheet, String range) throws Exception {
        worksheet.getPageSetup().setPrintArea(range);
        worksheet.getPageSetup().setLeftMargin(0);
        worksheet.getPageSetup().setRightMargin(0);
        worksheet.getPageSetup().setTopMargin(0);
        worksheet.getPageSetup().setBottomMargin(0);

        com.aspose.cells.ImageOrPrintOptions options = new com.aspose.cells.ImageOrPrintOptions();
        options.setOnePagePerSheet(true);
        options.setImageType(ImageType.PNG);

        com.aspose.cells.SheetRender sr = new com.aspose.cells.SheetRender(worksheet, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        sr.toImage(0, stream);
        return stream;
    }

    private static String trim(String s) {
        return s.trim().replaceAll(" +", " ");
    }
}
