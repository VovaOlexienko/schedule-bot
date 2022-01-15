package com.schedule.bot.registration.states;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.schedule.bot.registration.RegistrationRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class EndRegistrationState implements UserRegistrationState {

    @Override
    public BaseRequest<SendMessage, SendResponse> enter(RegistrationRequest context) {
        return new SendMessage(context.getChat().id(), StringUtils.EMPTY);
    }

    @Override
    public UserRegistrationState nextState() {
        return this;
    }
}
