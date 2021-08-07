package com.schedule.bot.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.schedule.bot.security.RolesAllowed;
import com.schedule.dao.DayScheduleDao;
import com.schedule.dao.StudentDao;
import com.schedule.dao.StudentGroupDao;
import com.schedule.modal.DaySchedule;
import com.schedule.modal.Role;
import com.schedule.utils.Menu;
import com.schedule.utils.ScheduleUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

@BotController
public class DayScheduleController implements TelegramMvcController {

    @Autowired
    DayScheduleDao dayScheduleDao;
    @Autowired
    StudentGroupDao studentGroupDao;
    @Autowired
    StudentDao studentDao;

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getToken() {
        return token;
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @MessageRequest(value = "\uD83D\uDCDAРозклад на сьогодні\uD83D\uDCDA")
    public BaseRequest scheduleOnToday(Chat chat) {
        return getScheduleOnDay(chat, LocalDate.now().getDayOfWeek());
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @MessageRequest(value = "\uD83D\uDDD3Розклад на інший день\uD83D\uDDD3")
    public BaseRequest scheduleOnAnotherDay(Chat chat) {
        return new SendMessage(chat.id(), "Виберіть день").replyMarkup(new ReplyKeyboardMarkup(Menu.Days.getMenu()).resizeKeyboard(true).selective(true));
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @MessageRequest(value = "Понеділок")
    public BaseRequest scheduleOnMonday(Chat chat) {
        return getScheduleOnDay(chat, DayOfWeek.MONDAY);
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @MessageRequest(value = "Вівторок")
    public BaseRequest scheduleOnTuesday(Chat chat) {
        return getScheduleOnDay(chat, DayOfWeek.TUESDAY);
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @MessageRequest(value = "Середа")
    public BaseRequest scheduleOnWednesday(Chat chat) {
        return getScheduleOnDay(chat, DayOfWeek.WEDNESDAY);
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @MessageRequest(value = "Четвер")
    public BaseRequest scheduleOnThursday(Chat chat) {
        return getScheduleOnDay(chat, DayOfWeek.THURSDAY);
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @MessageRequest(value = "П'ятниця")
    public BaseRequest scheduleOnFriday(Chat chat) {
        return getScheduleOnDay(chat, DayOfWeek.FRIDAY);
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @MessageRequest(value = "Субота")
    public BaseRequest scheduleOnSaturday(Chat chat) {
        return getScheduleOnDay(chat, DayOfWeek.SATURDAY);
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @MessageRequest(value = "Неділя")
    public BaseRequest scheduleOnSunday(Chat chat) {
        return getScheduleOnDay(chat, DayOfWeek.SUNDAY);
    }

    private BaseRequest getScheduleOnDay(Chat chat, DayOfWeek dayOfWeek) {
        Optional<DaySchedule> daySchedule = dayScheduleDao.getScheduleByDay(
                studentDao.getStudentByChatId(chat.id()).get().getStudentGroup(), dayOfWeek);
        if (daySchedule.isPresent()) {
            return new SendPhoto(chat.id(), daySchedule.get().getSchedule());
        }
        return new SendMessage(chat.id(), "В цей день у вашої групи немає занять!");
    }

    /**
     * Three ways to update schedule:
     * - on app start up
     * - once in a while with cron expression
     * - with command from bot
     */

    /*@EventListener(ApplicationReadyEvent.class)
    public void updateScheduleAfterStartup() {
        updateSchedule();
    }*/

    @Scheduled(cron = "${time.updateSchedule}", zone = "${time.zone}")
    private void updateScheduleEveryWeek() {
        updateSchedule();
    }

    @RolesAllowed(roles = {Role.Admin})
    @MessageRequest(value = "/updateSchedule")
    public String updateSchedule(Chat chat) {
        return updateSchedule() ? "Розклад оновлено!" : "Помилка, розклад не вдалося оновити.";
    }

    public synchronized boolean updateSchedule() {
        return new ScheduleUpdater().updateSchedule(studentGroupDao, dayScheduleDao);
    }
}
