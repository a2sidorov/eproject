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

package dev.a2.estore.service;

import dev.a2.estore.dao.MeasureUnitsDao;
import dev.a2.estore.dao.ProductDao;
import dev.a2.estore.exception.MeasureUnitsDeleteException;
import dev.a2.estore.model.MeasureUnits;
import dev.a2.estore.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This class provides implementation for MeasureUnitsService interface.
 *
 * @author Andrei Sidorov
 */
@Service
@Transactional
public class MeasureUnitsServiceImpl implements MeasureUnitsService {

    /**
     * Injects MeasureUnitsDao.
     */
    @Autowired
    private MeasureUnitsDao measureUnitsDao;

    /**
     * Injects ProductDao.
     */
    @Autowired
    private ProductDao productDao;

    @Override
    public void save(final String measureUnitsName) {
        MeasureUnits newMeasureUnits = new MeasureUnits();
        newMeasureUnits.setName(measureUnitsName);
        measureUnitsDao.save(newMeasureUnits);
    }

    @Override
    public List<MeasureUnits> getAllMeasureUnits() {
        return measureUnitsDao.getAllMeasureUnits();
    }

    @Override
    public MeasureUnits findById(final Long id) {
        return measureUnitsDao.findById(id);
    }

    @Override
    public void delete(final Long measureUnitsId) {
        MeasureUnits measureUnits = measureUnitsDao.findById(measureUnitsId);
        List<Product> products = productDao.findProductsByMeasureUnitsId(measureUnitsId);

        if (products.size() > 0) {
            throw new MeasureUnitsDeleteException("Measure units can not be deleted " +
                    "while there is a product that uses them ");
        }
        measureUnitsDao.delete(measureUnits);
    }

    @Override
    public void rename(final Long measureUnitsId, final String newName) {
        MeasureUnits measureUnits = measureUnitsDao.findById(measureUnitsId);
        measureUnits.setName(newName);
        measureUnitsDao.update(measureUnits);
    }

}
