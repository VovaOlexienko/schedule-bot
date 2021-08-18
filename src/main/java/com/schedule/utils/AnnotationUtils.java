package com.schedule.utils;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import org.aspectj.lang.JoinPoint;

public class AnnotationUtils {

    public static CallbackQuery getCallbackQuery(JoinPoint joinPoint) {
        Object[] signatureArgs = joinPoint.getArgs();
        CallbackQuery callbackQuery = null;
        for (Object signatureArg : signatureArgs) {
            if (signatureArg instanceof CallbackQuery) callbackQuery = (CallbackQuery) signatureArg;
        }
        if (callbackQuery == null) throw new IllegalArgumentException("None CallbackQuery");
        return callbackQuery;
    }

    public static TelegramBot getTelegramBot(JoinPoint joinPoint) {
        Object[] signatureArgs = joinPoint.getArgs();
        TelegramBot telegramBot = null;
        for (Object signatureArg : signatureArgs) {
            if (signatureArg instanceof TelegramBot) telegramBot = (TelegramBot) signatureArg;
        }
        if (telegramBot == null) throw new IllegalArgumentException("None TelegramBot");
        return telegramBot;
    }

    public static Chat getChat(JoinPoint joinPoint) {
        Object[] signatureArgs = joinPoint.getArgs();
        Chat chat = null;
        for (Object signatureArg : signatureArgs) {
            if (signatureArg instanceof Chat) chat = (Chat) signatureArg;
        }
        if (chat == null) throw new IllegalArgumentException("None Chat");
        return chat;
    }
}
