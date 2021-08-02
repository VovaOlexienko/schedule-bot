package com.schedule.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(Long id);

    T save(T obj);

    void delete(T obj);

    List<T> getAll();
}