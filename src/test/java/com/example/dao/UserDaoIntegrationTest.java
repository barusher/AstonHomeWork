package com.example.dao;

import com.example.userservice.dao.UserDaoImpl;
import com.example.userservice.model.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("users_db")
                    .withUsername("postgres")
                    .withPassword("123")
                    .withReuse(false);

    private UserDaoImpl userDao;
    private SessionFactory sessionFactory;

    @BeforeAll
    void setup() {
        postgres.start();
        Configuration cfg = new Configuration()
                .setProperty("hibernate.connection.url", postgres.getJdbcUrl())
                .setProperty("hibernate.connection.username", postgres.getUsername())
                .setProperty("hibernate.connection.password", postgres.getPassword())
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect") // ← важно
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .addAnnotatedClass(UserEntity.class);

        sessionFactory = cfg.buildSessionFactory();
        userDao = new UserDaoImpl(sessionFactory);
    }

    @AfterAll
    void cleanup() {
        if (sessionFactory != null) sessionFactory.close();
        postgres.stop();
    }

    @BeforeEach
    void cleanDb() {
        try (Session session = sessionFactory.openSession()) {
            var tx = session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY CASCADE").executeUpdate();
            tx.commit();
        }
    }

    @Test
    void testSaveAndGetById() {
        UserEntity user = new UserEntity("Test", "test@mail.com", 30);
        userDao.save(user);

        UserEntity fromDb = userDao.getById(user.getId());
        assertNotNull(fromDb);
        assertEquals("Test", fromDb.getName());
        assertEquals("test@mail.com", fromDb.getEmail());
        assertEquals(30, fromDb.getAge());
        assertNotNull(fromDb.getCreatedAt());
    }

    @Test
    void testUpdateUser() {
        UserEntity user = new UserEntity("Original", "orig@mail.com", 25);
        userDao.save(user);

        user.setName("Updated");
        user.setEmail("updated@mail.com");
        userDao.update(user);

        UserEntity fromDb = userDao.getById(user.getId());
        assertEquals("Updated", fromDb.getName());
        assertEquals("updated@mail.com", fromDb.getEmail());
    }

    @Test
    void testDeleteUser() {
        UserEntity user = new UserEntity("ToDelete", "delete@mail.com", 40);
        userDao.save(user);
        Long id = user.getId();

        userDao.delete(id);

        UserEntity fromDb = userDao.getById(id);
        assertNull(fromDb);
    }

    @Test
    void testGetAllUsers() {
        UserEntity user1 = new UserEntity("Alice", "alice@mail.com", 28);
        UserEntity user2 = new UserEntity("Bob", "bob@mail.com", 35);

        userDao.save(user1);
        userDao.save(user2);

        List<UserEntity> users = userDao.getAll();
        assertEquals(2, users.size());
    }
}
