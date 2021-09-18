package com.lamborghini.service;

import com.lamborghini.model.Product;

import java.util.List;

public interface IProductService {
    List<Product> findAll();

    void save(Product product);

    Product findById(Long id);

    void update(Long id, Product product);

    void remove(Long id);
}
