package com.schedule.bot.user.registration.states;

import com.pengrad.telegrambot.request.BaseRequest;
import com.schedule.bot.user.registration.BotContext;

public abstract class RegistrationState {
    public abstract BaseRequest enter(BotContext context);
    public abstract RegistrationState nextState();
    public abstract RegistrationState previousState();
    public abstract String stateName();
}