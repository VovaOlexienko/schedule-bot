package com.schedule.bot.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.schedule.bot.security.RolesAllowed;
import com.schedule.bot.user.BotContext;
import com.schedule.bot.user.states.StartState;
import com.schedule.dao.StudentDao;
import com.schedule.modal.Role;
import com.schedule.modal.Student;
import com.schedule.utils.KeyboardUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

@BotController
public class RegistrationController implements TelegramMvcController {

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getToken() {
        return token;
    }

    @Autowired
    StudentDao studentDao;
    @Autowired
    StartState startState;
    @Autowired
    KeyboardUtils keyboardUtils;

    @Value("${schedule.filename}")
    String scheduleFileName;

    @MessageRequest(value = "/start")
    public BaseRequest start(Chat chat) {
        return new SendPhoto(chat.id(), "https://web-static.wrike.com/blog/content/uploads/2020/01/Five-Features-of-a-Good-Monthly-Employee-Work-Schedule-Template-896x518.jpg?av=4e36fdf9890d9fb8b26460d2ce565b3c").replyMarkup(keyboardUtils.getLanguagesKeyboard());
    }

    @CallbackQueryRequest(value = "language/english")
    public BaseRequest chooseEnglishLanguage(TelegramBot bot, Chat chat, CallbackQuery callbackQuery, User user, String text) {
        return startState.enter(new BotContext(chat, user, text, Locale.ENGLISH));
    }

    @CallbackQueryRequest(value = "language/ukrainian")
    public BaseRequest chooseUkrainianLanguage(TelegramBot bot, Chat chat, CallbackQuery callbackQuery, User user, String text) {
        return startState.enter(new BotContext(chat, user, text, new Locale("uk")));
    }

    @CallbackQueryRequest(value = "language/russian")
    public BaseRequest chooseRussianLanguage(TelegramBot bot, Chat chat, CallbackQuery callbackQuery, User user, String text) {
        return startState.enter(new BotContext(chat, user, text, new Locale("ru")));
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @CallbackQueryRequest(value = "changeStudentGroup")
    public BaseRequest changeStudentGroup(TelegramBot bot, Chat chat, CallbackQuery callbackQuery, User user, String text) {
        return startState.enter(new BotContext(chat, user, text));
    }

    @RolesAllowed(roles = {Role.Admin, Role.User, Role.Unregistered})
    @MessageRequest(value = "**")
    public BaseRequest enterTextOrFile(Update update, TelegramBot bot, Chat chat, User user, String text) {
        Student student = studentDao.getByChatId(chat.id()).get();
        try {
            return ifAdminQueryThenProcess(student, update, bot, chat);
        } catch (Exception ignored) {
        }
        return student.getUserState().enter(new BotContext(chat, user, text));
    }

    private SendMessage ifAdminQueryThenProcess(Student student, Update update, TelegramBot bot, Chat chat) throws Exception {
        if (!student.getRole().equals(Role.Admin) || update.message().document() == null)
            throw new Exception("No admin query");
        return new SendMessage(chat.id(), saveFileFromTelegram(bot, update.message().document(), scheduleFileName) ? "Success" : "Fail");
    }

    public synchronized boolean saveFileFromTelegram(TelegramBot bot, Document document, String filename) {
        GetFile getFile = new GetFile(document.fileId());
        GetFileResponse getFileResponse = bot.execute(getFile);
        java.io.File localFile = new java.io.File(filename);
        try {
            InputStream is = new URL("https://api.telegram.org/file/bot" + token + "/" + getFileResponse.file().filePath()).openStream();
            FileUtils.copyInputStreamToFile(is, localFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
