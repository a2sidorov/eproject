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

import dev.a2.estore.model.MeasureUnits;

import java.util.List;

/**
 * This interface provides methods for managing product measure units.
 *
 * @author Andrei Sidorov
 */
public interface MeasureUnitsService {

    /**
     * Creates and saves new measure units.
     *
     * @param measureUnitsName the name of measure units.
     */
    void save(String measureUnitsName);

    /**
     * Finds all product measure units.
     *
     * @return the list of all product measure units.
     */
    List<MeasureUnits> getAllMeasureUnits();

    /**
     * Finds product measure units by its id.
     *
     * @param measureUnitsId the id of the measure units that need to be found.
     * @return a measure units.
     */
    MeasureUnits findById(Long measureUnitsId);

    /**
     * Delete measure units if there are no products with use them.
     *
     * @param measureUnitsId the id of the measure units that need to be deleted.
     */
    void delete(Long measureUnitsId);

    /**
     * Renames the name of measure units.
     *
     * @param measureUnitsId the id of measure units.
     * @param newName the new name of measure units.
     */
    void rename(Long measureUnitsId, String newName);

}
