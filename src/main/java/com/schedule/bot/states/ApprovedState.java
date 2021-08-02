package com.schedule.bot.states;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.BotContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 3)
public class ApprovedState implements BotState {

    @Override
    public BaseRequest enter(BotContext context) {
        return new SendMessage(context.getChat().id(), "");
    }
}
