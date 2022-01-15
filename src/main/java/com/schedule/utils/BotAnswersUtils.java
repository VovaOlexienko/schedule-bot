package com.schedule.utils;

import com.pengrad.telegrambot.model.request.InlineQueryResult;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.model.request.InputTextMessageContent;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.schedule.modal.StudentGroup;
import com.schedule.modal.Teacher;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@UtilityClass
public class BotAnswersUtils {

    public static List<InlineQueryResult> teacherListToInlineResultList(List<Teacher> teachers) {
        return teachers.stream().map(teacher ->
                        new InlineQueryResultArticle(teacher.getId().toString(), teacher.getFullname(), getInputMessageContent(teacher))
                                .description(getTeacherDescription(teacher))
                                .thumbUrl(teacher.getPhotoUrl())
                                .thumbHeight(48)
                                .thumbWidth(48))
                .collect(Collectors.toList());
    }

    private static InputTextMessageContent getInputMessageContent(Teacher teacher) {
        return new InputTextMessageContent(String.format("• %s", getTeacherText(teacher))).parseMode(ParseMode.HTML);
    }

    private String getTeacherDescription(Teacher teacher) {
        StringBuilder s = new StringBuilder();
        if (isNotBlank(teacher.getTgNickname())) s.append("Telegram: ").append(teacher.getTgNickname()).append("\n");
        if (isNotBlank(teacher.getUniversityEmail())) s.append(teacher.getUniversityEmail()).append("\n");
        if (isNotBlank(teacher.getEmail())) s.append(teacher.getEmail());
        return s.toString();
    }

    private String getTeacherText(Teacher teacher) {
        return teacher.getFullname() + "\n" + getTeacherDescription(teacher);
    }

    public static String studentGroupsListToString(List<StudentGroup> studentGroups) {
        return studentGroups.stream().map(BotAnswersUtils::getStudentGroupName).collect(Collectors.joining(", "));
    }

    private static String getStudentGroupName(StudentGroup group) {
        String groupNumber = group.getNumber();
        return groupNumber.contains(",") || groupNumber.contains(".") || groupNumber.contains(" ")
                ? String.format("\"%s\"", groupNumber) : groupNumber;
    }
}
