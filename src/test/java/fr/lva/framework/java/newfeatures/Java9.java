package fr.lva.framework.java.newfeatures;

import fr.lva.framework.pojo.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Effectively finals variables in try-with-ressources.
 * Diamond operator in IC.
 * Private methods in interface.
 * New instanciation of  List, Set & Map with List.of()... Returns an unmodifiable list.
 * VarHandle API to access variables in object.
 * JVM improvements.
 */
public class Java9 {

    private static final Logger LOG = LogManager.getLogger();

    @Test
    public void mainTest() {
        // Effectively finals variables in try-with-ressources
        try {
            BufferedReader buf = new BufferedReader(new FileReader("file.txt"));
            try (buf) {
                LOG.info(buf.readLine());
            }
        } catch (IOException ioe) {
            LOG.error("Error reading file");
        }
        // Diamond operator in IC
        Callable<String> callable = new Callable<>() {
            @Override
            public String call() throws Exception {
                return "";
            }
        };
    }

    @Test(expected = UnsupportedOperationException.class)
    public void listTest() {
        // New instanciation of  List, Set & Map with List.of()... Returns an unmodifiable list
        List<String> strings = List.of("foo");
        strings.add("bar"); // -> produce an error
    }

    @Test
    public void varHandleTest() {
        // VarHandle
        try {
            User user = new User(1L, "Name", "password");
            VarHandle userNameVarHandle = MethodHandles.lookup().in(User.class).findVarHandle(User.class, "name", String.class);
            LOG.info("User's name = {}", userNameVarHandle.get(user));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOG.error("Error accessing field", e);
        }
    }

    /**
     * Private methods in interface
     */
    public interface Int {

        private String printStr(String str) {
            return "Method private in interface come from Java9";
        }

    }
}
