package com.schedule.bot.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.InlineQueryRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.security.RolesAllowed;
import com.schedule.dao.StudentDao;
import com.schedule.dao.TeacherDao;
import com.schedule.modal.Role;
import com.schedule.utils.KeyboardUtils;
import com.schedule.utils.ToStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Locale;

@BotController
public class AdditionalInfoController implements TelegramMvcController {

    @Autowired
    StudentDao studentDao;
    @Autowired
    TeacherDao teacherDao;
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

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @CallbackQueryRequest(value = "additionalInfo")
    public BaseRequest additionalInfoMenu(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        Locale locale = studentDao.getStudentByChatId(chat.id()).get().getLocale();
        return new SendMessage(chat.id(), messageSource.getMessage("additionalInfoMessage", null, locale))
                .replyMarkup(keyboardUtils.getAdditionalInfoKeyboard(locale));
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @CallbackQueryRequest(value = "lessonsTimetable")
    public BaseRequest lessonSchedule(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        Locale locale = studentDao.getStudentByChatId(chat.id()).get().getLocale();
        return new SendMessage(chat.id(), messageSource.getMessage("classesTimetableMessage", null, locale))
                .replyMarkup(keyboardUtils.getAdditionalInfoKeyboard(locale));
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @CallbackQueryRequest(value = "teachersContacts")
    public BaseRequest aboutTeacher(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        Locale locale = studentDao.getStudentByChatId(chat.id()).get().getLocale();
        return new SendMessage(chat.id(), messageSource.getMessage("teachersContacts", null, locale)).replyMarkup(keyboardUtils.getAboutTeachersKeyboard(locale));
    }

    @InlineQueryRequest(value = "teachers")
    public BaseRequest teachers(Chat chat, InlineQuery inlineQuery) {
        int page = 0;
        if(!inlineQuery.offset().isEmpty()) page = Integer.parseInt(inlineQuery.offset()) / 25;
        List<InlineQueryResult> teachersResult = ToStringUtils.teacherListToInlineResultList(teacherDao.getAll(PageRequest.of(page, 25, Sort.by(Sort.Direction.ASC, "fullname"))));
        return new AnswerInlineQuery(inlineQuery.id(), teachersResult.toArray(new InlineQueryResult[0])).cacheTime(1).nextOffset(String.valueOf((page + 1) * 25));
    }

    @InlineQueryRequest(value = "**")
    public void findTeachers(TelegramBot bot, Chat chat, InlineQuery inlineQuery, String query) {
        if (query.length() > 2) {
            int page = 0;
            if(!inlineQuery.offset().isEmpty()) page = Integer.parseInt(inlineQuery.offset()) / 25;
            List<InlineQueryResult> teachersResult = ToStringUtils.teacherListToInlineResultList(teacherDao.getTeachersByFullname(query, PageRequest.of(page, 25, Sort.by(Sort.Direction.ASC, "fullname"))));
            bot.execute(new AnswerInlineQuery(inlineQuery.id(), teachersResult.toArray(new InlineQueryResult[0])).cacheTime(1).nextOffset(String.valueOf((page + 1) * 25)));
        }
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @CallbackQueryRequest(value = "mainMenu")
    public BaseRequest backToMainMenu(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        Locale locale = studentDao.getStudentByChatId(chat.id()).get().getLocale();
        return new SendMessage(chat.id(), messageSource.getMessage("mainMenuMessage", null, locale))
                .replyMarkup(keyboardUtils.getMainKeyboard(locale));
    }
}