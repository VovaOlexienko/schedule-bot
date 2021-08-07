package com.schedule.bot.controllers.user.actions.handlers;

import com.pengrad.telegrambot.request.BaseRequest;
import com.schedule.bot.controllers.BotContext;
import com.schedule.bot.controllers.user.actions.Action;

public abstract class ActionHandler {

    ActionHandler nextHandler;

    public void setNext(ActionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
    public abstract BaseRequest handle(Action action, BotContext context) throws Exception;
}
