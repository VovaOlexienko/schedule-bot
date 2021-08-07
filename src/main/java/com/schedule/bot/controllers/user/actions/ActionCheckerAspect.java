package com.schedule.bot.controllers.user.actions;

import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.schedule.dao.StudentDao;
import com.schedule.modal.Student;

import com.schedule.utils.Utils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
public class ActionCheckerAspect {

    @Autowired
    private StudentDao studentDao;

    @Pointcut("@annotation(com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest)")
    public void messageRequest() {}

    @Before("messageRequest()")
    public void checkNextAction(JoinPoint joinPoint) throws Exception {
        if (!Arrays.asList(getRequests(joinPoint)).toString().contains("*")) {
            Optional<Student> studentOptional = studentDao.getStudentByChatId(Utils.getChat(joinPoint).id());
            if(studentOptional.isPresent()) {
                Student student = studentOptional.get();
                student.setNextAction(null);
                studentDao.save(student);
            }
        }
    }

    private String[] getRequests(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        MessageRequest messageRequest = method.getAnnotation(MessageRequest.class);
        return messageRequest.value();
    }
}