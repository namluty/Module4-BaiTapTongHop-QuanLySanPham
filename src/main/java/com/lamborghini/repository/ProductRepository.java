package com.lamborghini.repository;

import com.lamborghini.model.Product;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class ProductRepository implements IProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> findAll() {
        //Viết List trước sau đó viết câu query sau:
//        String queryStr ="select c from Product as c";
//        List<Product> productList = entityManager.createQuery(queryStr, Product.class).getResultList();
//        return productList;

        //Viết query bằng TypedQuery
        TypedQuery<Product> query = entityManager.createQuery("select c from Product c", Product.class);
        return query.getResultList();
    }

    @Override
    public void save(Product product) {
        if (product != null) {
            entityManager.merge(product);
        } else {
            entityManager.persist(product);
        }
    }

    @Override
    public Product findById(Long id) {
        String queryStr = "select c from Product as c where c.id =:id";
        Product product = entityManager.createQuery(queryStr, Product.class).setParameter("id", id).getSingleResult();
        return product;
    }

    @Override
    public void update(Long id, Product product) {
    }

    @Override
    public void remove(Long id) {
        Product product = findById(id);
        if (product != null) {
            entityManager.remove(product);
        }
    }
}
