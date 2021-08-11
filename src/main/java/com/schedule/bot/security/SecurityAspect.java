package com.schedule.bot.security;

import com.schedule.dao.StudentDao;
import com.schedule.modal.Role;
import com.schedule.modal.Student;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static com.schedule.utils.AnnotationUtils.*;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class SecurityAspect {

    @Autowired
    private StudentDao studentDao;

    @Pointcut("@annotation(RolesAllowed)")
    public void rolesAllowed() {}

    @Before("rolesAllowed()")
    public void validateUserRole(JoinPoint joinPoint) throws Exception {
        Role[] roles = getRoles(joinPoint);
        Student student = studentDao.getStudentByChatId(getChat(joinPoint).id())
                .orElseThrow(() -> new Exception("Unregistered user"));
        if (!Arrays.asList(roles).contains(student.getRole())) throw new Exception("Access denied");
    }

    private Role[] getRoles(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RolesAllowed allow = method.getAnnotation(RolesAllowed.class);
        return allow.roles();
    }
}