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

package dev.a2.estore.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * This class provides the dto of a product quantity during the order creation.
 *
 * @author Andrei Sidorov
 */
public class ProductQuantityDto {

    /**
     * The id of a product that needs to be added to an order.
     */
    @NotNull
    @Min(1)
    @Max(Long.MAX_VALUE)
    private Long productId;

    /**
     * The required quantity of a product.
     */
    @NotNull
    @Min(1)
    @Max(Integer.MAX_VALUE)
    private Integer quantity;

    /**
     * The total number of products in an order.
     */
    private Integer totalNumberOfProducts;

    /**
     * The price that is calculated by multiplying a product selling price and required quantity.
     */
    private BigDecimal totalSellingPrice;

    /**
     * The sum of total selling prices in an order.
     */
    private BigDecimal orderPrice;

    /* Getters ans setters */

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotalNumberOfProducts() {
        return totalNumberOfProducts;
    }

    public void setTotalNumberOfProducts(Integer totalNumberOfProducts) {
        this.totalNumberOfProducts = totalNumberOfProducts;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getTotalSellingPrice() {
        return totalSellingPrice;
    }

    public void setTotalSellingPrice(BigDecimal totalSellingPrice) {
        this.totalSellingPrice = totalSellingPrice;
    }

    @Override
    public String toString() {
        return "ProductQuantityDto{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                ", totalNumberOfProducts=" + totalNumberOfProducts +
                ", totalSellingPrice=" + totalSellingPrice +
                ", orderPrice=" + orderPrice +
                '}';
    }
}

