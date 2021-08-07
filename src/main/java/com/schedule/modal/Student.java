package com.schedule.modal;

import com.schedule.bot.controllers.user.actions.Action;
import com.schedule.bot.controllers.user.registration.states.RegistrationState;
import com.schedule.utils.Constants;
import lombok.*;

import javax.persistence.*;

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

    private RegistrationState registrationState;

    @Enumerated(EnumType.STRING)
    private Action nextAction;

    @ManyToOne
    @JoinColumn
    private StudentGroup studentGroup;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Student(Long chatId) {
        this.chatId = chatId;
    }
}
