package fr.lva.framework.timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TimerTask;

public class LogoutTask extends TimerTask {

    private static final Logger LOG = LogManager.getLogger();

    private String login;

    public LogoutTask(String login) {
        this.login = login;
    }

    @Override
    public void run() {
        LOG.info("Launch {} for {}", LoginTask.class.getSimpleName(), this.login);
    }

}
