package com.schedule.modal;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum Role {

    Unregistered("ROLE_UNREGISTERED"), User("ROLE_USER"), Admin("ROLE_ADMIN");

    String role;
}
