package com.schedule.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BotContext {
    private final Chat chat;
    private final User user;
    private final String text;
}
