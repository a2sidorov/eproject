package dev.a2.estore.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import dev.a2.estore.dto.ImageDto;
import dev.a2.estore.dto.PriceListDto;
import dev.a2.estore.dto.ProductDto;
import dev.a2.estore.model.OrderProduct;
import dev.a2.estore.model.Product;

/**
 * This interface provides methods to manage products.
 *
 * @author Andrei Sidorov
 */
public interface ProductService {

    /**
     * Finds a product by its id.
     *
     * @param productId the id of the product that needs to be found.
     * @return a product.
     */
    Product findById(Long productId);

    /**
     * Creates and saves a new product.
     *
     * @param productDto the dto with product information.
     */
    void save(ProductDto productDto);

    /**
     * Finds all products.
     *
     * @return the list of all products.
     */
    List<Product> getAllProducts();

    /**
     * Finds products by a category id.
     *
     * @param categoryId the id of the products that need to be found.
     * @return the list of products.
     */
    List<Product> getProductsByCategory(Long categoryId);

    /**
     * Reserves products. Implemenation must be sycnhronized to prevent the concurrent execution.
     *
     * @param orderProducts the list of order-product entities that need to be reserved.
     */
    void reserveProducts(List<OrderProduct> orderProducts);

    /**
     * Unreserves products. Implemenation must be sycnhronized to prevent the concurrent execution.
     *
     * @param orderProducts the list of order-product entities that need to be unreserved.
     */
    void unreserveProducts(List<OrderProduct> orderProducts);

    /**
     * Buys products.
     *
     * @param orderProducts the list of order-product entities that need to be bought.
     */
    void buyProducts(List<OrderProduct> orderProducts);

    /**
     * Updates a product.
     *
     * @param product the product that that needs to be updated.
     * @param productDto the dto with update information.
     */
    void update(Product product, ProductDto productDto);

    /**
     * Updates a product image.
     *
     * @param product the product whose image needs to be updated.
     * @param imageDto the dto with an image file.
     */
    void updateImage(Product product, ImageDto imageDto);

    /**
     * Finds products by search criteria such as a partial name, a category id (categories ids)
     * and attribute values.
     *
     * @param criteria the map with all criteria.
     * @return the list of products.
     */
    List<Product> findByCriteria(Map<String, String> criteria);

    /**
     * Calculates top-selling products.
     *
     * @param maxLength the maximum products in the list.
     * @return the list of products.
     */
    List<Product> getTopSellingProducts(int maxLength);

    /**
     * Creates a price-list file from the current database.
     *
     * @param fileName the name of a price-list file
     * @throws IOException the IOException.
     * @return the price-list file.
     */
    File createPriceListFile(String fileName) throws IOException;

    /**
     * Processes an uploaded price-list file by updating exiting entities
     * and creating new ones if no id is provided.
     *
     * @param priceListDto the dto with a price-list file.
     * @throws IOException the IOException.
     */
    void processPriceListFile(PriceListDto priceListDto) throws IOException;


    /**
     * Deletes product-attribute entity.
     *
     * @param productId the id of the product whose attribute needs to be deleted.
     * @param attributeId the id of the attribute that needs to be deleted from a product.
     */
    void deleteProductAttribute(Long productId, Long attributeId);

}
