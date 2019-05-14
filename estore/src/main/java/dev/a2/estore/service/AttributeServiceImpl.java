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

import dev.a2.estore.dao.AttributeDao;
import dev.a2.estore.dao.CategoryDao;
import dev.a2.estore.dao.ProductDao;
import dev.a2.estore.dto.AttributeDto;
import dev.a2.estore.model.Attribute;
import dev.a2.estore.model.Category;
import dev.a2.estore.model.Product;
import dev.a2.estore.model.ProductAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class provides implementation for AttributeService interface.
 *
 * @author Andrei Sidorov
 */
@Service
@Transactional
public class AttributeServiceImpl implements AttributeService {

    /**
     * Injects AttributeDao.
     */
    @Autowired
    private AttributeDao attributeDao;

    /**
     * Injects CategoryDao.
     */
    @Autowired
    private CategoryDao categoryDao;

    /**
     * Injects ProductDao.
     */
    @Autowired
    private ProductDao productDao;

    /**
     * Injects CategoryService.
     */
    @Autowired
    private CategoryService categoryService;

    @Override
    public void save(final AttributeDto attributeDto) {
        Attribute newAttribute = attributeDao.findByName(attributeDto.getName());

        if (newAttribute == null) {
            newAttribute = new Attribute();
            newAttribute.setName(attributeDto.getName());
            attributeDao.save(newAttribute);
            newAttribute = attributeDao.findByName(newAttribute.getName());
        }
        Product product = productDao.findById(attributeDto.getProductId());

        // Checks if a product already has this attribute.
        boolean isPresent = false;
        for(ProductAttribute pa : product.getAttributes()) {
            if (pa.getPk().getAttribute().equals(newAttribute)) {
                isPresent = true;
            }
        }

        if (!isPresent) {
            attributeDao.save(new ProductAttribute(product, newAttribute));
        }
    }

    @Override
    public List<ProductAttribute> getAllProductAttributes(final Long productId) {
        return attributeDao.getAllProductAttributes(productId);
    }

    @Override
    public void updateAttributes(final Long productId, final Map<String, String> attributes) {
        List<ProductAttribute> productAttributes = attributeDao.getAllProductAttributes(productId);
        productAttributes.forEach(productAttribute -> {
            String value = attributes.get(productAttribute.getPk().getAttribute().getName());
            productAttribute.setValue(value);
            attributeDao.update(productAttribute);
        });
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Attribute> getAttributesWithValues(final Long categoryId) {
        Category category = categoryDao.findById(categoryId);
        Set<Attribute> attributes = new HashSet<>();

        if (category.getType().equals("folder")) {
            List<Long> subCategoriesIds = categoryService.getSubCategoriesIds(category.getSubCategories());
            List<Product> products =  productDao.getProductsByCategoriesIds(subCategoriesIds);
            for (Product product : products) {
                for (ProductAttribute productAttribute : product.getAttributes()) {
                    Attribute attribute = new Attribute();
                    attribute.setId(productAttribute.getPk().getAttribute().getId());
                    attribute.setName(productAttribute.getPk().getAttribute().getName());
                    attribute.setValues(getAttributeValues(productAttribute.getPk().getAttribute().getId(),
                            subCategoriesIds));
                    attributes.add(attribute);
                }
            }
        } else {
            List<Product> products = productDao.getProductsByCategoryId(categoryId);
            for (Product product : products) {
                for (ProductAttribute productAttribute : product.getAttributes()) {
                    Attribute attribute = new Attribute();
                    attribute.setId(productAttribute.getPk().getAttribute().getId());
                    attribute.setName(productAttribute.getPk().getAttribute().getName());
                    attribute.setValues(getAttributeValues(productAttribute.getPk().getAttribute().getId(),
                            categoryId));
                    attributes.add(attribute);
                }
            }
        }

        return attributes;
    }

    @Override
    public Set<String> getAttributeValues(final Long attributeId, final Long categoryId) {
        List<ProductAttribute> productAttributes =  attributeDao.getAttributeValues(attributeId, categoryId);
        return productAttributes
                .stream()
                .filter(productAttribute -> productAttribute.getValue() != null)
                .map(ProductAttribute::getValue)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getAttributeValues(final Long attributeId, final List<Long> categoriesIds) {
        List<ProductAttribute> productAttributes =  attributeDao.getAttributeValues(attributeId, categoriesIds);
        return productAttributes
                .stream()
                .filter(productAttribute -> productAttribute.getValue() != null)
                .map(ProductAttribute::getValue)
                .collect(Collectors.toSet());
    }

}
