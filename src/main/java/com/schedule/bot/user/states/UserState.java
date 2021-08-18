package com.schedule.bot.user.states;

import com.pengrad.telegrambot.request.BaseRequest;
import com.schedule.bot.user.BotContext;

public abstract class UserState {
    public abstract BaseRequest enter(BotContext context);
    public abstract UserState nextState();
    public abstract UserState previousState();
    public abstract String stateName();
}