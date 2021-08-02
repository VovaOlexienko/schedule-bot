package com.schedule.bot.states;

import com.pengrad.telegrambot.request.BaseRequest;
import com.schedule.bot.BotContext;

public interface BotState {
    BaseRequest enter(BotContext context);
}