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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.a2.estore.dto.OrderStatusDto;
import dev.a2.estore.dto.ProductQuantityDto;
import dev.a2.estore.dto.RevenuePeriodDto;
import dev.a2.estore.model.Order;
import dev.a2.estore.model.OrderProduct;
import dev.a2.estore.model.Product;
import dev.a2.estore.service.OrderService;
import dev.a2.estore.service.ProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * This class provides rest mapping for order related requests.
 *
 * @author Andrei Sidorov
 */
@RestController
@Validated
@SessionAttributes("order")
public class OrderRestController {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(OrderRestController.class);

    /**
     * Injects bean ProductService.
     */
    @Autowired
    private ProductService  productService;

    /**
     * Injects bean OrderService.
     */
    @Autowired
    private OrderService orderService;

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
     * Adds a product to the cart.
     *
     * @param order the user order.
     * @param  productQuantityDto the dto with a product and quantity.
     * @return the response entity with the received dto.
     */
    @PostMapping("/order/product/add")
    public ResponseEntity<ProductQuantityDto> addProductToCart(final @SessionAttribute("order") Order order,
                                                               final @RequestBody
                                                               @Validated ProductQuantityDto productQuantityDto) {
        logger.info("An add product to the order request " + productQuantityDto);
        Product product = productService.findById(productQuantityDto.getProductId());
        List<OrderProduct> orderProducts = order.getOrderProducts();
        boolean isFound = false;

        for (OrderProduct orderProduct : orderProducts) {
            if (orderProduct.getProduct().getId().equals(productQuantityDto.getProductId())) {
                if (orderProduct.getQuantity() + productQuantityDto.getQuantity() <= product.getQuantityInStock()) {
                    orderProduct.setQuantity(orderProduct.getQuantity() + productQuantityDto.getQuantity());
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productQuantityDto);
                }
                isFound = true;
            }
        }

        // Creating an order product if none is found
        if (!isFound) {
            OrderProduct orderProduct = new OrderProduct(order, product, productQuantityDto.getQuantity());
            orderProducts.add(orderProduct);
        }
        productQuantityDto.setTotalNumberOfProducts(order.calculateTotalNumberOfProducts());
        logger.info("A product has been added to the cart.");
        return ResponseEntity.status(HttpStatus.OK).body(productQuantityDto);
    }

    /**
     * Updates the quantity of a product in the cart.
     *
     * @param order the user order.
     * @param  productQuantityDto the dto with a product and quantity information.
     * @return the response entity with the received dto.
     */
    @PostMapping("/order/product/update")
    public ProductQuantityDto updateProductQuantity(final @SessionAttribute("order") Order order,
                                                    final @RequestBody @Validated ProductQuantityDto productQuantityDto) {
        logger.info("An update product quantity request " + productQuantityDto);
        Product product = productService.findById(productQuantityDto.getProductId());
        List<OrderProduct> orderProducts = order.getOrderProducts();

        for (OrderProduct orderProduct : orderProducts) {
            if (orderProduct.getProduct().getId().equals(product.getId())) {
                orderProduct.setQuantity(productQuantityDto.getQuantity());
                productQuantityDto.setTotalSellingPrice(orderProduct.calculateTotalSellingPrice());
            }
        }
        productQuantityDto.setTotalNumberOfProducts(order.calculateTotalNumberOfProducts());
        productQuantityDto.setOrderPrice(order.calculateOrderPrice());
        logger.info("Product quantity has been updated.");
        return productQuantityDto;
    }

    /**
     * Sends the details of a requested order to a user.
     *
     * @param  orderId an order id.
     * @return an order details.
     */
    @GetMapping("/my-orders/{orderId}")
    public List<Map<String, String>> sendUserOrderDetails(final @PathVariable
                                                                @NotNull
                                                                @Min(1)
                                                                @Max(Long.MAX_VALUE) Long orderId) {
        List<OrderProduct> orderProducts = orderService.getOrderProducts(orderId);
        List<Map<String, String>> products = new ArrayList<>();

        for (OrderProduct orderProduct : orderProducts) {
            Map<String, String> productProperties = new HashMap<>();
            productProperties.put("id", orderProduct.getProduct().getId().toString());
            productProperties.put("name", orderProduct.getProduct().getName());
            productProperties.put("imageUrl", orderProduct.getProduct().getImageUrl());
            productProperties.put("price", orderProduct.getSellingPrice().toString());
            productProperties.put("quantity", orderProduct.getQuantity().toString());
            productProperties.put("units", orderProduct.getProduct().getMeasureUnits().getName());
            productProperties.put("amount", orderProduct.calculateTotalSellingPrice().toString());
            products.add(productProperties);
        }
        return products;
    }

    /**
     * Sends the details of a requested order to a manager.
     *
     * @param orderId an order id.
     * @return an order details.
     */
    @GetMapping("/orders/{orderId}")
    public List<Map<String, String>> sendOrderDetails(final @PathVariable
                                                            @NotNull
                                                            @Min(1)
                                                            @Max(Long.MAX_VALUE) Long orderId) {
        List<OrderProduct> orderProducts = orderService.getOrderProducts(orderId);
        List<Map<String, String>> products = new ArrayList<>();

        for (OrderProduct orderProduct : orderProducts) {
            Map<String, String> productProperties = new HashMap<>();
            productProperties.put("id", orderProduct.getProduct().getId().toString());
            productProperties.put("name", orderProduct.getProduct().getName());
            productProperties.put("imageUrl", orderProduct.getProduct().getImageUrl());
            productProperties.put("price", orderProduct.getSellingPrice().toString());
            productProperties.put("quantity", orderProduct.getQuantity().toString());
            productProperties.put("units", orderProduct.getProduct().getMeasureUnits().getName());
            productProperties.put("amount", orderProduct.calculateTotalSellingPrice().toString());
            products.add(productProperties);
        }
        return products;
    }

    /**
     * Updates the status of an order.
     *
     * @param orderStatusDto the dto with a new status information.
     */
    @PostMapping("/orders/status/change")
    public void changeOrderStatus(final @RequestBody @Validated OrderStatusDto orderStatusDto) {
        logger.info("A update order status update request " + orderStatusDto);
        orderService.updateOrderStatus(orderStatusDto);
        logger.info("An order status updated.");
    }

    /**
     * Sends the revenue report.
     *
     * @param periodDto the dto with a time period and an interval.
     * @return the revenue report.
     */
    @PostMapping("/reports/revenue")
    public List<Map<String, String>> sendRevenues(final @RequestBody @Validated RevenuePeriodDto periodDto) {
        logger.info("A revenue report request " + periodDto);
        List<Map<String, String>> result = new ArrayList<>();

        if (periodDto.getInterval().equals("weekly")) {
            orderService.getWeeklyRevenues(periodDto).forEach((k, v) -> {
                Map<String, String> map = new HashMap<>();
                int weekNumber = (k.getDayOfYear() + 7 - 1) / 7;
                map.put("week", k.getYear() + " Week " + weekNumber);
                map.put("revenue", v.toString());
                result.add(map);
            });
            logger.info("Weekly revenues have been calculated.");
        } else {
            orderService.getMonthlyRevenues(periodDto).forEach((k, v) -> {
                Map<String, String> map = new HashMap<>();
                map.put("month", k.toString());
                map.put("revenue", v.toString());
                result.add(map);
            });
            logger.info("Monthly revenues have been calculated.");
        }
        return result;
    }

}

