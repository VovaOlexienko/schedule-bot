package com.schedule.bot.registration.states;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.context.UserContext;
import com.schedule.bot.registration.RegistrationRequest;
import com.schedule.bot.security.UserRole;
import com.schedule.dao.StudentDao;
import com.schedule.dao.StudentGroupDao;
import com.schedule.modal.Student;
import com.schedule.modal.StudentGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.schedule.utils.KeyboardUtils.mainKeyboard;
import static com.schedule.utils.KeyboardUtils.studentGroupsKeyboard;
import static com.schedule.utils.TextUtils.ENTER_STUDENT_GROUP_EXCEPTION;
import static com.schedule.utils.TextUtils.GOOD_LUCK_FOR_USER_MESSAGE;

@Component
@RequiredArgsConstructor
public class EnterGroupRegistrationState implements UserRegistrationState {

    private final StudentDao studentDao;
    private final StudentGroupDao studentGroupDao;
    private final EndRegistrationState approvedState;
    private UserContext userContext;

    @Override
    public BaseRequest enter(RegistrationRequest context) {
        Optional<StudentGroup> studentGroupOptional = studentGroupDao.getByNumber(context.getText());
        if (!studentGroupOptional.isPresent()) {
            return new SendMessage(context.getChat().id(), ENTER_STUDENT_GROUP_EXCEPTION)
                    .replyMarkup(studentGroupsKeyboard());
        }
        Student student = userContext.getStudent();
        student.setStudentGroup(studentGroupOptional.get());
        student.setUserRole(UserRole.USER);
        userContext.setStudent(student);
        userContext.setUserRegistrationState(nextState());
        studentDao.save(student);
        return new SendMessage(context.getChat().id(), GOOD_LUCK_FOR_USER_MESSAGE)
                .replyMarkup(mainKeyboard());
    }

    @Override
    public UserRegistrationState nextState() {
        return approvedState;
    }

    @Autowired
    public void setRegistration(UserContext userContext) {
        this.userContext = userContext;
    }
}
