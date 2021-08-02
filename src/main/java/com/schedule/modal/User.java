package com.schedule.modal;

import com.schedule.utils.Constants;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class User {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    private Long chatId;

    private Integer stateId;

    @ManyToOne
    @JoinColumn
    private StudentGroup studentGroup;

    public User(Long chatId) {
        this.chatId = chatId;
    }
}
