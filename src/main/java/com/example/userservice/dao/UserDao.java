package com.example.userservice.dao;

import com.example.userservice.model.UserEntity;

import java.util.List;

/**
 * Data Access Object (DAO) интерфейс для управления операциями сохранения UserEntity.
 * Предоставляет CRUD (Create, Read, Update, Delete) операции для хранения данных пользователей.
 */
public interface UserDao {

    /**
     * Сохраняет новую сущность пользователя в постоянное хранилище.
     * Если пользователь уже существует (на основе ID), поведение зависит от реализации.
     *
     * @param userEntity сущность пользователя для сохранения, не должна быть {@code null}
     */
    void save(UserEntity userEntity);

    /**
     * Получает сущность пользователя по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор пользователя для получения, не должен быть {@code null}
     * @return сущность пользователя с указанным ID, или {@code null} если пользователь не найден
     */
    UserEntity getById(Long id);

    /**
     * Получает список всех пользователей из хранилища.
     *
     * @return список всех сущностей пользователей, может быть пустым, но не {@code null}
     */
    List<UserEntity> getAll();

    /**
     * Обновляет существующую сущность пользователя в хранилище.
     * Сущность должна уже существовать в хранилище.
     *
     * @param userEntity сущность пользователя для обновления, не должна быть {@code null}
     * @throws IllegalArgumentException если userEntity равен {@code null}
     */
    void update(UserEntity userEntity);

    /**
     * Удаляет сущность пользователя по её уникальному идентификатору.
     *
     * @param id уникальный идентификатор пользователя для удаления, не должен быть {@code null}
     * @throws IllegalArgumentException если id равен {@code null}
     */
    void delete(Long id);
}