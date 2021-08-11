package com.schedule.utils;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class KeyboardUtils {

    @Autowired
    MessageSource messageSource;

    public InlineKeyboardMarkup getLanguagesKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{new InlineKeyboardButton("Choose language: English").callbackData("language/english")},
                new InlineKeyboardButton[]{new InlineKeyboardButton("Обрати мову: Українська").callbackData("language/ukrainian")},
                new InlineKeyboardButton[]{new InlineKeyboardButton("Выбрать язык: Русский").callbackData("language/russian")}
        );
    }

    public InlineKeyboardMarkup getStudentsGroupsKeyboard(Locale locale) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("listOfStudentGroups", null, locale)).callbackData("listOfStudentGroups")}
                );
    }

    public InlineKeyboardMarkup getMainKeyboard(Locale locale) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("scheduleOnToday", null, locale)).callbackData("scheduleOnToday")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("scheduleOnAnotherDay", null, locale)).callbackData("scheduleOnAnotherDay")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("additionalInfo", null, locale)).callbackData("additionalInfo")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("changeStudentGroup", null, locale)).callbackData("changeStudentGroup")}
                );
    }

    public InlineKeyboardMarkup getDaysKeyboard(Locale locale) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton(messageSource.getMessage("scheduleOnMonday", null, locale)).callbackData("scheduleOnMonday")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("scheduleOnTuesday", null, locale)).callbackData("scheduleOnTuesday")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("scheduleOnWednesday", null, locale)).callbackData("scheduleOnWednesday")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("scheduleOnThursday", null, locale)).callbackData("scheduleOnThursday")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("scheduleOnFriday", null, locale)).callbackData("scheduleOnFriday")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("scheduleOnSaturday", null, locale)).callbackData("scheduleOnSaturday")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("mainMenu", null, locale)).callbackData("mainMenu")}
        );
    }

    public InlineKeyboardMarkup getAdditionalInfoKeyboard(Locale locale) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("lessonsTimetable", null, locale)).callbackData("lessonsTimetable")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("teachersContacts", null, locale)).callbackData("teachersContacts")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("mainMenu", null, locale)).callbackData("mainMenu")}
        );
    }

    public InlineKeyboardMarkup getAboutTeachersKeyboard(Locale locale) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("teachers", null, locale)).switchInlineQueryCurrentChat("teachers")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("findTeacherByName", null, locale)).switchInlineQueryCurrentChat("")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageSource.getMessage("mainMenu", null, locale)).callbackData("mainMenu")}
        );
    }
}
