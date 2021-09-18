package com.lamborghini.service;

import com.lamborghini.model.Product;
import com.lamborghini.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductService implements IProductService {
    @Autowired
    private IProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void update(Long id, Product product) {
        productRepository.update(id, product);
    }

    @Override
    public void remove(Long id) {
        productRepository.remove(id);
    }
}
