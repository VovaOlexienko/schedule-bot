package com.schedule.bot.states;

import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.BotContext;
import com.schedule.dao.StudentGroupDao;
import com.schedule.dao.UserDao;
import com.schedule.modal.StudentGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.schedule.utils.TelegramMenu.getMainMenu;

@Component
@Order(value = 2)
public class EnterGroupState implements BotState {

    @Autowired
    UserDao userDao;
    @Autowired
    StudentGroupDao studentGroupDao;

    @Override
    public BaseRequest enter(BotContext context) {
        Optional<StudentGroup> studentGroupOptional = studentGroupDao.getStudentGroupByNumber(context.getText());
        if (!studentGroupOptional.isPresent()) {
            return new SendMessage(context.getChat().id(), "Помилка, не існує групи з номером \"" + context.getText() + "\"! Будь-ласка введіть коректний номер вашої студентської групи. Всі номери груп можна переглянути за допомогою команди /studentGroups.").replyMarkup(new ReplyKeyboardRemove());
        }
        com.schedule.modal.User databaseUser = userDao.getUserByChatId(context.getChat().id()).get();
        databaseUser.setStudentGroup(studentGroupOptional.get());
        databaseUser.setStateId(1);
        userDao.save(databaseUser);
        return new SendMessage(context.getChat().id(), "Гарного користування ботом!").replyMarkup(getMainMenu());
    }
}
