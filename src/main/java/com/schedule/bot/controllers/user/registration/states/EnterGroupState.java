package com.schedule.bot.controllers.user.registration.states;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.controllers.BotContext;
import com.schedule.dao.StudentDao;
import com.schedule.dao.StudentGroupDao;
import com.schedule.modal.Role;
import com.schedule.modal.Student;
import com.schedule.modal.StudentGroup;
import com.schedule.utils.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EnterGroupState extends RegistrationState {

    @Autowired
    StudentDao studentDao;
    @Autowired
    StudentGroupDao studentGroupDao;
    @Autowired
    StartState startState;
    @Autowired
    ApprovedState approvedState;

    @Override
    public BaseRequest enter(BotContext context) {
        Optional<StudentGroup> studentGroupOptional = studentGroupDao.getStudentGroupByNumber(context.getText());
        if (!studentGroupOptional.isPresent()) {
            return new SendMessage(context.getChat().id(), "Помилка, не існує групи з номером \"" + context.getText() + "\"! Будь-ласка введіть коректний номер вашої студентської групи. Всі номери груп можна переглянути за допомогою відповідної кнопки в меню.");
        }
        Student student = studentDao.getStudentByChatId(context.getChat().id()).get();
        student.setStudentGroup(studentGroupOptional.get());
        student.setRegistrationState(nextState());
        student.setRole(Role.User);
        studentDao.save(student);
        return new SendMessage(context.getChat().id(), "Гарного користування ботом!").replyMarkup(new ReplyKeyboardMarkup(Menu.Main.getMenu()).resizeKeyboard(true).selective(true));
    }

    @Override
    public RegistrationState nextState() {
        return approvedState;
    }

    @Override
    public RegistrationState previousState() {
        return startState;
    }

    @Override
    public String stateName() {
        return "EnterGroupState";
    }
}
