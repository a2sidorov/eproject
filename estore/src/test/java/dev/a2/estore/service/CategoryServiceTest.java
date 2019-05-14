package dev.a2.estore.service;

import java.util.*;

import dev.a2.estore.dao.AttributeDao;
import dev.a2.estore.dao.CategoryDao;
import dev.a2.estore.dao.ProductDao;
import dev.a2.estore.dto.AttributeDto;

import dev.a2.estore.dto.CategoryDto;
import dev.a2.estore.exception.CategoryDeleteException;
import dev.a2.estore.model.Attribute;
import dev.a2.estore.model.Category;
import dev.a2.estore.model.Product;
import dev.a2.estore.model.ProductAttribute;
import dev.a2.estore.model.ProductAttributePK;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Testing CategoryService")
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryDao categoryDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private CategoryService categoryService = new CategoryServiceImpl();

    @Nested
    @DisplayName("Testing save method")
    class saveTest {
        @DisplayName("when this method is called then category is created and saved")
        @Test
        void saveTest1() {
            // given
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setParentCategoryId(0L);

            // run
            categoryService.save(categoryDto);

            // assert
            verify(categoryDao, times(1)).save(any(Category.class));
        }
    }

    @Nested
    @DisplayName("Testing getAllCategories method")
    class getAllCategoriesTest {
        @DisplayName("when this method is called then method categoryDao.getAllCategories gets called")
        @Test
        void getAllCategoriesTest1() {
            // given

            // run
            categoryService.getAllCategories();

            // assert
            verify(categoryDao, times(1)).getAllCategories();
        }
    }

    @Nested
    @DisplayName("Testing findById method")
    class findByIdMethodTest {
        @DisplayName("when this method is called then method categoryDao.findById gets called")
        @Test
        void findByIdTest1() {
            // given
            Long categoryId = 1L;

            // run
            categoryService.findById(categoryId);

            // assert
            verify(categoryDao, times(1)).findById(categoryId);
        }
    }

    @Nested
    @DisplayName("Testing getTopLevelCategories method")
    class getTopLevelCategoriesTest {
        @DisplayName("when this method is called " +
                "then methodcategoryDao.getTopLevelCategories gets called")
        @Test
        void getTopLevelCategoriesTest1() {
            // given

            // run
            categoryService.getTopLevelCategories();

            // assert
            verify(categoryDao, times(1)).getTopLevelCategories();
        }
    }

    @Nested
    @DisplayName("Testing deleteCategory method")
    class deleteCategoryTest {
        @DisplayName("when a category has no products then this category gets deleted")
        @Test
        void deleteCategoryTest1() {
            // given
            Category category = new Category();
            category.setId(1L);
            category.setType("category");

            when(categoryDao.findById(1L)).thenReturn(category);

            List<Product> products = new ArrayList<>();

            when(productDao.getProductsByCategoryId(1L)).thenReturn(products);

            // run
            categoryService.deleteCategory(1L);

            // assert
            verify(categoryDao, times(1)).delete(category);
        }

        @DisplayName("when a category has products then exception CategoryDeleteException gets thrown")
        @Test
        void deleteCategoryTest2() {
            // given
            Category category = new Category();
            category.setId(1L);
            category.setType("category");

            when(categoryDao.findById(1L)).thenReturn(category);

            Product product = new Product();

            List<Product> products = new ArrayList<>();
            products.add(product);

            when(productDao.getProductsByCategoryId(1L)).thenReturn(products);

            // run and assert
            assertThrows(CategoryDeleteException.class, () -> {
                categoryService.deleteCategory(1L);
            });
        }

        @DisplayName("when a category is a folder that has no sub-categories " +
                "then method productDao.getProductsByCategoriesIds is not called")
        @Test
        void deleteCategoryTest3() {
            // given
            Category category = new Category();
            category.setId(1L);
            category.setType("folder");
            List<Category> subCategories = new ArrayList<>();
            category.setSubCategories(subCategories);

            when(categoryDao.findById(1L)).thenReturn(category);
            List<Long> categoriesIds = new ArrayList<>();
            // run
            categoryService.deleteCategory(1L);

            // assert
            verify(productDao, times(0)).getProductsByCategoriesIds(categoriesIds);
        }
    }

    @Nested
    @DisplayName("Testing renameCategory method")
    class renameCategoryTest {
        @DisplayName("when this method is called then a category gets renamed")
        @Test
        void renameCategoryTest1() {
            // given
            Category category = new Category();

            when(categoryDao.findById(1L)).thenReturn(category);

            // run
            categoryService.renameCategory(1L, "newName");

            // assert
            assertEquals("newName", category.getName());
            verify(categoryDao, times(1)).update(category);
        }
    }

    @Nested
    @DisplayName("Testing getSubCategoriesIds method")
    class getSubCategoriesIdsTest {
        @DisplayName("when this method is called then the all ids of all sub-categories are returned")
        @Test
        void getSubCategoriesIds() {
            // given
            Category folder = new Category();
            folder.setType("folder");
            folder.setId(1L);

                // sub-categories
                Category category1 = new Category();
                category1.setId(2L);
                category1.setType("category");

                Category category2 = new Category();
                category2.setId(3L);
                category2.setType("category");

                Category subfolder = new Category();
                subfolder.setId(4L);
                subfolder.setType("folder");

                    // sub-sub-categories
                    Category category5 = new Category();
                    category5.setId(5L);
                    category5.setType("category");

                    Category category6 = new Category();
                    category6.setId(6L);
                    category6.setType("category");

            List<Category> subSubCategories = new ArrayList<>();
            subSubCategories.add(category5);
            subSubCategories.add(category6);
            subfolder.setSubCategories(subSubCategories);

            List<Category> subCategories = new ArrayList<>();
            subCategories.add(category1);
            subCategories.add(category2);
            subCategories.add(subfolder);
            folder.setSubCategories(subCategories);

            List<Category> categories = new ArrayList<>();
            categories.add(folder);

            // run
            List<Long> result = categoryService.getSubCategoriesIds(categories);

            // assert
            assertTrue(result.containsAll(Arrays.asList(2L, 3L, 5L, 6L)));
        }
    }
}


