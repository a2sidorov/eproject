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
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.a2.estore.dao;

import dev.a2.estore.model.Attribute;
import dev.a2.estore.model.ProductAttribute;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * This class provides implementation for AttributeDao interface.
 *
 * @author Andrei Sidorov
 */
@Repository
public class AttributeDaoImpl implements AttributeDao {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(AttributeDao.class);

    /**
     * Injects bean SessionFactory.
     */
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(final Attribute attribute) {
        sessionFactory.getCurrentSession().save(attribute);
    }

    @Override
    public Attribute findByName(final String attributeName) {
        @SuppressWarnings("unchecked")
        TypedQuery<Attribute> query = sessionFactory.getCurrentSession()
                .createQuery("FROM Attribute a WHERE a.name = :name");
        query.setParameter("name", attributeName);
        try {
            logger.info("Fetched by name '" + attributeName + "' " + query.getSingleResult());
            return query.getSingleResult();
        } catch (NoResultException e) {
            logger.info("No attribute with name '" + attributeName + "' has been found.");
            return null;
        }
    }

    @Override
    public void save(final ProductAttribute productAttribute) {
        sessionFactory.getCurrentSession().save(productAttribute);
    }

    @Override
    public List<ProductAttribute> getAllProductAttributes(final Long productId) {
        @SuppressWarnings("unchecked")
        TypedQuery<ProductAttribute> query = sessionFactory.getCurrentSession()
                .createQuery("FROM ProductAttribute pa " +
                                        "JOIN FETCH pa.pk.attribute " +
                                        "WHERE pa.pk.product.id = :id");
        query.setParameter("id", productId);
        return query.getResultList();
    }

    @Override
    public void update(final ProductAttribute productAttribute) {
        sessionFactory.getCurrentSession().update(productAttribute);

    }

    @Override
    public Attribute findById(final Long attributeId) {
        Attribute attribute =  sessionFactory.getCurrentSession().get(Attribute.class, attributeId);
        logger.info("Fetched by attribute id '" + attributeId + "' " + attribute);
        return attribute;
    }

    @Override
    public List<ProductAttribute> getAttributeValues(final Long attributeId, final Long categoryId) {
        @SuppressWarnings("unchecked")
        TypedQuery<ProductAttribute> query = sessionFactory
                .getCurrentSession()
                .createQuery("FROM ProductAttribute pa " +
                                        "JOIN FETCH pa.pk.attribute " +
                                        "WHERE pa.pk.attribute.id = :attributeId " +
                                        "AND pa.pk.product.category.id = :categoryId");
        query.setParameter("attributeId", attributeId);
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }

    @Override
    public List<ProductAttribute> getAttributeValues(final Long attributeId, final List<Long> categoriesIds) {
        @SuppressWarnings("unchecked")
        TypedQuery<ProductAttribute> query = sessionFactory
                .getCurrentSession()
                .createQuery("FROM ProductAttribute pa " +
                                        "JOIN FETCH pa.pk.attribute " +
                                        "WHERE pa.pk.attribute.id = :attributeId " +
                                        "AND pa.pk.product.category.id IN (:categoriesIds)");
        query.setParameter("attributeId", attributeId);
        query.setParameter("categoriesIds", categoriesIds);
        return query.getResultList();
    }

}
