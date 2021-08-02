package com.schedule.dao;

import com.schedule.modal.User;

import java.util.Optional;

public interface UserDao extends Dao<User> {

    Optional<User> getUserByChatId(Long chatId);
}
