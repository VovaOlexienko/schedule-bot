package com.schedule.bot.registration;

import com.pengrad.telegrambot.model.Chat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegistrationRequest {
    private final Chat chat;
    private final String text;
}
