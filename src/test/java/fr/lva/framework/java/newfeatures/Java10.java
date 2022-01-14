package fr.lva.framework.java.newfeatures;

import fr.lva.framework.pojo.User;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Use of var to declare variables in local scope. http://openjdk.java.net/projects/amber/guides/lvti-style-guide
 * Copy factory methods with List.of(), Set & Map.
 * Collectors to unmodifiable list.
 * Optional.orElseThrow().
 * New transfer characters method.
 * Parallel Full GC for G1 : Full GC multi thread define by  -XX:ParallelGCThreads
 */
public class Java10 {

    @Test
    public void varTest() {
        // Local Variable Type Inference
        var myStringVar = "";
        var myMapVar = new HashMap<Long, List<User>>();
        var myUserVar = new User();
        // New copy : copy list to an immuable one. Return itself if the original list is already immuable
        // Be careful : list is immuable not objects inside
        var list = List.copyOf(Arrays.asList(""));
        // Collectors to unmodifiable list
        list = list.stream().collect(Collectors.toUnmodifiableList());
        Assert.assertEquals("Class are not equals", String.class, myStringVar.getClass());
        Assert.assertTrue("Class are not equals", myMapVar instanceof Map);
        Assert.assertEquals("Class are not equals", User.class, myUserVar.getClass());
    }

    @Test(expected = NoSuchElementException.class)
    public void orElseThrowTest() {
        // Optional.orElseThrow()
        var list = List.of("");
        list.stream().filter(s -> s.contains("t")).findAny().orElseThrow();
    }

    @Test(expected = FileNotFoundException.class)
    public void transferMethodTest() throws IOException {
        // New transfer method
        Reader reader = new FileReader("from.txt");
        Writer writer = new FileWriter("to.txt");
        reader.transferTo(writer);
    }
}
