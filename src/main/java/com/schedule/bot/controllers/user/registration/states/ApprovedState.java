package com.schedule.bot.controllers.user.registration.states;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.controllers.BotContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApprovedState extends RegistrationState {

    @Autowired
    EnterGroupState enterGroupState;

    @Override
    public BaseRequest enter(BotContext context) {
        return new SendMessage(context.getChat().id(), "");
    }

    @Override
    public RegistrationState nextState() {
        return this;
    }

    @Override
    public RegistrationState previousState() {
        return enterGroupState;
    }

    @Override
    public String stateName() {
        return "ApprovedState";
    }
}
