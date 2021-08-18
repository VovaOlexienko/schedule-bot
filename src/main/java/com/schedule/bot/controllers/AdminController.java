package com.schedule.bot.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.model.Chat;
import com.schedule.bot.security.RolesAllowed;
import com.schedule.dao.DayScheduleDao;
import com.schedule.dao.StudentGroupDao;
import com.schedule.modal.Role;
import com.schedule.utils.schedule.ScheduleUpdater;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.logging.Level;

@BotController
@Log
public class AdminController implements TelegramMvcController {

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getToken() {
        return token;
    }

    @Autowired
    DayScheduleDao dayScheduleDao;
    @Autowired
    StudentGroupDao studentGroupDao;

    @Value("${schedule.filename}")
    private String scheduleFilename;

    @RolesAllowed(roles = {Role.Admin})
    @MessageRequest(value = "/updateSchedule")
    public synchronized String updateSchedule(Chat chat) {
        log.log(Level.INFO, "Start of schedule updating...");
        try {
            new ScheduleUpdater().updateSchedule(studentGroupDao, dayScheduleDao, scheduleFilename);
            log.log(Level.INFO, "Successful end of schedule updating");
            return "Successful end of schedule updating";
        } catch (Exception e) {
            e.printStackTrace();
            log.log(Level.INFO, "Failed end of schedule updating");
            return "Failed end of schedule updating";
        }
    }
}
