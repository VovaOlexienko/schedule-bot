package com.schedule.modal;

import com.schedule.utils.Constants;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentGroup {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotBlank
    private String number;

    @Override
    public String toString() {
        return number.contains(",") || number.contains(" ") ? "\"" + number + "\"" : number;
    }
}
