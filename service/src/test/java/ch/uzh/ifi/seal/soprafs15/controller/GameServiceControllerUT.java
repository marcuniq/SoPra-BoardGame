package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameResponseBean;
import org.junit.Assert;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.Clock;

/**
 * Created by Hakuna on 14.04.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class GameServiceControllerUT {

    @Test
    public void testAddGame() {

        GameServiceController gameServiceController = new GameServiceController();
        GameRequestBean gameRequestBean = new GameRequestBean();
        gameRequestBean.setName("TestName1");

        GameResponseBean gameResponseBean = new GameResponseBean();
        gameResponseBean.setName("TestName1");

        GameResponseBean result = gameServiceController.addGame(gameRequestBean);

        Assert.assertEquals(null, gameResponseBean.getName());
        //Assert.assertNotNull(result);
    }
}
