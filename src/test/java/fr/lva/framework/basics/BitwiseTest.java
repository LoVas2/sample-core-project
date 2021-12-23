package fr.lva.framework.basics;

import org.junit.Assert;
import org.junit.Test;

public class BitwiseTest {

    @Test
    public void shiftTest() {
        // Décalage de bits : 8 >> 1 = 4; 8 >> 2 = 2; 16 >> 1 = 8; etc.
        // Signed Right or Left shift operato
        Assert.assertEquals("Décalage de 1 bit vers la droite 16 >> 1", 8, 16 >> 1);
        Assert.assertEquals("Décalage de 3 bit vers la droite 16 >> 3", 2, 16 >> 3);
        Assert.assertEquals("Décalage de 2 bit vers la gauche 16 << 2", 64, 16 << 2);
        // Unsigned Right shift operator
        Assert.assertEquals("Unsigned Right shift operator", 2147483647, -1 >>> 1);
    }

    @Test
    public void incrementTest() {
        int i = 0;
        // first incremented and then used
        Assert.assertEquals("PreIncrement Test", 1, ++i);
        // first used and then increment
        Assert.assertEquals("PreIncrement Test", 1, i++);
        Assert.assertEquals("PreIncrement Test", 2, i);
    }
}
