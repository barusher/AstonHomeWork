package com.example.service;

import com.example.userservice.dao.UserDaoImpl;
import com.example.userservice.model.UserEntity;
import com.example.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDaoImpl userDao;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserCallsDaoSave() {
        UserEntity user = new UserEntity("Test", "test@mail.com", 30);

        userService.createUser(user);

        verify(userDao, times(1)).save(user);
    }

    @Test
    void testGetUserById() {
        UserEntity user = new UserEntity("Alice", "alice@mail.com", 28);
        when(userDao.getById(1L)).thenReturn(user);

        UserEntity result = userService.getUserById(1L);

        assertEquals("Alice", result.getName());
        assertEquals("alice@mail.com", result.getEmail());
        verify(userDao, times(1)).getById(1L);
    }

    @Test
    void testGetAllUsers() {
        UserEntity user1 = new UserEntity("Alice", "alice@mail.com", 28);
        UserEntity user2 = new UserEntity("Bob", "bob@mail.com", 35);
        when(userDao.getAll()).thenReturn(List.of(user1, user2));

        List<UserEntity> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userDao, times(1)).getAll();
    }

    @Test
    void testUpdateUser() {
        UserEntity user = new UserEntity("Alice", "alice@mail.com", 28);

        userService.updateUser(user);

        verify(userDao, times(1)).update(user);
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(1L);

        verify(userDao, times(1)).delete(1L);
    }
}
