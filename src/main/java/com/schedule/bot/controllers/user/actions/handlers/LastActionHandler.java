package com.schedule.bot.controllers.user.actions.handlers;

import com.pengrad.telegrambot.request.BaseRequest;
import com.schedule.bot.controllers.BotContext;
import com.schedule.bot.controllers.user.actions.Action;

public class LastActionHandler extends ActionHandler {

    @Override
    public BaseRequest handle(Action action, BotContext context) throws Exception {
        throw new Exception("Handler not found");
    }
}
