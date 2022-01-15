package com.schedule.bot.registration.states;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.schedule.bot.context.UserContext;
import com.schedule.bot.registration.RegistrationRequest;
import com.schedule.bot.security.UserRole;
import com.schedule.dao.StudentDao;
import com.schedule.modal.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.schedule.utils.KeyboardUtils.studentGroupsKeyboard;
import static com.schedule.utils.TextUtils.ENTER_GROUP_MESSAGE;

@Component
@RequiredArgsConstructor
public class StartRegistrationState implements UserRegistrationState {

    private final StudentDao studentDao;
    private final EnterGroupRegistrationState enterGroupState;
    private UserContext userContext;

    @Override
    public BaseRequest<SendMessage, SendResponse> enter(RegistrationRequest context) {
        studentDao.getByChatId(context.getChat().id()).ifPresent(studentDao::delete);
        Student student = Student.builder()
                .chatId(context.getChat().id())
                .userRole(UserRole.UNREGISTERED)
                .build();
        userContext.setStudent(student);
        userContext.setUserRegistrationState(nextState());
        return new SendMessage(context.getChat().id(), ENTER_GROUP_MESSAGE).replyMarkup(studentGroupsKeyboard());
    }

    @Override
    public UserRegistrationState nextState() {
        return enterGroupState;
    }

    @Autowired
    public void setRegistration(UserContext userContext) {
        this.userContext = userContext;
    }
}
