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

import dev.a2.estore.dto.AttributeDto;
import dev.a2.estore.model.Attribute;
import dev.a2.estore.model.ProductAttribute;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This interface provides methods to manage product attributes.
 *
 * @author Andrei Sidorov
 */
public interface AttributeService {

    /**
     * Creates and saves a new product attribute.
     *
     * @param atributeDto the dto with attribute information.
     */
    void save(AttributeDto atributeDto);

    /**
     * Finds all product-attribute entities by their product id.
     *
     * @param productId the id of the product whose attributes need to be found.
     * @return the list of product-attribute entities.
     */
    List<ProductAttribute> getAllProductAttributes(Long productId);

    /**
     * Updates product attributes.
     *
     * @param productId the id of the product whose attributes need to be updated.
     * @param map the map with new attribute values.
     */
    void updateAttributes(Long productId, Map<String, String> map);

    /**
     * Finds attributes with their values filtered by a category id.
     *
     * @param categoryId the id of a category.
     * @return the set of attributes with values.
     */
    Set<Attribute> getAttributesWithValues(Long categoryId);

    /**
     * Finds attributes with all their values filtered by a category id.
     *
     * @param attributeId the id of an atrribute.
     * @param categoryId the id of a product category.
     * @return the set of attribute values.
     */
    Set<String> getAttributeValues(Long attributeId, Long categoryId);

    /**
     * Finds attributes with all their values filtered by categories ids.
     *
     * @param attributeId the id of an atrribute.
     * @param categoriesIds the ids of product categories..
     * @return the set of attribute values.
     */
    Set<String> getAttributeValues(Long attributeId, List<Long> categoriesIds);

}
