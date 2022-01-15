package com.schedule.bot.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.security.RolesAllowed;
import com.schedule.bot.security.UserRole;
import com.schedule.dao.StudentDao;
import com.schedule.utils.schedule.ScheduleUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;

@BotController
@Log
@RequiredArgsConstructor
public class AdminController implements TelegramMvcController {

    private final StudentDao studentDao;
    private final ScheduleUpdater scheduleUpdater;

    @Value("${schedule.filename}")
    private String scheduleFileName;

    @RolesAllowed(roles = {UserRole.ADMIN})
    @MessageRequest(value = "/message {text}")
    public BaseRequest messageForAllUsers(Chat chat, TelegramBot bot, @BotPathVariable("text") String text) {
        studentDao.getAll().forEach(student -> bot.execute(new SendMessage(student.getChatId(), text)));
        return new SendMessage(chat.id(), "Mailing is over");
    }

    @RolesAllowed(roles = {UserRole.ADMIN})
    @MessageRequest(value = "/updateSchedule")
    public synchronized BaseRequest updateSchedule(Chat chat, Update update, TelegramBot bot) {
        try {
            scheduleUpdater.updateSchedule(scheduleFileName);
            return new SendMessage(chat.id(), "Success updated schedule!");
        } catch (Exception e) {
            return new SendMessage(chat.id(), "Failed to update schedule!");
        }
    }

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getToken() {
        return token;
    }
}
