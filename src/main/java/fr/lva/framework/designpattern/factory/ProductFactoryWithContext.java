package fr.lva.framework.designpattern.factory;

public class ProductFactoryWithContext {

    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;

    public Product getProduct(int productId) {
        switch (productId) {
            case TYPE_1:
                return new ProductA1();
            case TYPE_2:
                return new ProductA2();
            default:
                throw new IllegalArgumentException("Type de produit inconnu");
        }
    }
}
