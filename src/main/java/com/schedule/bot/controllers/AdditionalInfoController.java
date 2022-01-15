package com.schedule.bot.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.InlineQueryRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.request.InlineQueryResult;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.schedule.bot.security.RolesAllowed;
import com.schedule.bot.security.UserRole;
import com.schedule.dao.StudentGroupDao;
import com.schedule.dao.TeacherDao;
import com.schedule.utils.BotAnswersUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static com.schedule.utils.KeyboardUtils.aboutTeachersKeyboard;
import static com.schedule.utils.KeyboardUtils.additionalInfoKeyboard;
import static com.schedule.utils.KeyboardUtils.mainKeyboard;
import static com.schedule.utils.KeyboardUtils.studentGroupsKeyboard;
import static com.schedule.utils.TextUtils.ADDITIONAL_INFO_MESSAGE;
import static com.schedule.utils.TextUtils.ALL_GROUPS_MESSAGE;
import static com.schedule.utils.TextUtils.BUILDINGS_LOCATION;
import static com.schedule.utils.TextUtils.CLASSES_TIMETABLE_MESSAGE;
import static com.schedule.utils.TextUtils.MAIN_MENU_MESSAGE;
import static com.schedule.utils.TextUtils.RULE_OF_GROUP_CHOSEN_MESSAGE;
import static com.schedule.utils.TextUtils.STUDY_WEEK_ABOVE;
import static com.schedule.utils.TextUtils.STUDY_WEEK_UNDER;
import static com.schedule.utils.TextUtils.TEACHERS_CONTACTS;

@BotController
@RequiredArgsConstructor
public class AdditionalInfoController implements TelegramMvcController {

    private final StudentGroupDao studentGroupDao;
    private final TeacherDao teacherDao;

    @Value("${study.date.start}")
    private String studyStartDay;

    @RolesAllowed(roles = {UserRole.ADMIN, UserRole.USER, UserRole.UNREGISTERED})
    @CallbackQueryRequest(value = "listOfStudentGroups")
    public BaseRequest allStudentGroupsInBot(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        return new SendMessage(chat.id(), String.format("%s %s %s", ALL_GROUPS_MESSAGE,
                BotAnswersUtils.studentGroupsListToString(studentGroupDao.getAll()), RULE_OF_GROUP_CHOSEN_MESSAGE))
                .replyMarkup(studentGroupsKeyboard());
    }

    @RolesAllowed(roles = {UserRole.ADMIN, UserRole.USER})
    @CallbackQueryRequest(value = "additionalInfo")
    public BaseRequest additionalInfoMenu(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        return new SendMessage(chat.id(), ADDITIONAL_INFO_MESSAGE).replyMarkup(additionalInfoKeyboard());
    }

    @RolesAllowed(roles = {UserRole.ADMIN, UserRole.USER})
    @CallbackQueryRequest(value = "lessonsTimetable")
    public BaseRequest classesSchedule(Chat chat, TelegramBot bot, CallbackQuery callbackQuery) {
        return new SendMessage(chat.id(), CLASSES_TIMETABLE_MESSAGE).replyMarkup(additionalInfoKeyboard());
    }

    @RolesAllowed(roles = {UserRole.ADMIN, UserRole.USER})
    @CallbackQueryRequest(value = "teachersContacts")
    public BaseRequest aboutTeacher(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        return new SendMessage(chat.id(), TEACHERS_CONTACTS).replyMarkup(aboutTeachersKeyboard());
    }

    @InlineQueryRequest(value = "teachers")
    public BaseRequest allTeachersInBot(Chat chat, InlineQuery inlineQuery) {
        int page = 0;
        if (!inlineQuery.offset().isEmpty()) page = Integer.parseInt(inlineQuery.offset()) / 25;
        List<InlineQueryResult> teachersResult = BotAnswersUtils.teacherListToInlineResultList(teacherDao.getAll(PageRequest.of(page, 25, Sort.by(Sort.Direction.ASC, "fullname"))));
        return new AnswerInlineQuery(inlineQuery.id(), teachersResult.toArray(new InlineQueryResult[0])).cacheTime(300).nextOffset(String.valueOf((page + 1) * 25));
    }

    @InlineQueryRequest(value = "**")
    public void findTeachers(TelegramBot bot, Chat chat, InlineQuery inlineQuery, String query) {
        if (query.length() > 2) {
            int page = 0;
            if (!inlineQuery.offset().isEmpty()) page = Integer.parseInt(inlineQuery.offset()) / 25;
            List<InlineQueryResult> teachersResult = BotAnswersUtils.teacherListToInlineResultList(teacherDao.getByFullname(query, PageRequest.of(page, 25, Sort.by(Sort.Direction.ASC, "fullname"))));
            bot.execute(new AnswerInlineQuery(inlineQuery.id(), teachersResult.toArray(new InlineQueryResult[0])).cacheTime(300).nextOffset(String.valueOf((page + 1) * 25)));
        }
    }

    @RolesAllowed(roles = {UserRole.ADMIN, UserRole.USER})
    @CallbackQueryRequest(value = "mainMenu")
    public BaseRequest backToMainMenu(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        return new SendMessage(chat.id(), MAIN_MENU_MESSAGE).replyMarkup(mainKeyboard());
    }

    @RolesAllowed(roles = {UserRole.ADMIN, UserRole.USER})
    @CallbackQueryRequest(value = "buildingsLocation")
    public BaseRequest buildingsLocation(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        return new SendPhoto(chat.id(), "https://i.ibb.co/rxwFrwx/image.png").caption(BUILDINGS_LOCATION)
                .replyMarkup(additionalInfoKeyboard());
    }

    @RolesAllowed(roles = {UserRole.ADMIN, UserRole.USER})
    @CallbackQueryRequest(value = "studyWeek")
    public BaseRequest studyWeek(TelegramBot bot, Chat chat, CallbackQuery callbackQuery) {
        int[] date = Arrays.stream(studyStartDay.split("/")).mapToInt(Integer::parseInt).toArray();
        long weeks = ChronoUnit.WEEKS.between(LocalDate.of(date[0], date[1], date[2]), LocalDate.now());
        String text = weeks % 2 == 0 ? STUDY_WEEK_ABOVE : STUDY_WEEK_UNDER;
        return new SendMessage(chat.id(), text).replyMarkup(mainKeyboard());
    }

    @MessageRequest(value = "/myChatId")
    public String myChatId(Chat chat) {
        return String.valueOf(chat.id());
    }

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getToken() {
        return token;
    }
}