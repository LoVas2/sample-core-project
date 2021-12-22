package fr.lva.framework.designpattern.factory;

public class ProductFactoryA2 extends ProductFactory {

    @Override
    public Product createProduct() {
        return new ProductA2();
    }

}
