package ru.kafpin.rkpppolyclinic.dao;

import java.util.List;

public interface Dao<T, K> {
    T findById(K id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(K id);
}