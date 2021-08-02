package com.schedule.bot.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.BotContext;
import com.schedule.bot.states.BotState;
import com.schedule.dao.StudentGroupDao;
import com.schedule.dao.UserDao;
import com.schedule.modal.StudentGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Optional;

@BotController
public class RegistrationController implements TelegramMvcController {

    @Autowired
    UserDao userDao;
    @Autowired
    StudentGroupDao studentGroupDao;
    @Autowired
    @Order
    private List<BotState> botStates;

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getToken() {
        return token;
    }

    @MessageRequest(value = "/start")
    public BaseRequest start(TelegramBot telegramBot, User user, Chat chat, String text) {
        telegramBot.execute(new SendMessage(chat.id(), "Привіт, " + user.firstName() + "!"));
        return botStates.get(0).enter(new BotContext(chat, user, text));
    }

    @MessageRequest(value = "**")
    public BaseRequest registration(User user, Chat chat, String text) {
        Optional<com.schedule.modal.User> databaseUser = userDao.getUserByChatId(chat.id());
        if (!databaseUser.isPresent()) {
            return new SendMessage(chat.id(), "Помилка, використайте команду /start, щоб розпочати використовувати бота.");
        }
        if (!text.isEmpty()) text = text.trim();
        BotContext context = new BotContext(chat, user, text.trim());
        return botStates.get((databaseUser.get().getStateId() + 1) % botStates.size()).enter(context);
    }

    @MessageRequest(value = "\uD83C\uDF93Змінити групу\uD83C\uDF93")
    public BaseRequest changeStudentGroup(User user, Chat chat, String text) {
        return botStates.get(0).enter(new BotContext(chat, user, text));
    }

    @MessageRequest(value = "/studentGroups")
    public BaseRequest studentGroups(Chat chat) {
        StringBuilder stringBuilder = new StringBuilder();
        for (StudentGroup studentGroup : studentGroupDao.getAll()) {
            stringBuilder.append(studentGroup.getNumber()).append(", ");
        }
        return new SendMessage(chat.id(), "Всі групи доступні в боті: " + stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length()).toString());
    }
}
