package fr.lva.framework.http;

import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpTest {

    @Test
    public void uriBuilderTest() {
        Map<String, String> uriMap = new HashMap<String, String>();
        uriMap.put("id_test", "value_test");
        URI uri =
                UriBuilder.fromPath("http://localhost:8080/api/mytest/{id_test}").buildFromMap(uriMap);
        Assert.assertEquals("URI not ok", "http://localhost:8080/api/mytest/value_test", uri.toString());
    }

}
