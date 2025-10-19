package org.example.dao;

import org.example.model.User;
import org.example.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoImplIT {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    private SessionFactory sessionFactory;
    private UserDaoImpl userDao;

    @BeforeAll
    void setUpAll() {
        postgres.start();
        sessionFactory = HibernateUtil.buildSessionFactory(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        userDao = new UserDaoImpl(sessionFactory);
    }

    @AfterAll
    void tearDownAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        postgres.stop();
    }

    @Test
    void testSaveAndFind() {
        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");

        Long id = userDao.save(user);
        assertNotNull(id);

        User fetched = userDao.findById(id);
        assertEquals("Alice", fetched.getName());
        assertEquals("alice@example.com", fetched.getEmail());
    }

    @Test
    void testUpdate() {
        User user = new User();
        user.setName("Bob");
        user.setEmail("bob@example.com");
        Long id = userDao.save(user);

        user.setId(id);
        user.setName("Bobby");
        userDao.update(user);

        User updated = userDao.findById(id);
        assertEquals("Bobby", updated.getName());
    }

    @Test
    void testDelete() {
        User user = new User();
        user.setName("Charlie");
        user.setEmail("charlie@example.com");
        Long id = userDao.save(user);

        user.setId(id);
        userDao.delete(user);

        User deleted = userDao.findById(id);
        assertNull(deleted);
    }

    @Test
    void testFindAll() {
        User u1 = new User();
        u1.setName("U1");
        u1.setEmail("u1@example.com");
        userDao.save(u1);

        User u2 = new User();
        u2.setName("U2");
        u2.setEmail("u2@example.com");
        userDao.save(u2);

        List<User> users = userDao.findAll();
        assertTrue(users.size() >= 2);
    }
}
