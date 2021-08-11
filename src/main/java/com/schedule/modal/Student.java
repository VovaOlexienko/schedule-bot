package com.schedule.modal;

import com.schedule.bot.user.registration.states.RegistrationState;
import com.schedule.utils.Constants;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Locale;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Student {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    private Long chatId;

    @NotNull
    private RegistrationState registrationState;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    private Locale locale;

    @NotNull
    @ManyToOne
    @JoinColumn
    private StudentGroup studentGroup;

    public Student(Long chatId) {
        this.chatId = chatId;
    }
}
