package com.schedule.bot.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.schedule.bot.context.UserContext;
import com.schedule.bot.security.RolesAllowed;
import com.schedule.bot.security.UserRole;
import com.schedule.dao.DayScheduleDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static com.schedule.utils.KeyboardUtils.daysKeyboard;
import static com.schedule.utils.KeyboardUtils.mainKeyboard;
import static com.schedule.utils.TextUtils.CHOOSE_DAY_MESSAGE;
import static com.schedule.utils.TextUtils.NO_CLASSES_MESSAGE;

@BotController
@Log
@RequiredArgsConstructor
public class ScheduleController implements TelegramMvcController {

    private final DayScheduleDao dayScheduleDao;
    private UserContext userContext;

    @RolesAllowed(roles = {UserRole.ADMIN, UserRole.USER})
    @CallbackQueryRequest(value = "scheduleOnAnotherDay")
    public BaseRequest scheduleOnAnotherDay(Chat chat, TelegramBot bot, CallbackQuery callbackQuery) {
        return new SendMessage(chat.id(), CHOOSE_DAY_MESSAGE).replyMarkup(daysKeyboard());
    }

    @RolesAllowed(roles = {UserRole.ADMIN, UserRole.USER})
    @CallbackQueryRequest(value = "scheduleOnToday")
    public BaseRequest scheduleOnToday(Chat chat, TelegramBot bot, CallbackQuery callbackQuery) {
        return getScheduleOnDay(chat, LocalDate.now().getDayOfWeek(), mainKeyboard());
    }

    @RolesAllowed(roles = {UserRole.ADMIN, UserRole.USER})
    @CallbackQueryRequest(value = "**")
    public BaseRequest schedule(Chat chat, String text, TelegramBot bot, CallbackQuery callbackQuery) {
        return getScheduleOnDay(chat, DayOfWeek.of(Integer.parseInt(text)), daysKeyboard());
    }

    private BaseRequest getScheduleOnDay(Chat chat, DayOfWeek dayOfWeek, InlineKeyboardMarkup keyboard) {
        return dayScheduleDao.getByDayAndStudentGroup(userContext.getStudent().getStudentGroup(), dayOfWeek)
                .map(d -> (BaseRequest) new SendPhoto(chat.id(), d.getSchedule()).replyMarkup(keyboard))
                .orElse(new SendMessage(chat.id(), NO_CLASSES_MESSAGE).replyMarkup(keyboard));
    }

    @Autowired
    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
    }

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getToken() {
        return token;
    }
}
