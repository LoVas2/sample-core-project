package fr.lva.framework.pojo;

import fr.lva.framework.annotation.Encrypted;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserTest {

    @Test
    public void encryptFieldTest() throws IllegalAccessException {
        boolean hasEncryptedField = false;
        User u = new User(1L, "firstName","superPassword");
        StringBuilder toString = new StringBuilder(User.class.getSimpleName()).append(" : ");
        for (Field field : User.class.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getDeclaredAnnotation(Encrypted.class) != null) {
                hasEncryptedField = true;
                if (field.getDeclaredAnnotation(Encrypted.class).print()) {
                    toString.append(field.getName()).append("=#EncryptKey#").append(field.get(u));
                } else {
                    toString.append(field.getName()).append("=#EncryptKey#").append("*****");
                }
            } else {
                toString.append(field.getName()).append("=").append(field.get(u));
            }
            toString.append(", ");
            field.setAccessible(false);
        }
        toString.delete(toString.lastIndexOf(","), toString.length());
        assertTrue(hasEncryptedField);
        assertEquals("Strings not equals", "User : id=1, name=#EncryptKey#firstName, password=#EncryptKey#*****", toString.toString());
    }

}
