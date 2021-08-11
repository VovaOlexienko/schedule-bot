package com.schedule.bot.user.registration.states;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.user.registration.BotContext;
import com.schedule.dao.StudentDao;
import com.schedule.modal.Role;
import com.schedule.modal.Student;
import com.schedule.utils.KeyboardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class StartState extends RegistrationState {

    @Autowired
    StudentDao studentDao;
    @Autowired
    EnterGroupState enterGroupState;
    @Autowired
    KeyboardUtils keyboardUtils;
    @Autowired
    MessageSource messageSource;

    @Override
    public BaseRequest enter(BotContext context) {
        Student student = studentDao.getStudentByChatId(context.getChat().id()).orElse(new Student(context.getChat().id()));
        student.setRegistrationState(nextState());
        if (context.getLocale() != null) {
            student.setLocale(context.getLocale());
        }
        student.setStudentGroup(null);
        student.setRole(Role.Unregistered);
        studentDao.save(student);
        return new SendMessage(context.getChat().id(), messageSource.getMessage("enterGroupMessage", null, student.getLocale())).replyMarkup(keyboardUtils.getStudentsGroupsKeyboard(student.getLocale()));
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
