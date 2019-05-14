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

import dev.a2.estore.model.MeasureUnits;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * This class provides implementation for MeasureUnits interface.
 *
 * @author Andrei Sidorov
 */
@Repository
public class MeasureUnitsDaoImpl implements MeasureUnitsDao {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(MeasureUnitsDao.class);

    /**
     * Injeacts bean SessionFactory.
     */
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(final MeasureUnits measureUnits) {
        sessionFactory.getCurrentSession().save(measureUnits);
    }

    @Override
    public List<MeasureUnits> getAllMeasureUnits() {
        @SuppressWarnings("unchecked")
        TypedQuery<MeasureUnits> query = sessionFactory.getCurrentSession().createQuery("FROM MeasureUnits");
        logger.info("Fetched all " + query.getResultList());
        return query.getResultList();
    }

    @Override
    public MeasureUnits findById(final Long measureUnitsId) {
        MeasureUnits measureUnits = sessionFactory.getCurrentSession().get(MeasureUnits.class, measureUnitsId);
        logger.info("Fetched by id '" + measureUnitsId + "' " + measureUnits);
        return measureUnits;
    }

    @Override
    public void delete(final MeasureUnits measureUnits) {
        sessionFactory.getCurrentSession().delete(measureUnits);
    }

    @Override
    public void update(final MeasureUnits measureUnits) {
        sessionFactory.getCurrentSession().update(measureUnits);
    }

}

