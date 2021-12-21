package fr.lva.framework.timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class TaskHandler<T extends TimerTask> {

    private static final Logger LOG = LogManager.getLogger();

    private int                          timeout;
    private final Timer                  timer;
    private final Map<String, TimerTask> timerByLogin;

    public TaskHandler() {
        LOG.info("New Instance by default constructor");
        this.timer = new Timer(true);
        this.timerByLogin = new ConcurrentHashMap<>();
    }

    public TaskHandler(int timeout) {
        LOG.info("New Instance with parameter");
        this.timeout = timeout;
        this.timer = new Timer(true);
        this.timerByLogin = new ConcurrentHashMap<>();
    }

    public void scheduleTask(String login, TimerTask task) {
        this.timerByLogin.put(login, task);
        this.timer.schedule(task, timeout);
    }

    public void cancelTask(String login) {
        TimerTask task = this.timerByLogin.remove(login);
        if (task != null) {
            task.cancel();
        }
    }

    /* GETTERS & SETTERS */

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
