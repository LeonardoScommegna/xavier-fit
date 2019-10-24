package it.fed03.DAOs;

import it.fed03.Models.User;

import javax.transaction.Transactional;
import java.util.List;

public interface UserDao {
    @Transactional
    void saveUser(User u);

    User getById(int userId);

    List<User> getAll();
}
