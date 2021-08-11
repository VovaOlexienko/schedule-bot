package com.schedule.bot.user;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.DeleteMessage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static com.schedule.utils.AnnotationUtils.*;

@Aspect
@Component
public class CallbackQueryAspect {

    @Pointcut("@annotation(com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest)")
    public void callbackQueryRequest() {
    }


    /**
     * Delete old messages with inline keyboards
     * @param joinPoint
     * @throws Exception
     */
    @Before("callbackQueryRequest()")
    public void deleteOldInlineQuery(JoinPoint joinPoint) throws Exception {
        CallbackQuery callbackQuery = getCallbackQuery(joinPoint);
        TelegramBot telegramBot = getTelegramBot(joinPoint);
        Chat chat = getChat(joinPoint);
        telegramBot.execute(new DeleteMessage(chat.id(),
                callbackQuery.message().messageId()));
    }
}