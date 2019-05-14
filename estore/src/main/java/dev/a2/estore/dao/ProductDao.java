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

import dev.a2.estore.model.Product;

/**
 * This interface provides methods to manipulate the product entity.
 *
 * @author Andrei Sidorov
 */
public interface ProductDao {

    /**
     * Saves a product to a database.
     *
     * @param product thd product.
     */
    void save(Product product);

    /**
     * Finds a product by its id.
     *
     * @param productId the id of the product that needs to be found.
     * @return a product.
     */
    Product findById(Long productId);

    /**
     * Finds all products.
     *
     * @return the list of products.
     */
    List<Product> getAllProducts();

    /**
     * Updates a product.
     *
     * @param product the product that needs to be updated.
     */
    void update(Product product);

    /**
     * Finds products by a category.
     *
     * @param categoryId the id of the category whose products need to be found.
     * @return the list of products.
     */
    List<Product> getProductsByCategoryId(Long categoryId);

    /**
     * Finds products by categories.
     *
     * @param categoriesIds the ids of categories whose products need to be found.
     * @return the list of products.
     */
    List<Product> getProductsByCategoriesIds(List<Long> categoriesIds);

    /**
     * Finds products by a partial name.
     *
     * @param input a partial or a full name of a product.
     * @return the list of products.
     */
    List<Product> findByPartialName(String input);

    /**
     * Finds products by a partial name and categories' ids.
     *
     * @param input a partial or a full name of a product.
     * @param categoriesIds the list of categories' ids.
     * @return the list of products.
     */
    List<Product> findByPartialNameAndCategoriesIds(String input, List<Long> categoriesIds);

    /**
     * Deletes product-attribute entities.
     *
     * @param productId the id of a product.
     * @param attributeId the id of a category.
     */
    void deleteProductAttribute(Long productId, Long attributeId);

    /**
     * Reserves products.
     *
     * @param productId the id of the product that needs to be reserved.
     * @param quantity the quantity that needs to be reserved.
     */
    void reserveProductWithLocking(Long productId, Integer quantity);

    /**
     * Unreserves products.
     *
     * @param productId the id of the product that needs to be unreserved.
     * @param quantity the quantity that needs to be unreserved.
     */
    void unreserveProductWithLocking(Long productId, Integer quantity);

    /**
     * Finds products by a measure units id.
     *
     * @param measureUnitsId the id of a measure units.
     * @return the list of products.
     */
    List<Product> findProductsByMeasureUnitsId(Long measureUnitsId);



}

