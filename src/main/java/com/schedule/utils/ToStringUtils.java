package com.schedule.utils;

import com.pengrad.telegrambot.model.request.InlineQueryResult;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.model.request.InputTextMessageContent;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.schedule.modal.StudentGroup;
import com.schedule.modal.Teacher;

import java.util.ArrayList;
import java.util.List;

public class ToStringUtils {

    public static String studentGroupsListToString(List<StudentGroup> studentGroups) {
        StringBuilder stringBuilder = new StringBuilder();
        for (StudentGroup studentGroup : studentGroups) {
            if (studentGroup.getNumber().contains(",") || studentGroup.getNumber().contains(".") || studentGroup.getNumber().contains("/"))
                stringBuilder.append("\"").append(studentGroup.getNumber()).append("\"");
            else
                stringBuilder.append(studentGroup.getNumber());
            stringBuilder.append(", ");
        }
        return stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length()).toString();
    }

    public static List<InlineQueryResult> teacherListToInlineResultList(List<Teacher> teachers) {
        List<InlineQueryResult> teachersResult = new ArrayList<>();
        for (Teacher teacher : teachers) {
            teachersResult.add(
                    new InlineQueryResultArticle(teacher.getId().toString(), teacher.getFullname(),
                            buildTeacherMessageContent(teacher))
                            .description(buildTeacherDescription(teacher))
                            .thumbUrl(teacher.getPhotoUrl()).thumbHeight(48).thumbWidth(48));
        }
        return teachersResult;
    }

    private static InputTextMessageContent buildTeacherMessageContent(Teacher teacher) {
        return new InputTextMessageContent(
                "• " + teacher.getFullname() + "\n" + buildTeacherDescription(teacher))
                .parseMode(ParseMode.HTML);
    }

    private static String buildTeacherDescription(Teacher teacher) {
        String text = "";
        if (teacher.getTgNickname() != null && !teacher.getTgNickname().isEmpty())
            text += "Telegram: " + teacher.getTgNickname() + "\n";
        if (teacher.getUniversityEmail() != null && !teacher.getUniversityEmail().isEmpty()) text += teacher.getUniversityEmail() + "\n";
        if (teacher.getEmail() != null && !teacher.getEmail().isEmpty()) text += teacher.getEmail();
        return text;
    }
}
