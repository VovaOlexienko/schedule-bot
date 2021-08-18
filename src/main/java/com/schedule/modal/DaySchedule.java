package com.schedule.modal;

import com.schedule.utils.Constants;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DaySchedule {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @ToString.Exclude
    @NotNull
    @Lob
    private byte[] schedule;

    @NotNull
    DayOfWeek dayOfWeek;

    @NotNull
    @ManyToOne
    @JoinColumn
    StudentGroup studentGroup;
}
