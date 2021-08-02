package com.schedule.dao.impl;

import com.schedule.dao.UserDao;
import com.schedule.modal.User;
import com.schedule.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDao {

    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<User> get(Long id) {
        return Optional.of(userRepository.getById(id));
    }

    @Override
    public User save(User obj) {
        return userRepository.save(obj);
    }

    @Override
    public void delete(User obj) {
        userRepository.delete(obj);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByChatId(Long chatId) {
        return Optional.ofNullable(userRepository.findByChatId(chatId));
    }
}

