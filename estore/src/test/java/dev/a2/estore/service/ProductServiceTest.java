package dev.a2.estore.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import com.opencsv.CSVWriter;
import dev.a2.estore.dao.ProductDao;
import dev.a2.estore.dao.UserDao;
import dev.a2.estore.dto.*;

import dev.a2.estore.exception.PriceListImportException;
import dev.a2.estore.exception.ProductReserveException;
import dev.a2.estore.model.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("Testing ProductService")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private CategoryService categoryService;

    @Mock
    private MeasureUnitsService measureUnitsService;

    @Mock
    private OrderService orderService;

    @Mock
    private JmsService jmsService;

    @InjectMocks
    private ProductService productService = new ProductServiceImpl();

    @Nested
    @DisplayName("Testing findById method")
    class findByIdTest {
        @DisplayName("when a product is found then this product is returned")
        @Test
        void findByIdTest1() {
            // given
            Product product = new Product();
            product.setId(1L);
            when(productDao.findById(product.getId())).thenReturn(product);

            // run
            Product result = productService.findById(product.getId());

            // assert
            assertEquals(product, result);
            verify(productDao, times(1)).findById(1L);
        }

        @DisplayName("when a product is not found then null is returned")
        @Test
        void findByIdTest2() {
            // given
            when(productDao.findById(1L)).thenReturn(null);

            // run
            Product result = productService.findById(1L);

            // assert
            assertNull(result);
            verify(productDao, times(1)).findById(1L);
        }
    }

    @Nested
    @DisplayName("Testing save method")
    class saveTest {
        @DisplayName("when this method is called then a product is saved")
        @Test
        void saveTest1() {
            // given
            ProductDto productDto = new ProductDto();
            productDto.setPurchasingPrice(new BigDecimal(2.01));
            productDto.setSellingPrice(new BigDecimal(3.01));

            // run
            productService.save(productDto);

            // assert
            verify(productDao, times(1)).save(any(Product.class));
        }
    }

    @Nested
    @DisplayName("Testing getAllProducts method")
    class getAllProductsTest {
        @DisplayName("when products are found then products are returned")
        @Test
        void getAllProductsTest1() {
            // given
            List<Product> products = new ArrayList<>();
            products.add(new Product());
            products.add(new Product());
            when(productDao.getAllProducts()).thenReturn(products);

            // run
            List<Product> result = productService.getAllProducts();

            // assert
            assertEquals(products, result);
            verify(productDao, times(1)).getAllProducts();
        }

        @DisplayName("when products are not found then empty list is returned")
        @Test
        void getAllProductsTest2() {
            // given
            List<Product> products = new ArrayList<>();
            when(productDao.getAllProducts()).thenReturn(products);

            // run
            List<Product> result = productService.getAllProducts();

            // assert
            assertEquals(products, result);
            verify(productDao, times(1)).getAllProducts();
        }
    }

    @Nested
    @DisplayName("Testing getProductsByCategory method")
    class getProductsByCategoryTest {
        @DisplayName("when a category has type folder " +
                "then the all products of sub-categories are returned")
        @Test
        void getProductsByCategoryTest1() {
            // given
            Category category = new Category();
            category.setType("folder");
            when(categoryService.findById(1L)).thenReturn(category);

            // run
            productService.getProductsByCategory(1L);

            // assert
            verify(productDao, times(1)).getProductsByCategoriesIds(new ArrayList<>());
        }

        @DisplayName("when a category has type category " +
                "then products of this category are returned")
        @Test
        void getProductsByCategoryTest2() {
            // given
            Category category = new Category();
            category.setType("category");
            when(categoryService.findById(1L)).thenReturn(category);

            // run
            productService.getProductsByCategory(1L);

            // assert
            verify(productDao, times(1)).getProductsByCategoryId(1L);
        }

    }

    @Nested
    @DisplayName("Testing reserveProducts method")
    class reserveProductsTest {
        @DisplayName("when an available quantity for each product is equal or greater than required " +
                "then all products get reserved")
        @Test
        void reserveProductTest1() {
            // given
            Product product1 = new Product();
            product1.setId(1L);

            Product product2 = new Product();
            product2.setId(2L);

            List<OrderProduct> orderProducts = new ArrayList<>();

            OrderProduct orderProduct1 = mock(OrderProduct.class);
            when(orderProduct1.getProduct()).thenReturn(product1);
            when(orderProduct1.getQuantity()).thenReturn(1);

            OrderProduct orderProduct2 = mock(OrderProduct.class);
            when(orderProduct2.getProduct()).thenReturn(product2);
            when(orderProduct2.getQuantity()).thenReturn(1);
            orderProducts.add(orderProduct1);
            orderProducts.add(orderProduct2);

            // run
            productService.reserveProducts(orderProducts);

            // assert
            verify(productDao, times(2)).reserveProductWithLocking(anyLong(), anyInt());
            verify(productDao, times(0)).unreserveProductWithLocking(anyLong(), anyInt());
        }

        @DisplayName("when an available quantity for one of the products is less than required " +
                "then none of the products get reserved")
        @Test
        void reserveProductTest2() {
            // given
            Product product1 = new Product();
            product1.setId(1L);

            Product product2 = new Product();
            product2.setId(2L);

            List<OrderProduct> orderProducts = new ArrayList<>();

            OrderProductPK orderProductPK1 = new OrderProductPK();
            orderProductPK1.setProduct(product1);

            OrderProduct orderProduct1 = new OrderProduct();
            orderProduct1.setPk(orderProductPK1);
            orderProduct1.setQuantity(10);

            OrderProductPK orderProductPK2 = new OrderProductPK();
            orderProductPK2.setProduct(product2);

            OrderProduct orderProduct2 = new OrderProduct();
            orderProduct2.setPk(orderProductPK2);
            orderProduct2.setQuantity(10);

            orderProducts.add(orderProduct1);
            orderProducts.add(orderProduct2);

            doNothing().when(productDao).reserveProductWithLocking(1L, 10);
            doThrow(new ProductReserveException()).when(productDao).reserveProductWithLocking(2L, 10);

            // run and assert
            assertThrows(ProductReserveException.class, () -> {
                productService.reserveProducts(orderProducts);
            });
            verify(productDao, times(2)).reserveProductWithLocking(anyLong(), anyInt());
            verify(productDao, times(1)).unreserveProductWithLocking(anyLong(), anyInt());
        }
    }

    @Nested
    @DisplayName("Testing unreserveProduct method")
    class unreserveProductTest {
        @DisplayName("when this method is called then products get unreserved")
        @Test
        void unreserveProductTest1() {
            // given
            Product product1 = new Product();
            product1.setId(1L);

            Product product2 = new Product();
            product2.setId(2L);

            List<OrderProduct> orderProducts = new ArrayList<>();

            OrderProduct orderProduct1 = mock(OrderProduct.class);
            when(orderProduct1.getProduct()).thenReturn(product1);
            when(orderProduct1.getQuantity()).thenReturn(1);

            OrderProduct orderProduct2 = mock(OrderProduct.class);
            when(orderProduct2.getProduct()).thenReturn(product2);
            when(orderProduct2.getQuantity()).thenReturn(1);
            orderProducts.add(orderProduct1);
            orderProducts.add(orderProduct2);

            // run
            productService.unreserveProducts(orderProducts);

            // assert
            verify(productDao, times(2)).unreserveProductWithLocking(anyLong(), anyInt());
        }
    }

    @Nested
    @DisplayName("Testing buyProducts method")
    class buyProductTest {
        @DisplayName("when this method is called then products get bought")
        @Test
        void buyProductTest1() {
            // given
            Product product1 = new Product();
            product1.setId(1L);
            product1.setQuantityReserved(1);
            product1.setSaleCount(0L);

            Product product2 = new Product();
            product2.setId(2L);
            product2.setQuantityReserved(1);
            product2.setSaleCount(0L);

            List<OrderProduct> orderProducts = new ArrayList<>();

            OrderProduct orderProduct1 = mock(OrderProduct.class);
            when(orderProduct1.getProduct()).thenReturn(product1);
            when(orderProduct1.getQuantity()).thenReturn(1);

            OrderProduct orderProduct2 = mock(OrderProduct.class);
            when(orderProduct2.getProduct()).thenReturn(product2);
            when(orderProduct2.getQuantity()).thenReturn(1);

            orderProducts.add(orderProduct1);
            orderProducts.add(orderProduct2);

            // run
            productService.buyProducts(orderProducts);

            // assert
            assertEquals(0, product1.getQuantityReserved());
            assertEquals(1, product1.getSaleCount());
            assertEquals(0, product2.getQuantityReserved());
            assertEquals(1, product2.getSaleCount());
            verify(productDao, times(1)).update(product1);
            verify(productDao, times(1)).update(product2);
        }

    }

    @Nested
    @DisplayName("Testing update method")
    class updateTest {
        @DisplayName("when a dto has a new price then the price of a product is updated")
        @Test
        void updateTest1() {
           // given
           Product product = new Product();
           Price price = new Price();
           price.setPrice(new BigDecimal(20));
           List<Price> prices = new ArrayList<>();
           prices.add(price);
           product.setPurchasingPrices(prices);

           ProductDto productDto = new ProductDto();
           productDto.setPurchasingPrice(new BigDecimal(25));
           productDto.setSellingPrice(new BigDecimal(30));

            // run
            productService.update(product, productDto);

            // assert
            assertEquals(new BigDecimal(25).setScale(2, RoundingMode.HALF_EVEN),
                    product.getPurchasingPrices().get(1).getPrice());
            verify(productDao, times(1)).update(product);
        }

        @DisplayName("when a dto has the same price as the current price of a product " +
                "then the price of this product is not updated")
        @Test
        void updateTest2() {
            // given
            Product product = new Product();
            Price price = new Price();
            price.setPrice(new BigDecimal(20).setScale(2, RoundingMode.HALF_EVEN));
            List<Price> prices = new ArrayList<>();
            prices.add(price);
            product.setPurchasingPrices(prices);

            ProductDto productDto = new ProductDto();
            productDto.setPurchasingPrice(new BigDecimal(20));
            productDto.setSellingPrice(new BigDecimal(30));

            // run
            productService.update(product, productDto);

            // assert
            assertEquals(1, product.getPurchasingPrices().size());
            assertEquals(new BigDecimal(20).setScale(2, RoundingMode.HALF_EVEN),
                    product.getPurchasingPrices().get(0).getPrice());
            verify(productDao, times(1)).update(product);
        }

        @DisplayName("when a dto has a new product name then the name of a product is updated")
        @Test
        void updateTest3() {
            // given
            Product product = new Product();
            Price price = new Price();
            price.setPrice(new BigDecimal(20));
            List<Price> prices = new ArrayList<>();
            prices.add(price);
            product.setPurchasingPrices(prices);

            ProductDto productDto = new ProductDto();
            productDto.setName("newName");
            productDto.setPurchasingPrice(new BigDecimal(20));
            productDto.setSellingPrice(new BigDecimal(30));

            // run
            productService.update(product, productDto);

            // assert
            assertEquals("newName", product.getName());
            verify(productDao, times(1)).update(product);
        }
    }

    @Nested
    @DisplayName("Testing searchByCriteria method")
    class searchByCriteriaTest {
        @DisplayName("when criteria have the empty input and the empty category id " +
                "then method productDao.findByPartialName is called")
        @Test
        void searchByCriteriaTest1() {
            // given
            Map<String, String> searchCriteria = new HashMap<>();
            searchCriteria.put("input", "");
            searchCriteria.put("categoryId", "");

            // run
            productService.findByCriteria(searchCriteria);

            // assert
            verify(productDao, times(1)).findByPartialName("");
        }

        @DisplayName("when criteria have only a partial name " +
                "then products are searched by this partial name")
        @Test
        void searchByCriteriaTest2() {
            // given
            Map<String, String> searchCriteria = new HashMap<>();
            searchCriteria.put("input", "test");
            searchCriteria.put("categoryId", "");

            // run
            productService.findByCriteria(searchCriteria);

            // assert
            verify(productDao, times(1)).findByPartialName("test");
        }

        @DisplayName("when criteria have a partial name and a category id " +
                "then products are searched by the partial name and the category id")
        @Test
        void searchByCriteriaTest3() {
            // given
            Map<String, String> searchCriteria = new HashMap<>();
            searchCriteria.put("input", "test");
            searchCriteria.put("categoryId", "1");

            Category category = new Category();
            category.setId(1L);
            category.setType("category");
            when(categoryService.findById(1L)).thenReturn(category);

            List<Long> categoriesIds = new ArrayList<>();
            categoriesIds.add(1L);

            // run
            productService.findByCriteria(searchCriteria);

            // assert
            verify(productDao, times(1))
                    .findByPartialNameAndCategoriesIds("test", categoriesIds);
        }

        @DisplayName("when criteria have a partial name and a category id of type folder " +
                "then products are searched by the partial name and the all sub-categories of the category")
        @Test
        void searchByCriteriaTest4() {
            // given
            Map<String, String> searchCriteria = new HashMap<>();
            searchCriteria.put("input", "test");
            searchCriteria.put("categoryId", "1");

            Category category = new Category();
            category.setType("folder");

            List<Category> categories = new ArrayList<>();
            List<Long> categoriesIds = new ArrayList<>();

            when(categoryService.findById(1L)).thenReturn(category);

            // run
            productService.findByCriteria(searchCriteria);

            // assert
            verify(productDao, times(1))
                    .findByPartialNameAndCategoriesIds("test", categoriesIds);
        }

        @DisplayName("when attributes and values of a product match searched criteria " +
                "then this product gets returned")
        @Test
        void searchByCriteriaTest5() {
            // given

            //first product with matching attributes and values
            Attribute attribute1 = new Attribute();
            attribute1.setName("attribute1");

            Attribute attribute2 = new Attribute();
            attribute2.setName("attribute2");

            ProductAttribute productAttribute1 = new ProductAttribute();
            ProductAttributePK productAttributePK1 = new ProductAttributePK();
            productAttributePK1.setAttribute(attribute1);
            productAttribute1.setPk(productAttributePK1);
            productAttribute1.setValue("value1");

            ProductAttribute productAttribute2 = new ProductAttribute();
            ProductAttributePK productAttributePK2 = new ProductAttributePK();
            productAttributePK2.setAttribute(attribute2);
            productAttribute2.setPk(productAttributePK2);
            productAttribute2.setValue("value2");

            List<ProductAttribute> productAttributes1 = new ArrayList<>();
            productAttributes1.add(productAttribute1);
            productAttributes1.add(productAttribute2);
            Product product1 = new Product();
            product1.setAttributes(productAttributes1);

            //second product with not matching values
            ProductAttribute productAttribute3 = new ProductAttribute();
            ProductAttributePK productAttributePK3 = new ProductAttributePK();
            productAttributePK3.setAttribute(attribute1);
            productAttribute3.setPk(productAttributePK3);
            productAttribute3.setValue("value1");

            ProductAttribute productAttribute4 = new ProductAttribute();
            ProductAttributePK productAttributePK4 = new ProductAttributePK();
            productAttributePK4.setAttribute(attribute2);
            productAttribute4.setPk(productAttributePK4);
            productAttribute4.setValue("value3");

            List<ProductAttribute> productAttributes2 = new ArrayList<>();
            productAttributes2.add(productAttribute3);
            productAttributes2.add(productAttribute4);
            Product product2 = new Product();
            product2.setAttributes(productAttributes2);

            //third product with no attributes
            ProductAttribute productAttribute5 = new ProductAttribute();
            ProductAttributePK productAttributePK5 = new ProductAttributePK();
            productAttribute5.setPk(productAttributePK5);

            List<ProductAttribute> productAttributes3 = new ArrayList<>();
            Product product3 = new Product();
            product3.setAttributes(productAttributes3);

            //search criteria
            Map<String, String> searchCriteria = new HashMap<>();
            searchCriteria.put("input", "");
            searchCriteria.put("categoryId", "");
            searchCriteria.put("attribute1", "value1");
            searchCriteria.put("attribute2", "value2");

            List<Product> products = new ArrayList<>();
            products.add(product1);
            products.add(product2);
            when(productDao.findByPartialName("")).thenReturn(products);

            // run
            List<Product> result = productService.findByCriteria(searchCriteria);

            // assert
            assertEquals(1, result.size());
        }

        @DisplayName("when attributes and values of a product do not match searched criteria " +
                "then this product gets ignored")
        @Test
        void searchByCriteriaTest6() {
            // given

            //first product
            Attribute attribute1 = new Attribute();
            attribute1.setName("attribute1");

            Attribute attribute2 = new Attribute();
            attribute2.setName("attribute2");

            ProductAttribute productAttribute1 = new ProductAttribute();
            ProductAttributePK productAttributePK1 = new ProductAttributePK();
            productAttributePK1.setAttribute(attribute1);
            productAttribute1.setPk(productAttributePK1);
            productAttribute1.setValue("value1");

            ProductAttribute productAttribute2 = new ProductAttribute();
            ProductAttributePK productAttributePK2 = new ProductAttributePK();
            productAttributePK2.setAttribute(attribute2);
            productAttribute2.setPk(productAttributePK2);
            productAttribute2.setValue("value2");

            List<ProductAttribute> productAttributes1 = new ArrayList<>();
            productAttributes1.add(productAttribute1);
            productAttributes1.add(productAttribute2);
            Product product1 = new Product();
            product1.setAttributes(productAttributes1);

            //search criteria
            Map<String, String> searchCriteria = new HashMap<>();
            searchCriteria.put("input", "");
            searchCriteria.put("categoryId", "");
            searchCriteria.put("attribute1", "value3");
            searchCriteria.put("attribute2", "value2");

            List<Product> products = new ArrayList<>();
            products.add(product1);
            when(productDao.findByPartialName("")).thenReturn(products);

            // run
            List<Product> result = productService.findByCriteria(searchCriteria);

            // assert
            assertEquals(0, result.size());
        }


        @DisplayName("when criteria have a product attribute with value 'All' " +
                "then all products with this attribute are returned")
        @Test
        void searchByCriteriaTest7() {
            // given
            Attribute attribute = new Attribute();
            attribute.setName("test");

            ProductAttribute productAttribute1 = new ProductAttribute();
            ProductAttributePK productAttributePK1 = new ProductAttributePK();
            productAttributePK1.setAttribute(attribute);
            productAttribute1.setPk(productAttributePK1);
            productAttribute1.setValue("1");
            List<ProductAttribute> productAttributes1 = new ArrayList<>();
            productAttributes1.add(productAttribute1);
            Product product1 = new Product();
            product1.setAttributes(productAttributes1);


            ProductAttribute productAttribute2 = new ProductAttribute();
            ProductAttributePK productAttributePK2 = new ProductAttributePK();
            productAttributePK2.setAttribute(attribute);
            productAttribute2.setPk(productAttributePK2);
            productAttribute2.setValue("2");
            List<ProductAttribute> productAttributes2 = new ArrayList<>();
            productAttributes2.add(productAttribute2);
            Product product2 = new Product();
            product2.setAttributes(productAttributes2);

            Map<String, String> searchCriteria = new HashMap<>();
            searchCriteria.put("input", "");
            searchCriteria.put("categoryId", "");
            searchCriteria.put("test", "All");

            List<Product> products = new ArrayList<>();
            products.add(product1);
            products.add(product2);
            when(productDao.findByPartialName("")).thenReturn(products);

            // run
            List<Product> result = productService.findByCriteria(searchCriteria);

            // assert
            assertEquals(2, result.size());
        }
    }

    @Nested
    @DisplayName("Testing getTopSellingProducts method")
    class getTopTestSellingProductsTest {
        @DisplayName("when this method is called then top selling products are returned")
        @Test
        void getTopTestSellingProductsTest1() {
            // given

            List<Product> products = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                Product product = new Product();
                product.setSaleCount(1L);
                products.add(product);
            }

            when(productDao.getAllProducts()).thenReturn(products);

            // run
            List<Product> result = productService.getTopSellingProducts(10);

            // assert
            assertEquals(10, result.size());
        }

        @DisplayName("when there are products with sale count zero then they are ingnored")
        @Test
        void getTopTestSellingProductsTest2() {
            // given

            List<Product> products = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Product product = new Product();
                product.setSaleCount(0L);
                products.add(product);
            }
            for (int i = 0; i < 5; i++) {
                Product product = new Product();
                product.setSaleCount(1L);
                products.add(product);
            }

            when(productDao.getAllProducts()).thenReturn(products);

            // run
            List<Product> result = productService.getTopSellingProducts(10);

            // assert
            assertEquals(5, result.size());
        }
    }

    @Nested
    @DisplayName("Testing createPriceListFile method")
    class createPriceListFileTest {
        @DisplayName("when this method is called then a price list file is created")
        @Test
        void createPriceListFileTest1() throws IOException {
            // given
            String fileName = "estore-price-list.csv";

            // run
            File file = productService.createPriceListFile(fileName);

            // assert
            assertEquals(fileName, file.getName());
        }
    }

    @Nested
    @DisplayName("Testing processPriceListFile method")
    class processPriceListFileTest {
        @DisplayName("when a product has no id then this product gets saved")
        @Test
        void processPriceListFileTest1() throws IOException {
            // given
            File priceList = new File("estore-price-list.csv");
            FileWriter outputfile = new FileWriter(priceList);
            CSVWriter writer = new CSVWriter(outputfile);
            String[] header = {"ID",
                    "Name",
                    "Purchasing price",
                    "Selling price",
                    "Weight",
                    "Height",
                    "Width",
                    "Depth",
                    "Quantity in stock",
                    "Image url",
                    "Measure units id",
                    "Category id"};
            writer.writeNext(header);

            List<Product> products = new ArrayList<>();

            Price price = new Price();
            price.setPrice(new BigDecimal(20));
            price.setCreationDate(LocalDate.now());
            List<Price> prices = new ArrayList<>();
            prices.add(price);

            Product product1 = new Product();
            product1.setName("Product name");
            product1.setPurchasingPrices(prices);
            product1.setWeight(1.0);
            product1.setHeight(1.0);
            product1.setWidth(1.0);
            product1.setDepth(1.0);
            product1.setQuantityInStock(1);

            MeasureUnits measureUnits = new MeasureUnits();
            measureUnits.setId(1L);
            product1.setMeasureUnits(measureUnits);

            product1.setSellingPrice(new BigDecimal(30));

            Category category = new Category();
            category.setId(1L);
            product1.setCategory(category);

            products.add(product1);

            products.forEach( product -> {
                String[] row = {"",
                        product.getName(),
                        product.getRecentPurchasingPrice().toString(),
                        product.getSellingPrice().toString(),
                        product.getWeight().toString(),
                        product.getHeight().toString(),
                        product.getWidth().toString(),
                        product.getDepth().toString(),
                        product.getQuantityInStock().toString(),
                        product.getImageUrl(),
                        product.getMeasureUnits().getId().toString(),
                        product.getCategory().getId().toString()};
                writer.writeNext(row);
            });
            writer.close();


            FileInputStream input = new FileInputStream(priceList);
            MultipartFile multipartFile = new MockMultipartFile("file",
                    priceList.getName(), "text/plain", IOUtils.toByteArray(input));

            PriceListDto priceListDto = new PriceListDto();
            priceListDto.setPriceList(multipartFile);


            // run
            productService.processPriceListFile(priceListDto);

            // assert
            verify(productDao, times(1)).save(any(Product.class));


        }

        @DisplayName("when a product has id then this product gets updated")
        @Test
        void processPriceListFileTest2() throws IOException {
            // given
            File priceList = new File("estore-price-list.csv");
            FileWriter outputfile = new FileWriter(priceList);
            CSVWriter writer = new CSVWriter(outputfile);
            String[] header = {"ID",
                    "Name",
                    "Purchasing price",
                    "Selling price",
                    "Weight",
                    "Height",
                    "Width",
                    "Depth",
                    "Quantity in stock",
                    "Image url",
                    "Measure units id",
                    "Category id"};
            writer.writeNext(header);

            List<Product> products = new ArrayList<>();

            Price price = new Price();
            price.setPrice(new BigDecimal(20));
            price.setCreationDate(LocalDate.now());
            List<Price> prices = new ArrayList<>();
            prices.add(price);

            Product product1 = new Product();
            product1.setId(1L);
            product1.setName("Product name");
            product1.setPurchasingPrices(prices);
            product1.setWeight(1.0);
            product1.setHeight(1.0);
            product1.setWidth(1.0);
            product1.setDepth(1.0);
            product1.setQuantityInStock(1);

            MeasureUnits measureUnits = new MeasureUnits();
            measureUnits.setId(1L);
            product1.setMeasureUnits(measureUnits);

            product1.setSellingPrice(new BigDecimal(30));

            Category category = new Category();
            category.setId(1L);
            product1.setCategory(category);

            products.add(product1);

            products.forEach( product -> {
                String[] row = {product.getId().toString(),
                        product.getName(),
                        product.getRecentPurchasingPrice().toString(),
                        product.getSellingPrice().toString(),
                        product.getWeight().toString(),
                        product.getHeight().toString(),
                        product.getWidth().toString(),
                        product.getDepth().toString(),
                        product.getQuantityInStock().toString(),
                        product.getImageUrl(),
                        product.getMeasureUnits().getId().toString(),
                        product.getCategory().getId().toString()};
                writer.writeNext(row);
            });
            writer.close();


            FileInputStream input = new FileInputStream(priceList);
            MultipartFile multipartFile = new MockMultipartFile("file",
                    priceList.getName(), "text/plain", IOUtils.toByteArray(input));

            PriceListDto priceListDto = new PriceListDto();
            priceListDto.setPriceList(multipartFile);

            when(productDao.findById(anyLong())).thenReturn(product1);

            // run
            productService.processPriceListFile(priceListDto);

            // assert
            verify(productDao, times(1)).update(any(Product.class));
        }

        @DisplayName("when one of the values is invalid then exception PriceListException is thrown")
        @Test
        void processPriceListFileTest3() throws IOException {
            // given
            File priceList = new File("estore-price-list.csv");
            FileWriter outputfile = new FileWriter(priceList);
            CSVWriter writer = new CSVWriter(outputfile);
            String[] header = {"ID",
                    "Name",
                    "Purchasing price",
                    "Selling price",
                    "Weight",
                    "Height",
                    "Width",
                    "Depth",
                    "Quantity in stock",
                    "Image url",
                    "Measure units id",
                    "Category id"};
            writer.writeNext(header);

            List<Product> products = new ArrayList<>();

            Price price = new Price();
            price.setPrice(new BigDecimal(20));
            price.setCreationDate(LocalDate.now());
            List<Price> prices = new ArrayList<>();
            prices.add(price);

            Product product1 = new Product();
            product1.setId(1L);
            product1.setName("Product name");
            product1.setPurchasingPrices(prices);
            product1.setWeight(1.0);
            product1.setHeight(1.0);
            product1.setWidth(1.0);
            product1.setDepth(1.0);
            product1.setQuantityInStock(1);

            MeasureUnits measureUnits = new MeasureUnits();
            measureUnits.setId(1L);
            product1.setMeasureUnits(measureUnits);

            product1.setSellingPrice(new BigDecimal(30));

            Category category = new Category();
            category.setId(1L);
            product1.setCategory(category);

            products.add(product1);

            products.forEach( product -> {
                String[] row = {product.getId().toString(),
                        product.getName(),
                        product.getRecentPurchasingPrice().toString(),
                        product.getSellingPrice().toString(),
                        "",
                        product.getHeight().toString(),
                        product.getWidth().toString(),
                        product.getDepth().toString(),
                        product.getQuantityInStock().toString(),
                        product.getImageUrl(),
                        product.getMeasureUnits().getId().toString(),
                        product.getCategory().getId().toString()};
                writer.writeNext(row);
            });
            writer.close();


            FileInputStream input = new FileInputStream(priceList);
            MultipartFile multipartFile = new MockMultipartFile("file",
                    priceList.getName(), "text/plain", IOUtils.toByteArray(input));

            PriceListDto priceListDto = new PriceListDto();
            priceListDto.setPriceList(multipartFile);

            // run and assert
            assertThrows(PriceListImportException.class, () -> {
                productService.processPriceListFile(priceListDto);

            });
        }

        @DisplayName("when one of the values is missing then exception PriceListException is thrown")
        @Test
        void processPriceListFileTest4() throws IOException {
            // given
            File priceList = new File("estore-price-list.csv");
            FileWriter outputfile = new FileWriter(priceList);
            CSVWriter writer = new CSVWriter(outputfile);
            String[] header = {"ID",
                    "Name",
                    "Purchasing price",
                    "Selling price",
                    "Weight",
                    "Height",
                    "Width",
                    "Depth",
                    "Quantity in stock",
                    "Image url",
                    "Measure units id",
                    "Category id"};
            writer.writeNext(header);

            List<Product> products = new ArrayList<>();

            Price price = new Price();
            price.setPrice(new BigDecimal(20));
            price.setCreationDate(LocalDate.now());
            List<Price> prices = new ArrayList<>();
            prices.add(price);

            Product product1 = new Product();
            product1.setId(1L);
            product1.setName("Product name");
            product1.setPurchasingPrices(prices);
            product1.setWeight(1.0);
            product1.setHeight(1.0);
            product1.setWidth(1.0);
            product1.setDepth(1.0);
            product1.setQuantityInStock(1);

            MeasureUnits measureUnits = new MeasureUnits();
            measureUnits.setId(1L);
            product1.setMeasureUnits(measureUnits);

            product1.setSellingPrice(new BigDecimal(30));

            Category category = new Category();
            category.setId(1L);
            product1.setCategory(category);

            products.add(product1);

            products.forEach( product -> {
                String[] row = {product.getId().toString(),
                        product.getName(),
                        product.getRecentPurchasingPrice().toString(),
                        product.getSellingPrice().toString(),
                        product.getHeight().toString(),
                        product.getWidth().toString(),
                        product.getDepth().toString(),
                        product.getQuantityInStock().toString(),
                        product.getImageUrl(),
                        product.getMeasureUnits().getId().toString(),
                        product.getCategory().getId().toString()};
                writer.writeNext(row);
            });
            writer.close();


            FileInputStream input = new FileInputStream(priceList);
            MultipartFile multipartFile = new MockMultipartFile("file",
                    priceList.getName(), "text/plain", IOUtils.toByteArray(input));

            PriceListDto priceListDto = new PriceListDto();
            priceListDto.setPriceList(multipartFile);

            // run and assert
            assertThrows(RuntimeException.class, () -> {
                productService.processPriceListFile(priceListDto);

            });
        }
    }

    @Nested
    @DisplayName("Testing deleteProductAttribute method")
    class deleteProductAttributeTest {
        @DisplayName("when this method id called then method productDao.deleteAttribute is called")
        @Test
        void deleteProductAttributeTest1() {
            // given

            // run
            productService.deleteProductAttribute(1L, 1L);

            //assert
            verify(productDao, times(1)).deleteProductAttribute(1L, 1L);

        }
    }


}


