package fr.lva.framework.java.newfeatures;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Oracle vs. Open JDK :
 * Java 10 was the last free Oracle JDK release that we could use commercially without a license.
 * Starting with Java 11, there's no free long-term support (LTS) from Oracle.
 *
 * New methods to the String class : isBlank, lines, strip, stripLeading, stripTrailing, and repeat.
 * New File Methods : readString and writeString.
 * The Not Predicate Method.
 * HttpClient from Java9 becomes a standard.
 * Nest Based Access Control.
 * Running Java Files : 'java HelloWorld.java'
 */
public class Java11 {

    @Test
    public void newStringMethodsTest() throws IOException, InterruptedException {
        // New methods to the String class : isBlank, lines, strip, stripLeading, stripTrailing, and repeat
        String multilineString = "New string method \n \n s are awesome \n . Lets explore.";
        String str = multilineString.lines()
                .filter(line -> !line.isBlank())
                .map(String::strip)
                .collect(Collectors.joining());
        Assert.assertEquals("String methods aren't ok", "New string methods are awesome. Lets explore.", str);
        Assert.assertEquals("String methods aren't ok", "New string methods are awesome. Lets explore.", str);
    }

    @Test
    public void newFileMethodsTest() throws IOException {
        // New File Methods : readString and writeString
        Path filePath = Files.writeString(Files.createTempFile("demo", ".txt"), "Sample text");
        String fileContent = Files.readString(filePath);
        Assert.assertEquals("Contents aren't equal", "Sample text", fileContent);
    }

    @Test
    public void predicateMethodTest() {
        // The Not Predicate Method
        List<String> sampleList = Arrays.asList("Java", "\n \n", "Kotlin", " ");
        List<String> withoutBlanks = sampleList.stream()
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.toList());
        Assert.assertEquals("Filter by predicate isn't ok", 2, withoutBlanks.size());
    }

    @Test(expected = ConnectException.class)
    public void httpClientTest() throws IOException, InterruptedException {
        // HttpClient
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080"))
                .build();
        HttpResponse httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }
}
