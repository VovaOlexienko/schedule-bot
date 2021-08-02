package com.schedule.bot.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.schedule.dao.DayScheduleDao;
import com.schedule.dao.StudentGroupDao;
import com.schedule.dao.UserDao;
import com.schedule.modal.DaySchedule;
import com.schedule.modal.User;
import com.schedule.utils.ScheduleUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

import static com.schedule.utils.TelegramMenu.getDayMenu;
import static com.schedule.utils.TelegramMenu.getMainMenu;

@BotController
public class ScheduleController implements TelegramMvcController {

    @Autowired
    DayScheduleDao dayScheduleDao;
    @Autowired
    StudentGroupDao studentGroupDao;
    @Autowired
    UserDao userDao;

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getToken() {
        return token;
    }

    @MessageRequest(value = "\uD83D\uDCDAРозклад на сьогодні\uD83D\uDCDA")
    public BaseRequest scheduleOnToday(Chat chat) {
        return getScheduleOnDay(chat, LocalDate.now().getDayOfWeek());
    }

    @MessageRequest(value = "\uD83D\uDDD3Розклад на інший день\uD83D\uDDD3")
    public BaseRequest scheduleOnAnotherDay(Chat chat) {
        return new SendMessage(chat.id(), "Виберіть день").replyMarkup(getDayMenu());
    }

    @MessageRequest(value = "⏰Розклад початку та завершення занять⏰")
    public BaseRequest lessonSchedule(Chat chat) {
        return new SendMessage(chat.id(), "1 пара  8:50 – 10:10\n" +
                "\n" +
                "2 пара  10:20 – 11:40\n" +
                "\n" +
                "3 пара  12:40 – 14:00\n" +
                "\n" +
                "4 пара  14:10 – 15:30\n" +
                "\n" +
                "5 пара 15:40 – 17:00\n" +
                "\n" +
                "6 пара 17:10 – 18:30").replyMarkup(getMainMenu());
    }

    @MessageRequest(value = "Понеділок")
    public BaseRequest scheduleOnMonday(Chat chat) {
        return getScheduleOnDay(chat, DayOfWeek.MONDAY);
    }

    @MessageRequest(value = "Вівторок")
    public BaseRequest scheduleOnTuesday(Chat chat) {
        return getScheduleOnDay(chat, DayOfWeek.TUESDAY);
    }

    @MessageRequest(value = "Середа")
    public BaseRequest scheduleOnWednesday(Chat chat) {
        return getScheduleOnDay(chat, DayOfWeek.WEDNESDAY);
    }

    @MessageRequest(value = "Четвер")
    public BaseRequest scheduleOnThursday(Chat chat) {
        return getScheduleOnDay(chat, DayOfWeek.THURSDAY);
    }

    @MessageRequest(value = "П'ятниця")
    public BaseRequest scheduleOnFriday(Chat chat) {
        return getScheduleOnDay(chat, DayOfWeek.FRIDAY);
    }

    @MessageRequest(value = "Субота")
    public BaseRequest scheduleOnSaturday(Chat chat) {
        return getScheduleOnDay(chat, DayOfWeek.SATURDAY);
    }

    @MessageRequest(value = "Неділя")
    public BaseRequest scheduleOnSunday(Chat chat) {
        return getScheduleOnDay(chat, DayOfWeek.SUNDAY);
    }

    @MessageRequest(value = "Головне меню\uD83D\uDD3C")
    public BaseRequest backToMainMenu(Chat chat) {
        return new SendMessage(chat.id(), "Головне меню").replyMarkup(getMainMenu());
    }

    private BaseRequest getScheduleOnDay(Chat chat, DayOfWeek dayOfWeek) {
        Optional<User> userOptional = userDao.getUserByChatId(chat.id());
        if (!userOptional.isPresent() || userOptional.get().getStudentGroup() == null) {
            return new SendMessage(chat.id(), "Помилка, введіть дані для реєстрації! /start");
        }
        Optional<DaySchedule> daySchedule = dayScheduleDao.getScheduleByDay(userOptional.get().getStudentGroup(), dayOfWeek);
        if (daySchedule.isPresent()) {
            return new SendPhoto(chat.id(), daySchedule.get().getSchedule()).replyMarkup(getMainMenu());
        }
        return new SendMessage(chat.id(), "В цей день у вашої групи немає занять!").replyMarkup(getMainMenu());
    }

    @MessageRequest(value = "/upgradeSchedule")
    public synchronized BaseRequest updateSchedule(Chat chat) {
        return new SendMessage(chat.id(), updateSchedule() ? "Розклад оновлено!" : "Помилка, розклад не вдалося оновити.")
                .replyMarkup(getMainMenu());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void updateScheduleAfterStartup() {
        updateSchedule();
    }

    @Scheduled(cron = "${time.updateSchedule}", zone = "${time.zone}")
    private void updateScheduleEveryWeek() {
        updateSchedule();
    }

    public synchronized boolean updateSchedule() {
        return new ScheduleUpdater().updateSchedule(studentGroupDao, dayScheduleDao);
    }
}
