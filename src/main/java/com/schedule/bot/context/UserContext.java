package com.schedule.bot.context;

import com.github.kshashov.telegram.TelegramScope;
import com.schedule.bot.registration.states.UserRegistrationState;
import com.schedule.modal.Student;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = TelegramScope.SCOPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Setter
public class UserContext {
    private Student student;
    private UserRegistrationState userRegistrationState;
}
