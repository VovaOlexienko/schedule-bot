package com.schedule.utils;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import org.apache.poi.ss.formula.functions.T;
import org.aspectj.lang.JoinPoint;

public class AnnotationUtils {
    
    public static CallbackQuery getCallbackQuery(JoinPoint joinPoint) throws Exception {
        Object[] signatureArgs = joinPoint.getArgs();
        CallbackQuery callbackQuery = null;
        for (Object signatureArg : signatureArgs) {
            if (signatureArg instanceof CallbackQuery) callbackQuery = (CallbackQuery) signatureArg;
        }
        if (callbackQuery == null) throw new Exception("No callbackQuery");
        return callbackQuery;
    }

    public static TelegramBot getTelegramBot(JoinPoint joinPoint) throws Exception {
        Object[] signatureArgs = joinPoint.getArgs();
        TelegramBot telegramBot = null;
        for (Object signatureArg : signatureArgs) {
            if (signatureArg instanceof TelegramBot) telegramBot = (TelegramBot) signatureArg;
        }
        if (telegramBot == null) throw new Exception("No telegramBot");
        return telegramBot;
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
