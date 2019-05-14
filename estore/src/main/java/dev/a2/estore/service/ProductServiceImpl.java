package dev.a2.estore.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import dev.a2.estore.dao.ProductDao;
import dev.a2.estore.dto.ImageDto;
import dev.a2.estore.dto.PriceListDto;
import dev.a2.estore.dto.ProductDto;
import dev.a2.estore.exception.PriceListImportException;
import dev.a2.estore.exception.ProductReserveException;
import dev.a2.estore.model.Category;
import dev.a2.estore.model.OrderProduct;
import dev.a2.estore.model.Price;
import dev.a2.estore.model.Product;
import dev.a2.estore.model.ProductAttribute;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * This class provides implementation for ProductService interface.
 *
 * @author Andrei Sidorov
 */
@Service
public class ProductServiceImpl implements ProductService {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(ProductService.class);

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

    /**
     * Injects MeasureUnitsService.
     */
    @Autowired
    private MeasureUnitsService measureUnitsService;

    /**
     * Path to a folder for storing uploaded product images.
     */
    @Value("${files.upload.folder}")
    private String filesFolder;

    @Transactional
    @Override
    public Product findById(final Long productId) {
        return productDao.findById(productId);
    }

    @Transactional
    @Override
    public void save(final ProductDto productDto) {
        Product newProduct = new Product();
        newProduct.setName(productDto.getName());

        Price price = new Price();
        price.setCreationDate(LocalDate.now());
        price.setPrice(productDto.getPurchasingPrice().setScale(2, RoundingMode.HALF_EVEN));
        List<Price> prices = new ArrayList<>();
        prices.add(price);

        newProduct.setPurchasingPrices(prices);
        newProduct.setSellingPrice(productDto.getSellingPrice().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        newProduct.setCategory(categoryService.findById(productDto.getCategoryId()));
        newProduct.setMeasureUnits(measureUnitsService.findById(productDto.getMeasureUnitsId()));
        newProduct.setWeight(productDto.getWeight());
        newProduct.setHeight(productDto.getHeight());
        newProduct.setWidth(productDto.getWidth());
        newProduct.setDepth(productDto.getDepth());
        newProduct.setQuantityInStock(productDto.getInStock());
        newProduct.setSaleCount(0L);
        newProduct.setImageUrl(productDto.getImageUrl());
        productDao.save(newProduct);
    }

    @Transactional
    @Override
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    @Transactional
    @Override
    public List<Product> getProductsByCategory(final Long categoryId) {
        Category category = categoryService.findById(categoryId);
        List<Product> products = null;

        if (category.getType().equals("folder")) {
            List<Long> subCategoriesIds = categoryService.getSubCategoriesIds(category.getSubCategories());
            products = productDao.getProductsByCategoriesIds(subCategoriesIds);
        } else {
            products = productDao.getProductsByCategoryId(categoryId);
        }
        return products;
    }

    @Override
    public void reserveProducts(final List<OrderProduct> orderProducts) {
        for (int i = 0; i < orderProducts.size(); i++) {
            OrderProduct orderProduct = orderProducts.get(i);
            try {
                productDao.reserveProductWithLocking(orderProduct.getProduct().getId(), orderProduct.getQuantity());
            } catch (ProductReserveException e) {
                for (int j = i - 1; j >= 0; j--) {
                    orderProduct = orderProducts.get(j);
                    productDao.unreserveProductWithLocking(orderProduct.getProduct().getId(), orderProduct.getQuantity());
                }
                throw e;
            }
        }
    }

    @Override
    public void unreserveProducts(final List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            productDao.unreserveProductWithLocking(orderProduct.getProduct().getId(), orderProduct.getQuantity());
        }
    }

    @Transactional
    @Override
    public void buyProducts(final List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            Product product = orderProduct.getProduct();
            Integer reservedQuantity = product.getQuantityReserved();
            product.setQuantityReserved(reservedQuantity - orderProduct.getQuantity());
            product.setSaleCount(product.getSaleCount() + 1);
            productDao.update(product);
        }
    }

    @Transactional
    @Override
    public void update(final Product product, final ProductDto productDto) {
        product.setName(productDto.getName());
        List<Price> prices = product.getPurchasingPrices();
        BigDecimal newPrice =  productDto.getPurchasingPrice().setScale(2, RoundingMode.HALF_EVEN);

        if (!newPrice.equals(prices.get(prices.size() - 1).getPrice())) {
            Price price = new Price();
            price.setPrice(newPrice);
            price.setCreationDate(LocalDate.now());
            prices.add(price);
        }
        product.setSellingPrice(productDto.getSellingPrice().setScale(2, RoundingMode.HALF_EVEN));
        product.setCategory(categoryService.findById(productDto.getCategoryId()));
        product.setMeasureUnits(measureUnitsService.findById(productDto.getMeasureUnitsId()));
        product.setWeight(productDto.getWeight());
        product.setHeight(productDto.getHeight());
        product.setWidth(productDto.getWidth());
        product.setDepth(productDto.getDepth());
        product.setQuantityInStock(productDto.getInStock());

        if (productDto.getImageUrl() != null) {
            product.setImageUrl(productDto.getImageUrl());
        }
        productDao.update(product);
    }

    @Transactional
    @Override
    public void updateImage(final Product product, final ImageDto imageDto) {
        product.setImageUrl("/resources/img/" + imageDto.getImage().getOriginalFilename());
        MultipartFile image = imageDto.getImage();

        File file = new File(filesFolder + "img/" + image.getOriginalFilename());
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(image.getBytes());
        } catch (IOException e) {
            logger.error(e);
        }
        productDao.update(product);
    }

    @Transactional
    @Override
    public List<Product> findByCriteria(final Map<String, String> searchCriteria) {
        String input = searchCriteria.remove("input").trim();
        String categoryIdString = searchCriteria.remove("categoryId");
        List<Long> categoriesIds = null;
        List<Product> products = null;

        if (categoryIdString.matches("^\\.*\\d+\\.*$")) {
            Long categoryId = Long.parseLong(categoryIdString);
            Category category = categoryService.findById(categoryId);

            if (category.getType().equals("folder")) {
                categoriesIds = categoryService.getSubCategoriesIds(category.getSubCategories());
            } else {
                categoriesIds = new ArrayList<>();
                categoriesIds.add(category.getId());
            }
        }

        if (categoriesIds == null) {
            products = productDao.findByPartialName(input);
        } else {
            products = productDao.findByPartialNameAndCategoriesIds(input, categoriesIds);
        }

        // Applies the filter by attributes.
        if (searchCriteria.size() > 0) {
            products = products.stream().filter(product -> {
                boolean result = true;

                if (product.getAttributes().isEmpty()) {
                    result = false;
                }

                for (ProductAttribute pa : product.getAttributes()) {
                    String attributeName = pa.getPk().getAttribute().getName();
                    String attributeValue = pa.getValue();

                    if (searchCriteria.get(attributeName).equals("All")) {
                        continue;
                    }

                    if (!searchCriteria.get(attributeName).equals(attributeValue)) {
                        result = false;
                    }
                }
                return result;
            }).collect(Collectors.toList());
        }
        return products;
    }

    @Transactional
    @Override
    public List<Product> getTopSellingProducts(final int maxLength) {
        List<Product> products = productDao.getAllProducts();
        products.sort((p1, p2) -> p2.getSaleCount().compareTo(p1.getSaleCount()));

        if (products.size() > maxLength) {
            products.subList(maxLength, products.size()).clear();
        }
        products.removeIf(product -> product.getSaleCount() < 1);
        return products;
    }

    @Transactional
    @Override
    public File createPriceListFile(final String fileName) throws IOException {
        File priceList = new File(fileName);
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

        List<Product> products = getAllProducts();
        products.sort(Comparator.comparing(Product::getId));
        products.forEach(product -> {
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
        return priceList;
    }

    @Transactional
    @Override
    public void processPriceListFile(final PriceListDto priceListDto) throws IOException {
        try (Reader reader = new InputStreamReader(priceListDto.getPriceList().getInputStream())) {
            CsvToBean<ProductDto> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(ProductDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            Iterator<ProductDto> productDtoIterator = csvToBean.iterator();
            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

            int rowCounter = 0;
            while (productDtoIterator.hasNext()) {
                rowCounter++;
                ProductDto productDto = productDtoIterator.next();
                Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);
                if (violations.isEmpty()) {
                    if (productDto.getId() == null) {
                        save(productDto);
                    } else {
                        Product product = findById(productDto.getId());
                        update(product, productDto);
                    }
                } else {
                    throw new PriceListImportException("Invalid data at row: " + rowCounter);

                }
            }
        }
    }

    @Transactional
    @Override
    public void deleteProductAttribute(final Long productId, final Long attributeId) {
        productDao.deleteProductAttribute(productId, attributeId);
    }

}
