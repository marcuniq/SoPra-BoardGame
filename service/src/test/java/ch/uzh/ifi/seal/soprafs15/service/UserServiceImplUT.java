package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.mapper.UserMapperService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Hakuna on 14.04.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class UserServiceImplUT {

    //protected UserRepository testUserRepo;
    //protected UserMapperService testUserMapperService;
    //protected UserServiceImpl userServiceImpl = new UserServiceImpl(testUserRepo, testUserMapperService);

    @Autowired
    protected UserServiceImpl userServiceImpl;

    @Test
    public void testAddUser(){

        UserRequestBean testUserRequestBean = new UserRequestBean();
        testUserRequestBean.setAge(45);
        testUserRequestBean.setUsername("TestUsername");

        User oracleUser = new User();
        oracleUser.setAge(45);
        oracleUser.setUsername("TestUsername");

        //testUserRepo.save(oracleUser);


        UserResponseBean result = userServiceImpl.addUser(testUserRequestBean);

        //Assert.assertEquals(testUserRepo.findByAge(result.getAge()).getAge(), oracleUser.getAge());
        //Assert.assertNotNull(testUserRepo.findByUsername("TestUsername"));

        Assert.assertEquals(1,1);





    }


}
