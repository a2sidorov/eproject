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

package dev.a2.estore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This model provides relationship between the oder model and product model.
 *
 * @author Andrei Sidorov
 */
@Entity
@Table(name = "orders_products")
public class OrderProduct {

    /**
     * The primary key of this entity that composed of a product and an order.
     */
    @EmbeddedId
    @JsonIgnore
    private OrderProductPK pk;

    /**
     * The quantity of a product.
     */
    @Column(nullable = false)
    private Integer quantity = 1;

    /**
     * The selling price of a product.
     */
    @Column(name = "selling_price", nullable = false, updatable = false)
    private BigDecimal sellingPrice;

    /**
     * Constuctor.
     */
    public OrderProduct() {
        super();
    }

    /**
     * Constructor.
     *
     * @param order an order.
     * @param product a product.
     * @param quantity a product quantity.
     */
    public OrderProduct(final Order order, final Product product, final Integer quantity) {
        pk = new OrderProductPK();
        pk.setOrder(order);
        pk.setProduct(product);
        this.quantity = quantity;
    }

    /**
     * Returns a product from the embedded primary key.
     *
     * @return a product.
     */
    public Product getProduct() {
        return this.pk.getProduct();
    }

    /**
     * Calculates the total selling price of a product
     * by multiplying a product current selling price and its quantity in a order.
     *
     * @return the total selling price of a product.
     */
    public BigDecimal calculateTotalSellingPrice() {
        BigDecimal result = getProduct().getSellingPrice().multiply(new BigDecimal(getQuantity()));
        return result.setScale(2, RoundingMode.HALF_EVEN);
    }

    /**
     * Calculates the total purchasing price of a product
     * by multiplying a product current purchasing price and its quantity in an order.
     *
     * @return the total purchasing price of a product.
     */
    public BigDecimal calculateTotalPurchasingPrice() {
        return getProduct().getRecentPurchasingPrice().multiply(new BigDecimal(getQuantity()));
    }

    /* Getters and setters */

    public OrderProductPK getPk() {
        return pk;
    }

    public void setPk(OrderProductPK pk) {
        this.pk = pk;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pk == null) ? 0 : pk.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OrderProduct other = (OrderProduct) obj;
        if (pk == null) {
            if (other.pk != null) {
                return false;
            }
        } else if (!pk.equals(other.pk)) {
            return false;
        }
        return true;
    }

}
