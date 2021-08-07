package com.schedule.modal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    Unregistered("ROLE_UNREGISTERED"), User("ROLE_USER"), Admin("ROLE_ADMIN");

    String role;
}
