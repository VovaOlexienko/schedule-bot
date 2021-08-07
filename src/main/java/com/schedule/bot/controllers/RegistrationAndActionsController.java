package com.schedule.bot.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.controllers.user.actions.handlers.FirstActionHandler;
import com.schedule.bot.controllers.user.registration.states.StartState;
import com.schedule.bot.security.RolesAllowed;
import com.schedule.dao.StudentDao;
import com.schedule.dao.StudentGroupDao;
import com.schedule.modal.Role;
import com.schedule.modal.Student;
import com.schedule.modal.StudentGroup;
import com.schedule.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.stream.Collectors;

@BotController
public class RegistrationAndActionsController implements TelegramMvcController {

    @Autowired
    StudentDao studentDao;
    @Autowired
    StudentGroupDao studentGroupDao;
    @Autowired
    StartState startState;
    @Autowired
    FirstActionHandler firstActionHandler;

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getToken() {
        return token;
    }

    @MessageRequest(value = "/start")
    public BaseRequest start(TelegramBot telegramBot, User user, Chat chat, String text) {
        telegramBot.execute(new SendMessage(chat.id(), "Привіт, " + user.firstName() + "!"));
        return startState.enter(new BotContext(chat, user, text));
    }

    @RolesAllowed(roles = {Role.Admin, Role.User, Role.Unregistered})
    @MessageRequest(value = "**")
    public BaseRequest registration(User user, Chat chat, String text) throws Exception {
        Student student = studentDao.getStudentByChatId(chat.id()).get();
        BotContext context = new BotContext(chat, user, text);
        if (student.getNextAction() != null) {
            BaseRequest baseRequest = firstActionHandler.handle(student.getNextAction(), context);
            student.setNextAction(null);
            studentDao.save(student);
            return baseRequest;
        }
        return student.getRegistrationState().enter(context);
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @MessageRequest(value = "\uD83C\uDF93Змінити групу\uD83C\uDF93")
    public BaseRequest changeStudentGroup(User user, Chat chat, String text) {
        return startState.enter(new BotContext(chat, user, text));
    }

    @RolesAllowed(roles = {Role.Admin, Role.User, Role.Unregistered})
    @MessageRequest(value = "\uD83C\uDF93Перелік всіх груп\uD83C\uDF93")
    public String studentGroups(Chat chat) {
        return "Всі групи доступні в боті: " + Utils.toStringGroupsList(studentGroupDao.getAll())
                + "\n\nЯкщо в гугл таблиці університету в двох груп однаковий розклад, то в боті використовуються такі правила:\n"
                + "- '103.107' => '103'\n"
                + "- '103,107' => '103,107'";
    }
}
