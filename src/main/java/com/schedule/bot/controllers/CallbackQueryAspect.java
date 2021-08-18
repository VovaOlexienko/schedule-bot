package com.schedule.bot.controllers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.schedule.utils.AnnotationUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CallbackQueryAspect {

    @Pointcut("@annotation(com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest)")
    public void callbackQueryRequest() {
    }

    /**
     * Delete old messages with inline keyboards
     */
    @Before("callbackQueryRequest()")
    public void deleteOldInlineQuery(JoinPoint joinPoint) {
        CallbackQuery callbackQuery = AnnotationUtils.getCallbackQuery(joinPoint);
        TelegramBot telegramBot = AnnotationUtils.getTelegramBot(joinPoint);
        Chat chat = AnnotationUtils.getChat(joinPoint);
        telegramBot.execute(new DeleteMessage(chat.id(), callbackQuery.message().messageId()));
    }
}