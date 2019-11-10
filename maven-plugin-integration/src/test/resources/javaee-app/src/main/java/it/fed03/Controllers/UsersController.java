package it.fed03.Controllers;

import it.fed03.DAOs.UserDao;
import it.fed03.Models.User;
import it.fed03.Services.NumberOfRetrievedUsers;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("users")
public class UsersController {

    @Inject
    private NumberOfRetrievedUsers service;
    @Inject
    private UserDao userDao;

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    @POST
    public String createNewUser(User user) {
        userDao.saveUser(user);
        return user.getName();
    }

    @GET
    @Path("count")
    public int getRetrievedUserCount() {
        return service.getNumber();
    }
}

