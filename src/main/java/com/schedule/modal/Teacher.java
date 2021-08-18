package com.schedule.modal;

import com.schedule.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import static com.schedule.utils.StringUtils.isBlank;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    public String getDescription() {
        String s = "";
        if (!isBlank(tgNickname)) s += "Telegram: " + tgNickname + "\n";
        if (!isBlank(universityEmail)) s += universityEmail + "\n";
        if (!isBlank(email)) s += email;
        return s;
    }

    @Override
    public String toString() {
        return fullname + "\n" + getDescription();
    }
}
