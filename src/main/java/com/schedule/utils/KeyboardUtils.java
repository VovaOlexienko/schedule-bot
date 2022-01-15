package com.schedule.utils;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.experimental.UtilityClass;

import static com.schedule.utils.TextUtils.ADDITIONAL_INFO;
import static com.schedule.utils.TextUtils.BUILDINGS_LOCATION;
import static com.schedule.utils.TextUtils.CHANGE_STUDENT_GROUP;
import static com.schedule.utils.TextUtils.FIND_TEACHER_BY_NAME;
import static com.schedule.utils.TextUtils.LESSONS_TIMETABLE;
import static com.schedule.utils.TextUtils.LIST_OF_STUDENT_GROUPS;
import static com.schedule.utils.TextUtils.MAIN_MENU;
import static com.schedule.utils.TextUtils.SCHEDULE_ON_ANOTHER_DAY;
import static com.schedule.utils.TextUtils.SCHEDULE_ON_FRIDAY;
import static com.schedule.utils.TextUtils.SCHEDULE_ON_MONDAY;
import static com.schedule.utils.TextUtils.SCHEDULE_ON_SATURDAY;
import static com.schedule.utils.TextUtils.SCHEDULE_ON_THURSDAY;
import static com.schedule.utils.TextUtils.SCHEDULE_ON_TODAY;
import static com.schedule.utils.TextUtils.SCHEDULE_ON_TUESDAY;
import static com.schedule.utils.TextUtils.SCHEDULE_ON_WEDNESDAY;
import static com.schedule.utils.TextUtils.STUDY_WEEK;
import static com.schedule.utils.TextUtils.TEACHERS;
import static com.schedule.utils.TextUtils.TEACHERS_CONTACTS;

@UtilityClass
public class KeyboardUtils {

    public static InlineKeyboardMarkup studentGroupsKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{new InlineKeyboardButton(LIST_OF_STUDENT_GROUPS).callbackData("listOfStudentGroups")}
        );
    }

    public static InlineKeyboardMarkup mainKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{new InlineKeyboardButton(SCHEDULE_ON_TODAY).callbackData("scheduleOnToday")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(SCHEDULE_ON_ANOTHER_DAY).callbackData("scheduleOnAnotherDay")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(STUDY_WEEK).callbackData("studyWeek")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(ADDITIONAL_INFO).callbackData("additionalInfo")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(CHANGE_STUDENT_GROUP).callbackData("changeStudentGroup")}
        );
    }

    public static InlineKeyboardMarkup daysKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton(SCHEDULE_ON_MONDAY).callbackData("1"),
                        new InlineKeyboardButton(SCHEDULE_ON_TUESDAY).callbackData("2")
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton(SCHEDULE_ON_WEDNESDAY).callbackData("3"),
                        new InlineKeyboardButton(SCHEDULE_ON_THURSDAY).callbackData("4")
                },
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton(SCHEDULE_ON_FRIDAY).callbackData("5"),
                        new InlineKeyboardButton(SCHEDULE_ON_SATURDAY).callbackData("6")
                },
                new InlineKeyboardButton[]{new InlineKeyboardButton(MAIN_MENU).callbackData("mainMenu")}
        );
    }

    public static InlineKeyboardMarkup additionalInfoKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{new InlineKeyboardButton(LESSONS_TIMETABLE).callbackData("lessonsTimetable")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(BUILDINGS_LOCATION).callbackData("buildingsLocation")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(TEACHERS_CONTACTS).callbackData("teachersContacts")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(MAIN_MENU).callbackData("mainMenu")}
        );
    }

    public static InlineKeyboardMarkup aboutTeachersKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{new InlineKeyboardButton(TEACHERS).switchInlineQueryCurrentChat("teachers")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(FIND_TEACHER_BY_NAME).switchInlineQueryCurrentChat("")},
                new InlineKeyboardButton[]{new InlineKeyboardButton(MAIN_MENU).callbackData("mainMenu")}
        );
    }
}
