package com.schedule.modal;

import com.schedule.utils.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Teacher {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    private String fullname;

    private String email;

    private String phone;

    private String tgNickname;

    public Teacher(String fullname, String email, String phone, String tgNickname){
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.tgNickname = tgNickname;
    }
}
