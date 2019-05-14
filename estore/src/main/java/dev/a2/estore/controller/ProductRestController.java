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

package dev.a2.estore.controller;

import dev.a2.estore.dto.CategoryDto;
import dev.a2.estore.model.Attribute;
import dev.a2.estore.model.Product;
import dev.a2.estore.service.AttributeService;
import dev.a2.estore.service.CategoryService;
import dev.a2.estore.service.MeasureUnitsService;
import dev.a2.estore.service.ProductService;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class provides rest mapping for product related requests.
 *
 * @author Andrei Sidorov
 */
@RestController
@Validated
public class ProductRestController {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(ProductRestController.class);

    /**
     * Injects bean ProductService.
     */
    @Autowired
    private ProductService productService;

    /**
     * Injects bean AttributeService.
     */
    @Autowired
    private AttributeService attributeService;

    /**
     * Injects bean CategoryService.
     */
    @Autowired
    private CategoryService categoryService;

    /**
     * Injects bean MeasureUnitsService.
     */
    @Autowired
    private MeasureUnitsService measureUnitsService;

    /**
     * The maximum products in the list of top-selling-products.
     */
    @Value("${top.products.max.length}")
    private int topProductsLength;

    /**
     * Sends all products.
     *
     * @return the list of products.
     */
    @GetMapping("/products")
    public List<Product> sendProducts() {
        return productService.getAllProducts();
    }

    /**
     * Sends products filtered by a category.
     *
     * @param categoryId the id of a product category.
     * @return the list of products.
     */
    @GetMapping("/products/category")
    public List<Product> filterProductsByCategoryApi(final @RequestParam("id")
                                                           @NotNull @Min(1) @Max(Long.MAX_VALUE) Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    /**
     * Sends products filtered by criteria.
     *
     * @param searchCriteria the criteria such as user input, category id and product attributes with values.
     * @return the list of products.
     */
    @PostMapping("/products/find")
    public List<Product> filterProductsBySearchCriteria(final @RequestBody Map<String, String> searchCriteria) {
        logger.info("A search products by criteria request: " + searchCriteria);
        return productService.findByCriteria(searchCriteria);
    }

    /**
     * Sends attributes with values for the product filter.
     *
     * @param categoryId the id of the category.
     * @return the set of attributes with values.
     */
    @GetMapping("/attributes/category")
    public Set<Attribute> getCategoryAttributes(final @RequestParam("id")
                                                      @NotNull
                                                      @Min(0)
                                                      @Max(Long.MAX_VALUE) Long categoryId) {
        return attributeService.getAttributesWithValues(categoryId);
    }

    /**
     * Creates a product category.
     *
     * @param categoryDto the dto with category information.
     */
    @PostMapping("/category/create")
    public void addCategory(final @RequestBody @Validated CategoryDto categoryDto) {
        logger.info("A category create request: " + categoryDto);
        categoryService.save(categoryDto);
        logger.info("A new category has been created.");
    }

    /**
     * Renames a product category.
     *
     * @param categoryId the id of the category that needs to be renamed.
     * @param newName a new name of the category.
     */
    @PutMapping("/category")
    public void renameCategory(final @RequestParam("id") @NotNull @Min(1) @Max(Long.MAX_VALUE) Long categoryId,
                               final @RequestParam("name") @NotEmpty String newName) {
        logger.info("A category rename request: " + "Category id '" + categoryId + "' New name '" + newName + "'");
        categoryService.renameCategory(categoryId, newName);
        logger.info("A category has been renamed.");
    }

    /**
     * Deletes a product category.
     *
     * @param categoryId the id of the category to be deleted.
     */
    @DeleteMapping("/category")
    public void deleteCategory(final @RequestParam("id") @NotNull @Min(1) @Max(Long.MAX_VALUE) Long categoryId) {
        logger.info("A category delete request: " + "Category id '" + categoryId + "'");
        categoryService.deleteCategory(categoryId);
        logger.info("A category has been deleted.");
    }

    /**
     * Creates product measure units.
     *
     * @param measureUnitsName the name of new measure units.
     */
    @GetMapping("/product/measure-units/create")
    public void createMeasureUnits(final @RequestParam("name") @NotEmpty String measureUnitsName) {
        logger.info("A measure units create request: Name '" + measureUnitsName + "'");
        measureUnitsService.save(measureUnitsName);
        logger.info("New measure units have been created.");
    }

    /**
     * Deletes product measure units.
     *
     * @param measureUnitsId the id of measure units.
     */
    @DeleteMapping("/product/measure-units/delete")
    public void deleteMeasureUnits(final @RequestParam("id")
                                         @NotNull @Min(1) @Max(Long.MAX_VALUE)
                                         Long measureUnitsId) {
        logger.info("A measure units delete request: id '" + measureUnitsId + "'");
        measureUnitsService.delete(measureUnitsId);
        logger.info("Measure units have been deleted.");
    }

    /**
     * Renames measure units.
     *
     * @param measureUnitsId the id of measure units that need to be renamed.
     * @param newName a new measure units name.
     */
    @PutMapping("/product/measure-units/rename")
    public void renameMeasureUnits(final @RequestParam("id")
                                   @NotNull @Min(1) @Max(Long.MAX_VALUE)
                                   Long measureUnitsId,
                                   final @RequestParam("name")
                                   @NotEmpty
                                   String newName) {
        logger.info("A measure units rename request: " +
                "id '" + measureUnitsId + "' " + "New name '" + newName + "'");
        measureUnitsService.rename(measureUnitsId, newName);
        logger.info("Measure units have been renamed.");
    }

    /**
     * Sends the list of top-ten-selling products.
     *
     * @return the list of top-ten-selling products.
     */
    @GetMapping("/api/top-products")
    public Object sendTopSellingProducts() {
        List<Map<String, String>> list = new ArrayList<>();

        productService.getTopSellingProducts(topProductsLength).forEach(product -> {
            Map<String, String> map = new HashMap<>();
            map.put("imageUrl", product.getImageUrl());
            map.put("name", product.getName());
            map.put("sellingPrice", product.getSellingPrice().toString());
            list.add(map);
        });

        if (list.size() == 0) {
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
        return list;
    }
}
