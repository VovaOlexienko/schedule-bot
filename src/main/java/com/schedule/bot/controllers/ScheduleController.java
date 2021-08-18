package com.schedule.bot.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.schedule.bot.security.RolesAllowed;
import com.schedule.dao.DayScheduleDao;
import com.schedule.dao.StudentDao;
import com.schedule.modal.DaySchedule;
import com.schedule.modal.Role;
import com.schedule.utils.KeyboardUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

@BotController
@Log
public class ScheduleController implements TelegramMvcController {

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getToken() {
        return token;
    }

    @Autowired
    DayScheduleDao dayScheduleDao;
    @Autowired
    StudentDao studentDao;
    @Autowired
    KeyboardUtils keyboardUtils;
    @Autowired
    MessageSource messageSource;

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @CallbackQueryRequest(value = "scheduleOnAnotherDay")
    public BaseRequest scheduleOnAnotherDay(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        Locale locale = studentDao.getByChatId(chat.id()).get().getLocale();
        return new SendMessage(chat.id(), messageSource.getMessage("chooseDayMessage", null, locale)).
                replyMarkup(keyboardUtils.getDaysKeyboard(locale));
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @CallbackQueryRequest(value = "scheduleOnToday")
    public BaseRequest scheduleOnToday(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        return getScheduleOnDay(chat, LocalDate.now().getDayOfWeek(), true);
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @CallbackQueryRequest(value = "scheduleOnMonday")
    public BaseRequest scheduleOnMonday(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        return getScheduleOnDay(chat, DayOfWeek.MONDAY);
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @CallbackQueryRequest(value = "scheduleOnTuesday")
    public BaseRequest scheduleOnTuesday(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        return getScheduleOnDay(chat, DayOfWeek.TUESDAY);
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @CallbackQueryRequest(value = "scheduleOnWednesday")
    public BaseRequest scheduleOnWednesday(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        return getScheduleOnDay(chat, DayOfWeek.WEDNESDAY);
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @CallbackQueryRequest(value = "scheduleOnThursday")
    public BaseRequest scheduleOnThursday(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        return getScheduleOnDay(chat, DayOfWeek.THURSDAY);
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @CallbackQueryRequest(value = "scheduleOnFriday")
    public BaseRequest scheduleOnFriday(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        return getScheduleOnDay(chat, DayOfWeek.FRIDAY);
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @CallbackQueryRequest(value = "scheduleOnSaturday")
    public BaseRequest scheduleOnSaturday(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        return getScheduleOnDay(chat, DayOfWeek.SATURDAY);
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @CallbackQueryRequest(value = "scheduleOnSunday")
    public BaseRequest scheduleOnSunday(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        return getScheduleOnDay(chat, DayOfWeek.SUNDAY);
    }

    private BaseRequest getScheduleOnDay(Chat chat, DayOfWeek dayOfWeek) {
        return getScheduleOnDay(chat, dayOfWeek, false);
    }

    private BaseRequest getScheduleOnDay(Chat chat, DayOfWeek dayOfWeek, boolean returnMainMenu) {
        Locale locale = studentDao.getByChatId(chat.id()).get().getLocale();
        Keyboard keyboard = returnMainMenu ? keyboardUtils.getMainKeyboard(locale) : keyboardUtils.getDaysKeyboard(locale);
        Optional<DaySchedule> daySchedule = dayScheduleDao.getByDayAndStudentGroup(
                studentDao.getByChatId(chat.id()).get().getStudentGroup(), dayOfWeek);
        if (daySchedule.isPresent())
            return new SendPhoto(chat.id(), daySchedule.get().getSchedule()).replyMarkup(keyboard);
        return new SendMessage(chat.id(), messageSource.getMessage("noClassesMessage", null, locale)).replyMarkup(keyboard);
    }
}
