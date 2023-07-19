package com.grupo7.renthotels.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CityTest {

    @Test
    public void shouldAddProductsInCity(){
        City city = new City();

        Product product1 = new Product();
        Product product2 = new Product();

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        city.setProducts(products);

        Assert.assertNotNull(city.getProducts());
        Assert.assertEquals(city.getProducts().size(), products.size());
    }

    @Test
    public void shouldRemoveProductsFromCity(){
        City city = new City();

        Product product1 = new Product();
        Product product2 = new Product();

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        city.setProducts(products);

        city.getProducts().remove(1);

        Assert.assertNotNull(city.getProducts());
        Assert.assertEquals(city.getProducts().size(), products.size());
    }
}
