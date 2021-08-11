package com.schedule.bot.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.*;
import com.schedule.bot.user.registration.BotContext;
import com.schedule.bot.user.registration.states.StartState;
import com.schedule.bot.security.RolesAllowed;
import com.schedule.dao.StudentDao;
import com.schedule.dao.StudentGroupDao;
import com.schedule.modal.Role;
import com.schedule.modal.Student;
import com.schedule.utils.KeyboardUtils;
import com.schedule.utils.ToStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;

import java.util.Locale;

@BotController
public class RegistrationController implements TelegramMvcController {

    @Autowired
    StudentDao studentDao;
    @Autowired
    StudentGroupDao studentGroupDao;
    @Autowired
    StartState startState;
    @Autowired
    KeyboardUtils keyboardUtils;
    @Autowired
    MessageSource messageSource;

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getToken() {
        return token;
    }

    @MessageRequest(value = "/start")
    public BaseRequest start(Chat chat) {
        return new SendPhoto(chat.id(), "https://web-static.wrike.com/blog/content/uploads/2020/01/Five-Features-of-a-Good-Monthly-Employee-Work-Schedule-Template-896x518.jpg?av=4e36fdf9890d9fb8b26460d2ce565b3c").replyMarkup(keyboardUtils.getLanguagesKeyboard());
    }

    @CallbackQueryRequest(value = "language/english")
    public BaseRequest english(TelegramBot bot, Chat chat, CallbackQuery callbackQuery, User user, String text) {
        return startState.enter(new BotContext(chat, user, text, Locale.ENGLISH));
    }

    @CallbackQueryRequest(value = "language/ukrainian")
    public BaseRequest ukrainian(TelegramBot bot, Chat chat, CallbackQuery callbackQuery, User user, String text) {
        return startState.enter(new BotContext(chat, user, text, new Locale("uk")));
    }

    @CallbackQueryRequest(value = "language/russian")
    public BaseRequest russian(TelegramBot bot, Chat chat, CallbackQuery callbackQuery, User user, String text) {
        return startState.enter(new BotContext(chat, user, text, new Locale("ru")));
    }

    @RolesAllowed(roles = {Role.Admin, Role.User, Role.Unregistered})
    @MessageRequest(value = "**")
    public BaseRequest registration(Chat chat, User user, String text) {
        Student student = studentDao.getStudentByChatId(chat.id()).get();
        return student.getRegistrationState().enter(new BotContext(chat, user, text));
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @CallbackQueryRequest(value = "changeStudentGroup")
    public BaseRequest changeStudentGroup(TelegramBot bot, Chat chat, CallbackQuery callbackQuery, User user, String text) {
        return startState.enter(new BotContext(chat, user, text));
    }

    @RolesAllowed(roles = {Role.Admin, Role.User, Role.Unregistered})
    @CallbackQueryRequest(value = "listOfStudentGroups")
    public BaseRequest studentGroups(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        Locale locale = studentDao.getStudentByChatId(chat.id()).get().getLocale();
        return new SendMessage(chat.id(), messageSource.getMessage("allGroupsMessage", null, locale)
                + ToStringUtils.studentGroupsListToString(studentGroupDao.getAll())
                + messageSource.getMessage("ruleOfGroupChosenMessage", null, locale))
                .replyMarkup(keyboardUtils.getStudentsGroupsKeyboard(locale));
    }
}
