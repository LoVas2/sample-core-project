package fr.lva.framework.designpattern.factory;

public abstract class ProductFactory {

    public Product getProduct() {
        return createProduct();
    }

    public abstract Product createProduct();

}
