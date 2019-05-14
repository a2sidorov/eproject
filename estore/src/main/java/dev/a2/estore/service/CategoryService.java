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

import dev.a2.estore.dto.CategoryDto;
import dev.a2.estore.model.Category;

import java.util.List;

/**
 * This interface provies methods to manage product categories.
 *
 * @author Andrei Sidorov
 */
public interface CategoryService {

    /**
     * Creates and saves a new product category.
     *
     * @param categoryDto the dto with category information.
     */
    void save(CategoryDto categoryDto);

    /**
     * Finds all product categories.
     *
     * @return the list of categories.
     */
    List<Category> getAllCategories();

    /**
     * Finds a category by its id.
     *
     * @param categoryId the id of the category that needs to be found.
     * @return a category.
     */
    Category findById(Long categoryId);

    /**
     * Finds top level categories from the category tree.
     *
     * @return the list of categories.
     */
    List<Category> getTopLevelCategories();

    /**
     * Deletes a category.
     *
     * @param categoryId the id of the category that needs to be deleted.
     */
    void deleteCategory(Long categoryId);

    /**
     * Renames a category.
     *
     * @param categoryId the id of the category that needs to be renamed.
     * @param newName the new category name.
     */
    //void renameCategory(CategoryDto categoryDto);
    void renameCategory(Long categoryId, String newName);

    /**
     * Finds all sub-categories ids.
     *
     * @param categories the list of categories. whose sub-categories ids need to be found.
     * @return the list of categories ids.
     */
    List<Long> getSubCategoriesIds(List<Category> categories);

}
