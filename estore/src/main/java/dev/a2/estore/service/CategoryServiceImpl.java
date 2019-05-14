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

import dev.a2.estore.dao.CategoryDao;
import dev.a2.estore.dao.ProductDao;
import dev.a2.estore.dto.CategoryDto;
import dev.a2.estore.exception.CategoryDeleteException;
import dev.a2.estore.model.Product;
import dev.a2.estore.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides implementation for CategoryService interface.
 *
 * @author Andrei Sidorov
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

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

    @Override
    public void save(final CategoryDto categoryDto) {
        Category newCategory = new Category();
        newCategory.setName(categoryDto.getName());
        newCategory.setType(categoryDto.getType());
        Category parentCategory = null;

        if (!categoryDto.getParentCategoryId().equals(0L)) {
            parentCategory = findById(categoryDto.getParentCategoryId());
        }
        newCategory.setParentCategory(parentCategory);
        categoryDao.save(newCategory);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    @Override
    public Category findById(final Long categoryId) {
        return categoryDao.findById(categoryId);
    }

    @Override
    public List<Category> getTopLevelCategories() {
        return categoryDao.getTopLevelCategories();
    }

    @Override
    public void deleteCategory(final Long categoryId) {
        Category category = categoryDao.findById(categoryId);
        List<Product> products = null;

        if (category.getType().equals("folder")) {
            List<Long> categoriesIds = getSubCategoriesIds(category.getSubCategories());
            if (categoriesIds.size() > 0) {
                products = productDao.getProductsByCategoriesIds(categoriesIds);
            } else {
                products = new ArrayList<>();
            }
        } else {
            products = productDao.getProductsByCategoryId(categoryId);
        }

        if (products.size() > 0) {
            throw new CategoryDeleteException("Category cannot be deleted while it has products.");
        }
        categoryDao.delete(category);
    }

    @Override
    public void renameCategory(final Long categoryId, final String newName) {
        Category category = categoryDao.findById(categoryId);
        category.setName(newName);
        categoryDao.update(category);
    }

    @Override
    public List<Long> getSubCategoriesIds(final List<Category> categories) {
        List<Long> result = new ArrayList<>();
        for (Category category : categories) {

            if (category.getType().equals("category")) {
                result.add(category.getId());
            }

            if (category.getType().equals("folder")) {
                result.addAll(getSubCategoriesIds(category.getSubCategories()));
            }
        }
        return result;
    }

}
