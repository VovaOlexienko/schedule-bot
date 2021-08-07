package com.schedule.bot.controllers.user.actions.handlers;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.controllers.BotContext;
import com.schedule.bot.controllers.user.actions.Action;
import com.schedule.dao.TeacherDao;
import com.schedule.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FindTeacherByNameHandler extends ActionHandler {

    @Autowired
    TeacherDao teacherDao;

    @Override
    public BaseRequest handle(Action action, BotContext context) throws Exception {
        if (action == Action.FindTeacherByName) {
            String s = Utils.toStringTeacherList(teacherDao.getTeachersByFullname(context.getText()));
            return new SendMessage(context.getChat().id(), s.isEmpty() ? "There are no teacher with this name" : s);
        }
        return nextHandler.handle(action, context);
    }
}
