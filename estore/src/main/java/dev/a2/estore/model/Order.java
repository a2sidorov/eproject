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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This moodel represents an order that keeps information about a buyer and its bought products,
 * their quantities and prices. It plays a major role in the application.
 * At the beginning of the selling process, it is a shopping cart that allows users to add, remove
 * chosen products and change their quantities. At the checkout, information about
 * a buyer and a payment is added and an object is persisted to the database.
 * In addition, the order model provides shipping and payment statuses
 * to reflect the rest check points of the selling process.
 *
 * @author Andrei Sidorov
 */
@Entity
@Table(name = "orders")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "orderProducts")
public class Order {

    /**
     * The id of an order. Generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    /**
     * The time and date when an order was created.
     */
    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime;

    /**
     * The method of payment such as cash or a credit card.
     */
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    /**
     * The shipping method such as standard or express.
     */
    @Column(name = "shipping_method")
    private ShippingMethod shippingMethod;

    /**
     * The payment status of an order.
     */
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    /**
     * The status of an order.
     */
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    /**
     * The total selling price of an order.
     */
    @Column(name = "total_selling_price")
    private BigDecimal totalSellingPrice;

    /**
     * The total purchasing price of an order.
     */
    @Column(name = "total_purchasing_price")
    private BigDecimal totalPurchasingPrice;

    /**
     * The user who made an order.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The address of the user who made an order.
     */
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    /**
     * The order-product entities that keep track of products in an order.
     */
    @OneToMany(mappedBy = "pk.order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    /**
     * Calculates total number of products in an order.
     *
     * @return the total number of products in a norder.
     */
    public int calculateTotalNumberOfProducts() {
        return this.orderProducts.stream().mapToInt(OrderProduct::getQuantity).sum();
    }

    /**
     * Calculates the total selling price of an order.
     *
     * @return the total selling price of an order.
     */
    public BigDecimal calculateOrderPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (OrderProduct op : getOrderProducts()) {
            sum = sum.add(op.calculateTotalSellingPrice());
        }
        return sum.setScale(2, RoundingMode.HALF_EVEN);
    }

    /* Getters and setters */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getTotalSellingPrice() {
        return totalSellingPrice;
    }

    public void setTotalSellingPrice(BigDecimal totalSellingPrice) {
        this.totalSellingPrice = totalSellingPrice;
    }

    public BigDecimal getTotalPurchasingPrice() {
        return totalPurchasingPrice;
    }

    public void setTotalPurchasingPrice(BigDecimal totalPurchasingPrice) {
        this.totalPurchasingPrice = totalPurchasingPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", creationDateTime=" + creationDateTime +
                ", paymentMethod=" + paymentMethod +
                ", shippingMethod=" + shippingMethod +
                ", paymentStatus=" + paymentStatus +
                ", orderStatus=" + orderStatus +
                ", totalSellingPrice=" + totalSellingPrice +
                ", totalPurchasingPrice=" + totalPurchasingPrice +
                '}';
    }
}

