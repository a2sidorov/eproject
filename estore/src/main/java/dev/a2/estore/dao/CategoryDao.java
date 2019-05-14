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

import java.util.List;

/**
 * This interface provides methods to manipulate the product category entity.
 *
 * @author Andrei Sidorov
 */
public interface CategoryDao {

    /**
     * Saves a category to a database.
     *
     * @param category the category that needs to be saved.
     */
    void save(Category category);

    /**
     * Finds all categories.
     *
     * @return the list of categories.
     */
    List<Category> getAllCategories();

    /**
     * Finds a category by its id.
     *
     * @param categoryId the id of the category that needs to be found.
     * @return the category.
     */
    Category findById(Long categoryId);

    /**
     * Finds top-level categories from the category tree.
     *
     * @return the list of categories.
     */
    List<Category> getTopLevelCategories();

    /**
     * Deletes a category.
     *
     * @param category the category that need to be deleted.
     */
    void delete(Category category);

    /**
     * Updates a category.
     *
     * @param category the category that need to be updated.
     */
    void update(Category category);

}
