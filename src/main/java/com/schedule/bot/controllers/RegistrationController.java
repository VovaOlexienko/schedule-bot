package com.schedule.bot.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.schedule.bot.context.UserContext;
import com.schedule.bot.registration.RegistrationRequest;
import com.schedule.bot.registration.states.StartRegistrationState;
import com.schedule.bot.security.RolesAllowed;
import com.schedule.bot.security.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.URL;
import java.util.Objects;

@BotController
@RequiredArgsConstructor
public class RegistrationController implements TelegramMvcController {

    private final StartRegistrationState startState;
    private UserContext userContext;

    @Value("${schedule.filename}")
    private String scheduleFileName;

    @MessageRequest(value = "/start")
    public BaseRequest start(Chat chat, String text) {
        return startState.enter(new RegistrationRequest(chat, text));
    }

    @RolesAllowed(roles = {UserRole.ADMIN, UserRole.USER, UserRole.UNREGISTERED})
    @MessageRequest(value = "**")
    public BaseRequest nextRegistrationState(Chat chat, String text, Update update, TelegramBot bot) {
        saveFileFromTelegram(bot, update, scheduleFileName);
        return userContext.getUserRegistrationState().enter(new RegistrationRequest(chat, text));
    }

    @RolesAllowed(roles = {UserRole.ADMIN, UserRole.USER})
    @CallbackQueryRequest(value = "changeStudentGroup")
    public BaseRequest changeStudentGroup(Chat chat, String text, TelegramBot bot, CallbackQuery callbackQuery) {
        return startState.enter(new RegistrationRequest(chat, text));
    }

    @SneakyThrows
    private void saveFileFromTelegram(TelegramBot bot, Update update, String filename) {
        if (Objects.nonNull(userContext.getStudent()) && userContext.getStudent().getUserRole() == UserRole.ADMIN) {
            Document document = update.message().document();
            if (update.message().caption().equals("/updateSchedule") || document.fileName().endsWith(".xlsx")) {
                GetFileResponse getFileResponse = bot.execute(new GetFile(document.fileId()));
                URL url = new URL(String.format("https://api.telegram.org/file/bot%s/%s", token, getFileResponse.file().filePath()));
                FileUtils.copyInputStreamToFile(url.openStream(), new java.io.File(filename));
            } else {
                throw new IllegalArgumentException("Bad type of file!");
            }
        }
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
