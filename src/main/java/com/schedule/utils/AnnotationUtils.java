package com.schedule.utils;

import lombok.experimental.UtilityClass;
import org.aspectj.lang.JoinPoint;

import java.util.Arrays;
import java.util.Optional;

@UtilityClass
public class AnnotationUtils {

    public static <T> Optional<T> getMethodParameter(JoinPoint joinPoint, Class<T> tClass) {
        return Arrays.stream(joinPoint.getArgs())
                .filter(tClass::isInstance)
                .map(tClass::cast)
                .findFirst();
    }
}
