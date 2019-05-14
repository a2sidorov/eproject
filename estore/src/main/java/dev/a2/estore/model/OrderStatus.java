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

/**
 * The order statuses.
 *
 * @author Andrei Sidorov
 */
public enum OrderStatus {

    /**
     * The status is set when an order is awaiting a payment.
     */
    //AWAITING_PAYMENT("Awaiting payment"),

    /**
     * This status is set order's products are awaiting delivery.
     */
    AWAITING_DELIVERY("Awaiting delivery"),

    /**
     * This status is set when an order has been dispatched from a warehouse.
     */
    DISPATCHED("Dispatched"),

    /**
     * This status is set when an order has been delivered to a customer.
     */
    DELIVERED("Delivered");

    private final String value;

    OrderStatus(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
