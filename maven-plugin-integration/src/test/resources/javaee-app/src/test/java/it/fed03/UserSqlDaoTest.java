package it.fed03;

import it.fed03.DAOs.UserSqlDao;
import it.fed03.Models.User;
import it.fed03.Services.NumberOfRetrievedUsers;
import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

@EnableWeld
class UserSqlDaoTest {
    private static final EntityManager entityManager = Persistence
            .createEntityManagerFactory("Default")
            .createEntityManager();
    @WeldSetup
    private WeldInitiator weld = WeldInitiator.from(UserSqlDao.class, NumberOfRetrievedUsers.class, User.class)
            .setPersistenceContextFactory(injectionPoint -> entityManager)
            .activate(SessionScoped.class).build();

    @Inject
    private UserSqlDao sut;

    @Test
    void name() {
        var u1 = new User();
        u1.setName("John");
        u1.setAge(23);

        var trns = entityManager.getTransaction();
        trns.begin();
        sut.saveUser(u1);
        trns.commit();

        var result = sut.getAll();
        Assertions.assertEquals(1,result.size());
    }
}
