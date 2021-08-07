package com.schedule.bot.controllers.user.registration.states;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.controllers.BotContext;
import com.schedule.dao.StudentDao;
import com.schedule.modal.Role;
import com.schedule.modal.Student;
import com.schedule.utils.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartState extends RegistrationState {

    @Autowired
    StudentDao studentDao;
    @Autowired
    EnterGroupState enterGroupState;

    @Override
    public BaseRequest enter(BotContext context) {
        Student student = studentDao.getStudentByChatId(context.getChat().id()).orElse(new Student(context.getChat().id()));
        student.setRegistrationState(nextState());
        student.setStudentGroup(null);
        student.setNextAction(null);
        student.setRole(Role.Unregistered);
        studentDao.save(student);
        return new SendMessage(context.getChat().id(), "Введіть номер вашої студентської групи. Всі номери груп можна переглянути за допомогою відповідної кнопки в меню.").replyMarkup(new ReplyKeyboardMarkup(Menu.StudentsGroups.getMenu()).resizeKeyboard(true).selective(true));
    }

    @Override
    public RegistrationState nextState() {
        return enterGroupState;
    }

    @Override
    public RegistrationState previousState() {
        return this;
    }

    @Override
    public String stateName() {
        return "StartState";
    }
}
