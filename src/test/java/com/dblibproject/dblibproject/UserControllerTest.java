package com.dblibproject.dblibproject;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserControllerTest {
    @Test
    public void getAll(){
        EmptyUserRepository emptyUserRepository = new  EmptyUserRepository();

        UserController userController = new UserController(emptyUserRepository);

      List<User> userList = userController.getAllUsers();

        assertEquals(0,userList.size());
    }


    @Test
    public void getAllFive(){

        FiveUserRepository fiveUserRepository = new FiveUserRepository();

        UserController userController = new UserController(fiveUserRepository);

        List<User> userList = userController.getAllUsers();

        assertEquals(5,userList.size());
    }

    @Test
    public void insertTest(){

        User user = new User("Ale","example@mail.com");


    }
}
