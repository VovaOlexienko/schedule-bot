package com.schedule.bot.controllers.user.actions.handlers;

import com.pengrad.telegrambot.request.BaseRequest;
import com.schedule.bot.controllers.BotContext;
import com.schedule.bot.controllers.user.actions.Action;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 0)
public class FirstActionHandler extends ActionHandler {

    @Override
    public BaseRequest handle(Action action, BotContext context) throws Exception {
        return nextHandler.handle(action, context);
    }
}
