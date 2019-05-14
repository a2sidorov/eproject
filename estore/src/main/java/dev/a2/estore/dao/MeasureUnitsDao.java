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

import java.util.List;

/**
 * This interface provides methods to manipulate the measure units entity.
 *
 * @author Andrei Sidorov
 */
public interface MeasureUnitsDao {

    /**
     * Saves measure units to a database.
     *
     * @param measureUnits the measure units that needs to be saved.
     */
    void save(MeasureUnits measureUnits);

    /**
     * Finds all measure units.
     *
     * @return the list of measure units.
     */
    List<MeasureUnits> getAllMeasureUnits();

    /**
     * Finds measure units by its id.
     *
     * @param measureUnitsId the id of the measure units that needs to found.
     * @return the measure units.
     */
    MeasureUnits findById(Long measureUnitsId);

    /**
     * Deletes measure units.
     * @param measureUnits the measure units that needs to be deleted.
     *
     */
    void delete(MeasureUnits measureUnits);

    /**
     * Updates measure units.
     *
     * @param measureUnits the measure units that need to be updated.
     */
    void update(MeasureUnits measureUnits);

}
