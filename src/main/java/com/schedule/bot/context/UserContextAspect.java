package com.schedule.bot.context;

import com.pengrad.telegrambot.model.Chat;
import com.schedule.bot.registration.states.EndRegistrationState;
import com.schedule.bot.registration.states.UserRegistrationState;
import com.schedule.dao.StudentDao;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.schedule.utils.AnnotationUtils.getMethodParameter;

@Aspect
@Order(1)
@Component
@RequiredArgsConstructor
public class UserContextAspect {

    private final StudentDao studentDao;
    private UserContext userContext;

    @Pointcut("@annotation(com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest)")
    public void allMessageRequests() {
        //Pointcut for all Message requests to bots
    }

    @Pointcut("@annotation(com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest)")
    public void allCallbackQueryRequests() {
        //Pointcut for all CallbackQuery requests to bots
    }

    @Before("allMessageRequests() || allCallbackQueryRequests()")
    public void createSecurityContext(JoinPoint joinPoint) {
        if (Objects.isNull(userContext.getStudent())) {
            getMethodParameter(joinPoint, Chat.class)
                    .flatMap(chat -> studentDao.getByChatId(chat.id()))
                    .ifPresent(s -> userContext.setStudent(s));
        }
        UserRegistrationState state = userContext.getUserRegistrationState();
        userContext.setUserRegistrationState(Objects.nonNull(state) ? state : new EndRegistrationState());
    }

    @Autowired
    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
    }
}
