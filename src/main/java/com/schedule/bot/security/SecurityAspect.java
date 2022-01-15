package com.schedule.bot.security;

import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.context.UserContext;
import com.schedule.modal.Student;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Order(2)
@Component
public class SecurityAspect {

    private UserContext userContext;

    @Pointcut("@annotation(RolesAllowed)")
    public void rolesAllowed() {
        //Validate user role for request
    }

    @SneakyThrows
    @Around("rolesAllowed()")
    public Object validateUserRole(ProceedingJoinPoint joinPoint) {
        Student student = userContext.getStudent();
        if (!getRoles(joinPoint).contains(student.getUserRole())) {
            return new SendMessage(student.getChatId(), "Access denied");
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }

    private List<UserRole> getRoles(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RolesAllowed allow = method.getAnnotation(RolesAllowed.class);
        return Arrays.asList(allow.roles());
    }

    @Autowired
    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
    }
}