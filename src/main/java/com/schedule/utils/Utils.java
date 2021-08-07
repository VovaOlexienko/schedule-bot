package com.schedule.utils;

import com.pengrad.telegrambot.model.Chat;
import com.schedule.modal.StudentGroup;
import com.schedule.modal.Teacher;
import org.aspectj.lang.JoinPoint;

import java.util.List;

public class Utils {

    public static String toStringTeacherList(List<Teacher> teachers) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < teachers.size(); i++) {
            stringBuilder.append(i + 1).append(". ").append(teachers.get(i).getFullname());
            if (!teachers.get(i).getEmail().isEmpty())
                stringBuilder.append("\nEmail: ").append(teachers.get(i).getEmail());
            if (!teachers.get(i).getTgNickname().isEmpty())
                stringBuilder.append("\nTelegram: ").append(teachers.get(i).getTgNickname());
            stringBuilder.append("\n\n");
        }
        return stringBuilder.toString();
    }

    public static String toStringGroupsList(List<StudentGroup> studentGroups) {
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

    public static Chat getChat(JoinPoint joinPoint) throws Exception {
        Object[] signatureArgs = joinPoint.getArgs();
        Chat chat = null;
        for (Object signatureArg : signatureArgs) {
            if (signatureArg instanceof Chat) chat = (Chat) signatureArg;
        }
        if (chat == null) throw new Exception("No chat");
        return chat;
    }
}
