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

    public DaySchedule(byte[] schedule, DayOfWeek dayOfWeek, StudentGroup studentGroup) {
        this.schedule = schedule;
        this.dayOfWeek = dayOfWeek;
        this.studentGroup = studentGroup;
    }
}
