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
@ToString
public class Teacher {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @NotBlank
    private String fullname;

    private String universityEmail;

    private String email;

    private String tgNickname;

    private String photoUrl;

    private String phone;

    public Teacher(String fullname, String universityEmail, String email, String tgNickname, String photoUrl, String phone){
        this.fullname = fullname;
        this.universityEmail = universityEmail;
        this.email = email;
        this.tgNickname = tgNickname;
        this.photoUrl = photoUrl;
        this.phone = phone;
    }
}
