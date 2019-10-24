package it.fed03.DAOs;

import it.fed03.Models.User;
import it.fed03.NumberOfRetrievedUsers;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

public class UserSqlDao implements UserDao {
    private final NumberOfRetrievedUsers numberOfRetrievedUsers;

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    public UserSqlDao(NumberOfRetrievedUsers numberOfRetrievedUsers) {
        this.numberOfRetrievedUsers = numberOfRetrievedUsers;
    }

    @Override
    @Transactional
    public void saveUser(User u) {
        entityManager.persist(u);
    }

    @Override
    public User getById(int userId) {
        var user = entityManager.find(User.class,userId);
        if (user != null)
            numberOfRetrievedUsers.addRetrievedCount(1);

        return user;
    }

    @Override
    public List<User> getAll() {
        var users =  entityManager.createQuery("SELECT u from User u",User.class).getResultList();
        numberOfRetrievedUsers.addRetrievedCount(users.size());
        return users;
    }
}
