package com.schedule.modal;

import com.schedule.utils.Constants;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
public class StudentGroup {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    private String number;

    public StudentGroup(String number){
        this.number = number;
    }
}
