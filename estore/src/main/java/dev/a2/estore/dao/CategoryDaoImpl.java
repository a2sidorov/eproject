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

import dev.a2.estore.model.Category;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * This class provides implementation for CategoryDao interface.
 *
 * @author Andrei Sidorov
 */
@Repository
public class CategoryDaoImpl implements CategoryDao {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(CategoryDao.class);

    /**
     * Injects bean SessionFactory.
     */
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(final Category category) {
        sessionFactory.getCurrentSession().save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        @SuppressWarnings("unchecked")
        TypedQuery<Category> query = sessionFactory.getCurrentSession()
                .createQuery("FROM Category pc WHERE pc.type = 'category'");
        logger.info("Fetched all " + query.getResultList());
        return query.getResultList();
    }

    @Override
    public Category findById(final Long categoryId) {
        Category category = sessionFactory.getCurrentSession().get(Category.class, categoryId);
        logger.info("Fetched by id '" + categoryId + "' " + category);
        return category;
    }

    @Override
    public List<Category> getTopLevelCategories() {
        @SuppressWarnings("unchecked")
        TypedQuery<Category> query = sessionFactory.getCurrentSession()
                .createQuery("FROM Category pc WHERE pc.parentCategory IS NULL");
        logger.info("Fetched top level categories " + query.getResultList());
        return query.getResultList();
    }

    @Override
    public void delete(final Category category) {
        sessionFactory.getCurrentSession().delete(category);
    }

    @Override
    public void update(final Category category) {
        sessionFactory.getCurrentSession().update(category);
    }

}
