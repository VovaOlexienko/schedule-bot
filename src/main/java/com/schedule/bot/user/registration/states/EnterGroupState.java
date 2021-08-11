package com.schedule.bot.user.registration.states;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.user.registration.BotContext;
import com.schedule.dao.StudentDao;
import com.schedule.dao.StudentGroupDao;
import com.schedule.modal.Role;
import com.schedule.modal.Student;
import com.schedule.modal.StudentGroup;
import com.schedule.utils.KeyboardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
    @Autowired
    KeyboardUtils keyboardUtils;
    @Autowired
    MessageSource messageSource;

    @Override
    public BaseRequest enter(BotContext context) {
        Optional<StudentGroup> studentGroupOptional = studentGroupDao.getStudentGroupByNumber(context.getText());
        Student student = studentDao.getStudentByChatId(context.getChat().id()).get();
        if (!studentGroupOptional.isPresent()) {
            return new SendMessage(context.getChat().id(), messageSource.getMessage("enterStudentGroupException", null, student.getLocale()))
                    .replyMarkup(keyboardUtils.getStudentsGroupsKeyboard(student.getLocale()));
        }
        student.setStudentGroup(studentGroupOptional.get());
        student.setRegistrationState(nextState());
        student.setRole(Role.User);
        studentDao.save(student);
        return new SendMessage(context.getChat().id(), messageSource.getMessage("goodLuckForUserMessage", null, student.getLocale())).replyMarkup(keyboardUtils.getMainKeyboard(student.getLocale()));
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
