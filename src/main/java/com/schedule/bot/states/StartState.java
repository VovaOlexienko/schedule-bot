package com.schedule.bot.states;

import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.BotContext;
import com.schedule.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class StartState implements BotState {

    @Autowired
    UserDao userDao;

    @Override
    public BaseRequest enter(BotContext context) {
        com.schedule.modal.User user = userDao.getUserByChatId(context.getChat().id()).orElse(new com.schedule.modal.User(context.getChat().id()));
        user.setStateId(0);
        user.setStudentGroup(null);
        userDao.save(user);
        return new SendMessage(context.getChat().id(), "Введіть номер вашої студентської групи. Всі номери груп можна переглянути за допомогою команди /studentGroups.").replyMarkup(new ReplyKeyboardRemove());
    }
}
