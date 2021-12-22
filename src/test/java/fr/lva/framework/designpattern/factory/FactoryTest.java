package fr.lva.framework.designpattern.factory;

import org.junit.Assert;
import org.junit.Test;

public class FactoryTest {

    @Test
    public void factoryTest() {
        ProductFactory produitFactory1 = new ProductFactoryA1();
        ProductFactory produitFactory2 = new ProductFactoryA2();

        Product product;

        // Utilisation de la premiere fabrique
        product = produitFactory1.getProduct();
        Assert.assertEquals("Product name is not ok", "Product A1", product.getProductName());

        // Utilisation de la seconde fabrique
        product = produitFactory2.getProduct();
        Assert.assertEquals("Product name is not ok", "Product A2", product.getProductName());
    }

    @Test
    public void factoryWithContextTest() {
        Product product;
        // Factory with context
        ProductFactoryWithContext productFactoryWithContext = new ProductFactoryWithContext();
        product = productFactoryWithContext.getProduct(ProductFactoryWithContext.TYPE_1);
        Assert.assertEquals("Product name is not ok", "Product A1", product.getProductName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void factoryWithContextErrorTest() {
        // Factory with context
        ProductFactoryWithContext productFactoryWithContext = new ProductFactoryWithContext();
        productFactoryWithContext.getProduct(3);
    }
}
