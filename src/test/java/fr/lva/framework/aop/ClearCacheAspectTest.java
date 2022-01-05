package fr.lva.framework.aop;

import fr.lva.framework.annotation.ClearCache;
import fr.lva.framework.pojo.User;
import fr.lva.framework.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:sample-core-context-test.xml")
public class ClearCacheAspectTest {

    @Autowired
    private UserService userService;

    @Mock
    private ClearCacheAspect clearCacheAspect;

    @Test
    public void aspectHasBennCalledTest() {
        /*AspectJProxyFactory factory = new AspectJProxyFactory(userService);
        ClearCacheAspect clearCacheAspect = new ClearCacheAspect();
        factory.addAspect(clearCacheAspect);
        ((UserService)factory.getProxy()).sampleMethod(new User(1L, "name", "password"));*/
        userService.sampleMethod(new User(1L, "name", "password"));
    }

    @Test
    public void springElTest() {
        ClearCacheAspect aspect = new ClearCacheAspect();

        User u = new User(1L, "name", "password");
        JoinPoint jp = Mockito.mock(JoinPoint.class);
        Mockito.when(jp.getArgs()).thenReturn(Collections.singletonList(u).toArray());

        ClearCache clearCacheAnnotation = Mockito.mock(ClearCache.class);
        Mockito.when(clearCacheAnnotation.value()).thenReturn("#{args[0].id}");

        Assert.assertEquals("Wrong id extracted from SpEL", "1", aspect.clearCache(jp, clearCacheAnnotation));
    }

    @Test
    public void springElListTest() {
        ClearCacheAspect aspect = new ClearCacheAspect();

        User u1 = new User(1L, "name", "password");
        User u2 = new User(2L, "name", "password");
        JoinPoint jp = Mockito.mock(JoinPoint.class);
        Mockito.when(jp.getArgs()).thenReturn(Collections.singletonList(Arrays.asList(u1, u2).toArray()).toArray());

        ClearCache clearCacheAnnotation = Mockito.mock(ClearCache.class);
        Mockito.when(clearCacheAnnotation.value()).thenReturn("#{args[0].![id]}");

        Assert.assertEquals("Wrong ids extracted from SpEL", "1,2", aspect.clearCache(jp, clearCacheAnnotation));
    }

}
