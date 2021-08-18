package com.schedule.bot.user;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@Getter
@AllArgsConstructor
public class BotContext {
    private final Chat chat;
    private final User user;
    private final String text;
    private Locale locale;

    public BotContext(Chat chat, User user, String text) {
        this.chat = chat;
        this.user = user;
        this.text = text;
    }
}
