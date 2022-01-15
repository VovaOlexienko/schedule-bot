package com.schedule.bot.callback;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.schedule.bot.context.UserContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.schedule.utils.AnnotationUtils.getMethodParameter;

@Aspect
@Order(3)
@Component
public class CallbackQueryAspect {

    private UserContext userContext;

    @Pointcut("@annotation(com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest)")
    public void callbackQueryRequest() {
        //Delete old messages after new request
    }

    @Before("callbackQueryRequest()")
    public void deleteOldInlineQuery(JoinPoint joinPoint) {
        Optional<CallbackQuery> callbackQuery = getMethodParameter(joinPoint, CallbackQuery.class);
        Optional<TelegramBot> telegramBot = getMethodParameter(joinPoint, TelegramBot.class);
        if(callbackQuery.isPresent() && telegramBot.isPresent()) {
            telegramBot.get().execute(new DeleteMessage(userContext.getStudent().getChatId(), callbackQuery.get().message().messageId()));
        }
    }

    @Autowired
    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
    }
}