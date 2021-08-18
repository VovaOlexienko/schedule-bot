package com.schedule.bot.user.states;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.user.BotContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApprovedState extends UserState {

    @Autowired
    EnterGroupState enterGroupState;

    @Override
    public BaseRequest enter(BotContext context) {
        return new SendMessage(context.getChat().id(), "");
    }

    @Override
    public UserState nextState() {
        return this;
    }

    @Override
    public UserState previousState() {
        return enterGroupState;
    }

    @Override
    public String stateName() {
        return "ApprovedState";
    }
}
