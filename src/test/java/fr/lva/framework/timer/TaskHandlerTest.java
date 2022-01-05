package fr.lva.framework.timer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:task-application-context.xml")
public class TaskHandlerTest {

    @Autowired
    @Qualifier("loginTaskHandler")
    private TaskHandler loginTaskHandler;

    @Autowired
    @Qualifier("logoutTaskHandler")
    private TaskHandler logoutTaskHandler;

    @Test
    public void test() throws InterruptedException {
        loginTaskHandler.scheduleTask("azert@test.fr", new LoginTask("azert@test.fr"));
        logoutTaskHandler.scheduleTask("azert@test.fr", new LogoutTask("azert@test.fr"));
        logoutTaskHandler.scheduleTask("test2@test.fr", new LogoutTask("test2@test.fr"));
        loginTaskHandler.scheduleTask("test2@test.fr", new LoginTask("test2@test.fr"));
        //TimeUnit.SECONDS.sleep(2L);
        loginTaskHandler.scheduleTask("test3@test.fr", new LoginTask("test3@test.fr"));
        logoutTaskHandler.scheduleTask("test3@test.fr", new LogoutTask("test3@test.fr"));
        //TimeUnit.SECONDS.sleep(3L);
    }
}
