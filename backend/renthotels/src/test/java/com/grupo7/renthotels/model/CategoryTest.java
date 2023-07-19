package com.grupo7.renthotels.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CategoryTest {

    @Test
    public void shouldAddProductsInCategory(){
        Category category = new Category();

        Product product1 = new Product();
        Product product2 = new Product();

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        category.setProducts(products);

        Assert.assertNotNull(category.getProducts());
        Assert.assertEquals(category.getProducts().size(), products.size());
    }

    @Test
    public void shouldRemoveProductsFromCategory(){
        Category category = new Category();

        Product product1 = new Product();
        Product product2 = new Product();

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        category.setProducts(products);

        category.getProducts().remove(1);

        Assert.assertNotNull(category.getProducts());
        Assert.assertEquals(category.getProducts().size(), products.size());
    }
}
