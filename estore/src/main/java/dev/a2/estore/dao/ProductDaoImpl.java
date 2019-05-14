/*
 * MIT License
 *
 * Copyright (c) 2019 Andrei Sidorov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package dev.a2.estore.dao;

import java.util.List;

import dev.a2.estore.exception.ProductReserveException;
import dev.a2.estore.model.Product;
import dev.a2.estore.model.ProductAttribute;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

/**
 * This class provides implementation for ProductDao interface.
 *
 * @author Andrei Sidorov
 */
@Repository
public class ProductDaoImpl implements ProductDao {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(ProductDao.class);

    /**
     * Injects bean SessionFactory.
     */
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(final Product product) {
        sessionFactory.getCurrentSession().save(product);
    }

    @Override
    public Product findById(final Long productId) {
        Product product =  sessionFactory.getCurrentSession().get(Product.class, productId);
        logger.info("Fetched by id '" + productId + "' " + product);
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        @SuppressWarnings("unchecked")
        TypedQuery<Product> query = sessionFactory
                .getCurrentSession()
                .createQuery("FROM Product " +
                                        "ORDER BY quantityInStock DESC");
        logger.info("Fetched all " + query.getResultList());
        return query.getResultList();
    }

    @Override
    public void update(final Product product) {
        sessionFactory.getCurrentSession().update(product);
    }

    @Override
    public List<Product> getProductsByCategoryId(final Long categoryId) {
        @SuppressWarnings("unchecked")
        TypedQuery<Product> query = sessionFactory
                .getCurrentSession()
                .createQuery("FROM Product p " +
                                        "WHERE p.category.id =:id " +
                                        "ORDER BY quantityInStock DESC");
        query.setParameter("id", categoryId);
        logger.info("Fetched by category id " + categoryId + "' " + query.getResultList());
        return query.getResultList();
    }

    @Override
    public List<Product> getProductsByCategoriesIds(final List<Long> categoriesIds) {
        @SuppressWarnings("unchecked")
        TypedQuery<Product> query = sessionFactory
                .getCurrentSession()
                .createQuery("FROM Product p " +
                                        "WHERE p.category.id IN (:categoriesIds) " +
                                        "ORDER BY quantityInStock DESC");
        query.setParameter("categoriesIds", categoriesIds);
        logger.info("Fetched by categories ids '" + categoriesIds + "' " + query.getResultList());
        return query.getResultList();
    }

    @Override
    public List<Product> findByPartialName(String input) {
        @SuppressWarnings("unchecked")
        TypedQuery<Product> query = sessionFactory
                .getCurrentSession()
                .createQuery("FROM Product p " +
                        "WHERE p.name LIKE concat('%', ?1, '%') " +
                        "ORDER BY quantityInStock DESC");
        query.setParameter(1, input);
        logger.info("Fetched by partial name '" + input + " result: " + query.getResultList());
        return query.getResultList();
    }

    @Override
    public List<Product> findByPartialNameAndCategoriesIds(String input, List<Long> categoriesIds) {
        @SuppressWarnings("unchecked")
        TypedQuery<Product> query = sessionFactory
                .getCurrentSession()
                .createQuery("FROM Product p " +
                        "WHERE p.name LIKE concat('%', ?1, '%') " +
                        "AND p.category.id IN (:categoriesIds) " +
                        "ORDER BY quantityInStock DESC");
        query.setParameter(1, input);
        query.setParameter("categoriesIds", categoriesIds);
        logger.info("Fetched by partial name '" + input + "' and category id '" + categoriesIds + "' " +
                query.getResultList());
        return query.getResultList();
    }

    @Override
    public void deleteProductAttribute(final Long productId, final Long attributeId) {
        @SuppressWarnings("unchecked")
        TypedQuery<ProductAttribute> query = sessionFactory.getCurrentSession()
                .createQuery("FROM ProductAttribute pa " +
                                        "JOIN FETCH pa.pk.attribute " +
                                        "WHERE pa.pk.product.id = :productId " +
                                        "AND pa.pk.attribute.id = :attributeId");
        query.setParameter("productId", productId);
        query.setParameter("attributeId", attributeId);

        ProductAttribute productAttribute =  query.getSingleResult();
        sessionFactory.getCurrentSession().delete(productAttribute);

    }

    @Transactional
    @Override
    public void reserveProductWithLocking(final Long productId, final Integer quantity) {
        final Session session = sessionFactory.getCurrentSession();
        final Product product = session.get(Product.class, productId, LockMode.PESSIMISTIC_WRITE);
        Integer availableQuantity = product.getQuantityInStock();

        if (quantity <= availableQuantity) {
            product.setQuantityInStock(availableQuantity - quantity);
            product.setQuantityReserved(product.getQuantityReserved() + quantity);
            //session.saveOrUpdate(product);
        } else {
            throw new ProductReserveException("Requested amount is not available.");
        }
    }

    @Transactional
    @Override
    public void unreserveProductWithLocking(final Long productId, final Integer quantity) {
        final Session session = sessionFactory.getCurrentSession();
        final Product product = session.get(Product.class, productId, LockMode.PESSIMISTIC_WRITE);
        Integer availableQuantity = product.getQuantityInStock();
        Integer reservedQuantity = product.getQuantityReserved();
        product.setQuantityInStock(availableQuantity + quantity);
        product.setQuantityReserved(reservedQuantity - quantity);
        //session.saveOrUpdate(product);
    }

    @Override
    public List<Product> findProductsByMeasureUnitsId(final Long measureUnitsId) {
        @SuppressWarnings("unchecked")
        TypedQuery<Product> query = sessionFactory
                .getCurrentSession()
                .createQuery("FROM Product p " +
                                        "WHERE p.measureUnits.id =:id");
        query.setParameter("id", measureUnitsId);
        logger.info("Fetched by measure units id '" + measureUnitsId + "' " + query.getResultList());
        return query.getResultList();
    }



}

