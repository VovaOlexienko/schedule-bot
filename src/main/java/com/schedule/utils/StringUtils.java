package com.schedule.utils;

import com.pengrad.telegrambot.model.request.*;
import com.schedule.modal.StudentGroup;
import com.schedule.modal.Teacher;

import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {

    public static boolean isBlank(String s) {
        return s == null || s.isEmpty();
    }

    public static String trim(String s) {
        return s.trim().replaceAll(" +", " ");
    }

    public static String studentGroupsListToString(List<StudentGroup> studentGroups) {
        return studentGroups.stream().map(StudentGroup::toString).collect(Collectors.joining(", "));
    }

    public static List<InlineQueryResult> teacherListToInlineResultList(List<Teacher> teachers) {
        return teachers.stream().map(teacher ->
                new InlineQueryResultArticle(
                        teacher.getId().toString(),
                        teacher.getFullname(),
                        new InputTextMessageContent("• " + teacher.toString()).parseMode(ParseMode.HTML))
                        .description(teacher.getDescription())
                        .thumbUrl(teacher.getPhotoUrl())
                        .thumbHeight(48)
                        .thumbWidth(48))
                .collect(Collectors.toList());
    }
}
