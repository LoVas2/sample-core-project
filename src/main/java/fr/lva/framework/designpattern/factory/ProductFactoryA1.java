package fr.lva.framework.designpattern.factory;

public class ProductFactoryA1 extends ProductFactory {

    @Override
    public Product createProduct() {
        return new ProductA1();
    }

}
