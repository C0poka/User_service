import org.example.UserServiceImpl;
import org.example.dao.UserDao;
import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetUserByIdFound() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        when(userDao.findById(1L)).thenReturn(user);

        User result = userService.getUserById(1L);
        assertEquals("John", result.getName());
        verify(userDao).findById(1L);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userDao.findById(2L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(2L));
        verify(userDao).findById(2L);
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setName("New User");
        when(userDao.save(user)).thenReturn(10L);
        when(userDao.findById(10L)).thenReturn(user);

        User created = userService.createUser(user);
        assertEquals("New User", created.getName());
        verify(userDao).save(user);
        verify(userDao).findById(10L);
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Updated");
        userService.updateUser(user);
        verify(userDao).update(user);
    }

    @Test
    void testDeleteUserByIdFound() {
        User user = new User();
        user.setId(1L);
        when(userDao.findById(1L)).thenReturn(user);
        userService.deleteUserById(1L);
        verify(userDao).delete(user);
    }

    @Test
    void testDeleteUserByIdNotFound() {
        when(userDao.findById(2L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUserById(2L));
        verify(userDao).findById(2L);
    }
}
