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

import java.util.List;

/**
 * This interface provides methods to manipulate the product attribute entity.
 *
 * @author Andrei Sidorov
 */
public interface AttributeDao {

    /**
     * Saves an attribute to a database.
     *
     * @param attribute the attribute that needs to be saved.
     */
    void save(Attribute attribute);

    /**
     * Finds an attribute by its name.
     *
     * @param attributeName the name of the attrbite that needs to be found.
     * @return the matching attribute.
     */
    Attribute findByName(String attributeName);

    /**
     * Finds an attribute by its id.
     *
     * @param attributeId the id of the attribute that needs to found.
     * @return the attribute.
     */
    Attribute findById(Long attributeId);

    /**
     * Saves a product-attribute enity.
     *
     * @param productAttribute the product-attribute entity.
     */
    void save(ProductAttribute productAttribute);

    /**
     * Finds all product-attribute entity.
     *
     * @param productId the id of a product.
     * @return the list of product-attribute entities.
     */
    List<ProductAttribute> getAllProductAttributes(Long productId);

    /**
     * Updates a product-attribute entity.
     *
     * @param productAttribute the product-attribute entity.
     */
    void update(ProductAttribute productAttribute);


    /**
     * Finds all attribute values by its id and filtering by a category id.
     *
     * @param attributeId the id of the attribute whose values need to be found.
     * @param categoryId the id of a category.
     * @return the list of product-attribute entities.
     */
    List<ProductAttribute> getAttributeValues(Long attributeId, Long categoryId);

    /**
     * Finds all attribute values by its id and filtering by the list of a categories ids.
     *
     * @param attributeId the id of the attribute whose values need to be found.
     * @param categoriesIds the ids of categories.
     * @return the list of product-attribute entities.
     */
    List<ProductAttribute> getAttributeValues(Long attributeId, List<Long> categoriesIds);

}
