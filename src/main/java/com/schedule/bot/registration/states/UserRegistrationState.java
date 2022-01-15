package com.schedule.bot.registration.states;

import com.pengrad.telegrambot.request.BaseRequest;
import com.schedule.bot.registration.RegistrationRequest;

public interface UserRegistrationState {

    BaseRequest enter(RegistrationRequest context);

    UserRegistrationState nextState();
}