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

package dev.a2.estore.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dev.a2.estore.dto.AttributeDto;
import dev.a2.estore.dto.ImageDto;
import dev.a2.estore.dto.PriceListDto;
import dev.a2.estore.dto.ProductDto;
import dev.a2.estore.exception.PriceListExportException;
import dev.a2.estore.exception.ProductNotFoundException;
import dev.a2.estore.model.Order;
import dev.a2.estore.model.Price;
import dev.a2.estore.model.Product;
import dev.a2.estore.service.AttributeService;
import dev.a2.estore.service.CategoryService;
import dev.a2.estore.service.MeasureUnitsService;
import dev.a2.estore.service.ProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * This class provides mapping for product related requests.
 *
 * @author Andrei Sidorov
 */
@Controller
@Validated
@SessionAttributes("order")
public class ProductController {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(ProductController.class);

    /**
     * Injects bean ProductService.
     */
    @Autowired
    private ProductService  productService;

    /**
     * Injects bean CategoryService.
     */
    @Autowired
    private CategoryService categoryService;

    /**
     * Injects bean MessageSource.
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Injects bean AttributeService.
     */
    @Autowired
    private AttributeService attributeService;

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
     * Initializes the shopping cart.
     *
     * @return order.
     */
    @ModelAttribute("order")
    public Order initCart() {
        return new Order();
    }

    /**
     * Generates the home page.
     *
     * @param  model the model
     * @return the view name 'home'.
     */
    @GetMapping("/")
    public String showHomePage(final Model model) {
        model.addAttribute("categories", categoryService.getTopLevelCategories());
        model.addAttribute("order");
        return "home";
    }

    /**
     * Generates a product page with information about a product.
     *
     * @param  model the model.
     * @param  productId the id of a product.
     * @return the view name 'product'.
     */
    @GetMapping("/product")
    public String showProduct(final Model model,
                              final @RequestParam("id") @NotNull @Min(1) @Max(Long.MAX_VALUE) Long productId) {
            Product product = productService.findById(productId);
        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("attributes", attributeService.getAllProductAttributes(product.getId()));
        }
        return "product";
    }

    /**
     * Generates the create-product page.
     *
     * @param model the model.
     * @return the view name 'create-product'.
     */
    @GetMapping("/product/create")
    public String showProductCreationForm(final Model model) {
        model.addAttribute("allMeasureUnits", measureUnitsService.getAllMeasureUnits());
        model.addAttribute("categoryLeafs", categoryService.getAllCategories());
        model.addAttribute("productDto", new ProductDto());
        return "create-product";
    }

    /**
     * Creates a product.
     *
     * @param productDto the dto with product information.
     * @param result the holder for binding validation errors.
     * @param redirectAttributes the attributes for flash messages.
     * @return redirects to view 'create-product'.
     */
    @PostMapping("/product/create")
    public String createProduct(final @Validated @ModelAttribute ProductDto productDto,
                                final BindingResult result,
                                final RedirectAttributes redirectAttributes) {
        logger.info("A product create request: " + productDto);
        if (result.hasErrors()) {
            return "create-product";
        }
        productService.save(productDto);
        logger.info("A new product has been created.");
        String message = messageSource.getMessage("product.create.success", null, Locale.US);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/product/create";
    }

    /**
     * Exports the price list of the application.
     *
     * @return the response entity with the price list.
     */
    @GetMapping("/product/price-list/export")
    public ResponseEntity<InputStreamResource>  downloadCSV() {
        try {
            String fileName = "estore-price-list.csv";
            File priceList = productService.createPriceListFile(fileName);
            HttpHeaders respHeaders = new HttpHeaders();
            MediaType mediaType = new MediaType("text", "csv");
            respHeaders.setContentType(mediaType);
            respHeaders.setContentDispositionFormData("attachment", fileName);
            InputStreamResource isr = new InputStreamResource(new FileInputStream(priceList));
            logger.info("Price-list has been exported.");
            return new ResponseEntity<>(isr, respHeaders, HttpStatus.OK);
        } catch (Exception e) {
            throw new PriceListExportException();
        }
    }

    /**
     * Generates the import-price-list page.
     *
     * @param model the model.
     * @return the view name 'import-price-list'.
     */
    @GetMapping("/product/price-list/import")
    public String showPriceListUploadForm(final Model model) {
        model.addAttribute("priceListDto", new PriceListDto());
        return "import-price-list";
    }

    /**
     * Imports the the price-list.
     *
     * @param priceListDto the dto with csv file.
     * @param redirectAttributes the attributes for flash messages.
     * @return redirect to view 'import-price-list'.
     */
    @PostMapping("/product/price-list/import")
    public String updateProductImage(final @ModelAttribute PriceListDto priceListDto,
                                     final RedirectAttributes redirectAttributes) {
        logger.info("A price-list import request.");

        //Checks if the uploded file has type csv.
        String mimeType = priceListDto.getPriceList().getContentType();
        if (!mimeType.equals("application/vnd.ms-excel")) {
            logger.warn("Uploaded product image has invalid type: " + mimeType);
            String error = messageSource.getMessage("price.list.invalid.type", null, Locale.US);
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/product/price-list/import";
        }

        try {
            productService.processPriceListFile(priceListDto);
        } catch (Exception e) {
            String error = e.getMessage();
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/product/price-list/import";
        }
        logger.info("Price-list has been imported.");
        String message = messageSource.getMessage("product.price-list.import.success", null, Locale.US);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/product/price-list/import";
    }

    /**
     * Generates the edit-product page.
     *
     * @param model the model.
     * @param productId the id of a product.
     * @return the view name 'edit-product'.
     */
    @GetMapping("/product/{productId}/edit")
    public String showProductUpdateForm(final Model model,
                                        final @PathVariable @NotNull @Min(1) @Max(Long.MAX_VALUE) Long productId) {
        Product product = productService.findById(productId);

        if (product == null) {
            throw new ProductNotFoundException();
        }
        List<Price> prices = product.getPurchasingPrices();
        model.addAttribute("product", product);
        model.addAttribute("purchasingPrice", prices.get(prices.size() - 1));
        model.addAttribute("categoryLeafs", categoryService.getAllCategories());
        model.addAttribute("allMeasureUnits", measureUnitsService.getAllMeasureUnits());
        model.addAttribute("attributes", attributeService.getAllProductAttributes(product.getId()));
        model.addAttribute("productDto", new ProductDto());
        model.addAttribute("order");
        return "edit-product";
    }

    /**
     * Updates product information.
     *
     * @param productId the id of the product that needs to be updated.
     * @param productDto the dto with new product information.
     * @param result the holder for binding validation errors.
     * @param redirectAttributes the attributes for flash messages.
     * @return redirects to view 'edit-product'.
     */
    @PostMapping("/product/{productId}/update")
    public String updateProduct(final @PathVariable @NotNull @Min(1) @Max(Long.MAX_VALUE) Long productId,
                                final @Validated @ModelAttribute ProductDto productDto,
                                final BindingResult result,
                                final RedirectAttributes redirectAttributes) {
        logger.info("A product update request. " + productDto);

        if (result.hasErrors()) {
            return "edit-product";
        }
        Product product = productService.findById(productId);

        if (product == null) {
            throw new ProductNotFoundException();
        }
        productService.update(product, productDto);
        logger.info("Product '" + product.getName() + "' has been updated.");
        String message = messageSource.getMessage("product.update.success", null, Locale.US);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/product/" + productId + "/edit";
    }

    /**
     * Generates update-image page to update a product image.
     *
     * @param model the model.
     * @param productId the id of the product whose image needs to be updated.
     * @return the view name 'update-image'.
     */
    @GetMapping("/product/{productId}/image/update")
    public String showUpdateImage(final Model model,
                                  final @PathVariable @NotNull @Min(1) @Max(Long.MAX_VALUE) Long productId) {
        Product product = productService.findById(productId);

        if (product == null) {
            throw new ProductNotFoundException();
        }
        model.addAttribute("product", product);
        model.addAttribute("imageDto", new ImageDto());
        model.addAttribute("order");
        return "update-image";
    }

    /**
     * Updates a product image.
     *
     * @param productId the id of the product whose image needs to be updated.
     * @param imageDto the dto with an image file.
     * @param redirectAttributes the attributes for flash messages.
     * @return redirects to view 'update-image'.
     */
    @PostMapping("/product/{productId}/image/update")
    public String updateProductImage(final @PathVariable @NotNull @Min(1) @Max(Long.MAX_VALUE) Long productId,
                                     final @ModelAttribute ImageDto imageDto,
                                     final RedirectAttributes redirectAttributes) {
        logger.info("A product image update request.");

        //Checks if the uploded file has type image.
        String mimeType = imageDto.getImage().getContentType();
        String type = mimeType.split("/")[0];
        if (!type.equals("image")) {
            logger.warn("Uploaded product image has invalid type: " + mimeType);
            String error = messageSource.getMessage("product.image.update.invalid.type", null, Locale.US);
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/product/" + productId + "/image/update";
        }

        Product product = productService.findById(productId);

        if (product == null) {
            throw new ProductNotFoundException();
        }
        productService.updateImage(product, imageDto);
        logger.info("A product image has been updated.");
        String message = messageSource.getMessage("product.image.update.success", null, Locale.US);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/product/" + productId + "/image/update";
    }

    /**
     * Generates create-attribute page to create product attributes.
     *
     * @param model the model.
     * @param productId the id of a product.
     * @return the view name 'create-attribute'.
     */
    @GetMapping("/product/{productId}/attribute/create")
    public String showCreateProductAttribute(final Model model,
                                             final @PathVariable @NotNull @Min(1) @Max(Long.MAX_VALUE) Long productId) {
        Product product = productService.findById(productId);

        if (product == null) {
            throw new ProductNotFoundException();
        }
        model.addAttribute("product", product);
        model.addAttribute("attributeDto", new AttributeDto());
        return "create-attribute";
    }

    /**
     * Creates a product attribute.
     *
     * @param productId the id of a product.
     * @param attributeDto the dto with attribute information.
     * @param result the holder for binding validation errors.
     * @param redirectAttributes the attributes for flash messages.
     * @return redirect to view 'create-attribute'.
     */
    @PostMapping("/product/{productId}/attribute/create")
    public String createProductAttribute(final @PathVariable @NotNull @Min(1) @Max(Long.MAX_VALUE) Long productId,
                                         final @ModelAttribute @Validated AttributeDto attributeDto,
                                         final BindingResult result,
                                         final RedirectAttributes redirectAttributes) {
        logger.info("A product attribute create request. " + attributeDto);

        if (result.hasErrors()) {
            return "create-attribute";
        }
        attributeService.save(attributeDto);
        logger.info("A product attribute has been created.");
        String message = messageSource.getMessage("product.attribute.create.success", null, Locale.US);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/product/" + productId + "/attribute/create";
    }

    /**
     * Genearates delete-product page to delete product attributes.
     *
     * @param model the model.
     * @param productId the id of the product whose attribute needs to be deleted.
     * @return the view name 'delete-attribute'.
     */
    @GetMapping("/product/{productId}/attribute/delete")
    public String showDeleteProductAttribute(final Model model,
                                             final @PathVariable
                                             @Validated
                                             @NotNull
                                             @Min(1) @Max(Long.MAX_VALUE) Long productId) {
        Product product = productService.findById(productId);
        model.addAttribute("product", product);
        model.addAttribute("attributes", attributeService.getAllProductAttributes(product.getId()));
        return "delete-attribute";
    }

    /**
     * Deletes a product attribute.
     *
     * @param productId the id of the product whose attribute needs to be deleted.
     * @param attributeId the id of the attribute whose attribute needs to be deleted.
     * @param redirectAttributes the attributes for flash messages.
     * @return redirects to view 'delete-attribute'.
     */
    @PostMapping("/product/{productId}/attribute/delete")
    public String deleteProductAttributes(final @PathVariable @NotNull @Min(1) @Max(Long.MAX_VALUE) Long productId,
                                          final @RequestParam @NotNull @Min(1) @Max(Long.MAX_VALUE) Long attributeId,
                                          final RedirectAttributes redirectAttributes) {
        logger.info("A product attribute delete request. " +
                "Product id '" + productId + "', attribute id '" + attributeId + "' ");
        productService.deleteProductAttribute(productId, attributeId);
        String message = messageSource.getMessage("product.attribute.delete.success", null, Locale.US);
        redirectAttributes.addFlashAttribute("message", message);
        logger.info("A product attribute has been deleted.");
        return "redirect:/product/" + productId + "/attribute/delete";
    }

    /**
     * Updates product attributes' values.
     *
     * @param productId the id of the product whose attribute needs to be updated.
     * @param map the map with product attributes and their changed values.
     * @param redirectAttributes the attributes for flash messages.
     * @return redirects to view 'delete-attribute'.
     */
    @PostMapping("/product/{productId}/attributes/update")
    public String updateProductAttributes(final @PathVariable String productId,
                                          final @RequestParam Map<String, String> map,
                                          final RedirectAttributes redirectAttributes) {
        logger.info("Product attributes update request. " + map);
        attributeService.updateAttributes(Long.parseLong(productId), map);
        logger.info("Product attributes have been updated.");
        String message = messageSource.getMessage("product.attributes.update.success", null, Locale.US);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/product/" + productId + "/edit";
    }

    /**
     * Generates measure units page.
     *
     * @param model the model
     * @return the view name 'measure-units'
     */
    @GetMapping("/product/measure-units")
    public String measureUnitsUpdate(final Model model) {
        model.addAttribute("measureUnitsList", measureUnitsService.getAllMeasureUnits());
        return "measure-units";
    }

    /**
     * Genearates edit-categories page to edit product categories.
     *
     * @param model the model.
     * @return the view name 'edit-categories'.
     */
    @GetMapping("/categories/edit")
    public String showEditCategories(final Model model) {
        model.addAttribute("categories", categoryService.getTopLevelCategories());
        model.addAttribute("order");
        return "edit-categories";
    }

    /**
     * Genearates top-products page with the top-ten-selling products.
     *
     * @param model the model.
     * @return the view name 'top-products'.
     */
    @GetMapping("/reports/top-products")
    public String showTopSellingProducts(final Model model) {
        model.addAttribute("products", productService.getTopSellingProducts(topProductsLength));
        return "top-products";
    }

}
