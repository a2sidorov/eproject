package dev.a2.estore.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.a2.estore.dao.AttributeDao;
import dev.a2.estore.dao.CategoryDao;
import dev.a2.estore.dao.ProductDao;
import dev.a2.estore.dto.AttributeDto;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Testing AttributeService")
@ExtendWith(MockitoExtension.class)
class AttributeServiceTest {

    @Mock
    private AttributeDao attributeDao;

    @Mock
    private CategoryDao categoryDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private AttributeService attributeService = new AttributeServiceImpl();

    @Nested
    @DisplayName("Testing save method")
    class saveTest {
        @DisplayName("when an attribute does not exist " +
                "then an attrbute and a ProductAttribute are created and saved")
        @Test
        void saveTest1() {
            // given
            AttributeDto attributeDto = new AttributeDto();
            attributeDto.setProductId(1L);
            when(productDao.findById(1L)).thenReturn(mock(Product.class));

            // run
            attributeService.save(attributeDto);

            // assert
            verify(attributeDao, times(1)).save(any(Attribute.class));
            verify(attributeDao, times(1)).save(any(ProductAttribute.class));
        }

        @DisplayName("when an attribute exists then only a ProductAttribute is created and saved")
        @Test
        void saveTest2() {
            // given
            Attribute attribute = new Attribute();
            attribute.setName("Attrbiute name");

            AttributeDto attributeDto = new AttributeDto();
            attributeDto.setName("Attribute name");
            attributeDto.setProductId(1L);

            when(attributeDao.findByName(attributeDto.getName())).thenReturn(attribute);
            when(productDao.findById(1L)).thenReturn(mock(Product.class));

            // run
            attributeService.save(attributeDto);

            // assert
            verify(attributeDao, times(0)).save(any(Attribute.class));
            verify(attributeDao, times(1)).save(any(ProductAttribute.class));
        }
    }

    @Nested
    @DisplayName("Testing getAllProductAttributes method")
    class getAllProductAttributesTest {
        @DisplayName("when this method is called then method attrbiuteDao.getAllProductAttributes() is called")
        @Test
        void getAllProductAttributesTest1() {
            // given
            Long productId = 1L;

            // run
            attributeService.getAllProductAttributes(productId);

            // given
            verify(attributeDao, times(1)).getAllProductAttributes(productId);
        }
    }

    @Nested
    @DisplayName("Testing updateAttributes method")
    class updateAttributesTest {
        @DisplayName("when this method is called then the values of a product's attributes are updated")
        @Test
        void updateAttributesTest1() {
            // given
            Long productId = 1L;
            Map<String, String> attributes = new HashMap<>();
            attributes.put("Attribute name", "Attribute value");

            Attribute attribute = new Attribute();
            attribute.setName("Attribute name");

            ProductAttributePK productAttributePK = new ProductAttributePK();
            productAttributePK.setAttribute(attribute);

            ProductAttribute productAttribute = new ProductAttribute();
            productAttribute.setPk(productAttributePK);

            List<ProductAttribute> productAttributes = new ArrayList<>();
            productAttributes.add(productAttribute);

            when(attributeDao.getAllProductAttributes(productId)).thenReturn(productAttributes);

            // run
            attributeService.updateAttributes(productId, attributes);

            // given
            assertEquals("Attribute value", productAttribute.getValue());
            verify(attributeDao, times(1)).update(any(ProductAttribute.class));
        }
    }

    @Nested
    @DisplayName("Testing getAttributesWithValues method")
    class getAttributesWithValuesTest {
        @DisplayName("when this method is called " +
                "then attributes of products of a specified category are returned")
        @Test
        void getAttributesWithValuesTest1() {
            //given
            Category category = new Category();
            category.setType("category");

            when(categoryDao.findById(1L)).thenReturn(category);


            Attribute attribute = new Attribute();
            attribute.setName("Attribute name");

            Product product = new Product();

            ProductAttributePK productAttributePK = new ProductAttributePK();
            productAttributePK.setAttribute(attribute);
            productAttributePK.setProduct(product);

            ProductAttribute productAttribute = new ProductAttribute();
            productAttribute.setPk(productAttributePK);

            List<ProductAttribute> productAttributes = new ArrayList<>();
            productAttributes.add(productAttribute);

            product.setAttributes(productAttributes);

            List<Product> products = new ArrayList<>();
            products.add(product);

            when(productDao.getProductsByCategoryId(1L)).thenReturn(products);

            // run
            Set<Attribute> attributes = attributeService.getAttributesWithValues(1L);

            // assert
            assertEquals("Attribute name", attributes.iterator().next().getName());

        }
    }

    @Nested
    @DisplayName("Testing getAttributeValues method")
    class getAttributeValuesTest {
        @DisplayName("when there are equal attribute values then they are merged and returned")
        @Test
        void getAttributeValuesTest1() {
            // given
            ProductAttribute productAttribute1 = new ProductAttribute();
            productAttribute1.setValue("attribute value");

            ProductAttribute productAttribute2 = new ProductAttribute();
            productAttribute2.setValue("attribute value");

            ProductAttribute productAttribute3 = new ProductAttribute();
            productAttribute2.setValue("another attribute value");

            List<ProductAttribute> productAttributes = new ArrayList<>();
            productAttributes.add(productAttribute1);
            productAttributes.add(productAttribute2);
            productAttributes.add(productAttribute3);

            when(attributeDao.getAttributeValues(1L, 1L)).thenReturn(productAttributes);

            // run
            Set<String> result = attributeService.getAttributeValues(1L, 1L);

            // assert
            assertEquals(2, result.size());

        }

        @DisplayName("when the second argument is a list of categories' ids " +
                "then the overloaded method is called")
        @Test
        void getAttributeValuesTest2() {
            // given
            ProductAttribute productAttribute1 = new ProductAttribute();
            productAttribute1.setValue("attribute value");

            ProductAttribute productAttribute2 = new ProductAttribute();
            productAttribute2.setValue("attribute value");

            ProductAttribute productAttribute3 = new ProductAttribute();
            productAttribute2.setValue("another attribute value");

            List<ProductAttribute> productAttributes = new ArrayList<>();
            productAttributes.add(productAttribute1);
            productAttributes.add(productAttribute2);
            productAttributes.add(productAttribute3);

            List<Long> categoriesIds = new ArrayList<>();
            categoriesIds.add(1L);
            categoriesIds.add(2L);

            when(attributeDao.getAttributeValues(1L, categoriesIds)).thenReturn(productAttributes);

            // run
            Set<String> result = attributeService.getAttributeValues(1L, categoriesIds);

            // assert
            assertEquals(2, result.size());
        }
    }

}


