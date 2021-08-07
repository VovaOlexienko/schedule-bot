package com.schedule.bot.controllers.user.registration.states;

import com.pengrad.telegrambot.request.BaseRequest;
import com.schedule.bot.controllers.BotContext;

public abstract class RegistrationState {
    public abstract BaseRequest enter(BotContext context);
    public abstract RegistrationState nextState();
    public abstract RegistrationState previousState();
    public abstract String stateName();
}