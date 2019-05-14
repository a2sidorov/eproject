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

package dev.a2.estore.dto;

import dev.a2.estore.model.OrderStatus;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This class provides the dto of search criteria for finding orders.
 *
 * @author Andrei Sidorov
 */
public class SearchOrdersDto {

    /**
     * The id of a searched order.
     */
    @Min(1)
    @Max(Long.MAX_VALUE)
    private Long orderId;

    /**
     * The start date of a time period. Inititally set to minus two weeks from the present day.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate = LocalDate.now().minusWeeks(2);

    /**
     * The end date of a time period. Initially set to the present day.
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate = LocalDate.now();

    /**
     * The email of a user whose order is searched.
     */
    private String userEmail;

    /**
     * The minimum price of a searched order.
     */
    @Min(0)
    private BigDecimal minPrice;

    /**
     * The maximum price of a searched order.
     */
    @Min(0)
    private BigDecimal maxPrice;

    /**
     * The status of a searched order.
     */
    private OrderStatus orderStatus = null;

    /**
     * The column of the orders whose values need to be sorted.
     */
    private OrdersColumn sortBy = OrdersColumn.ID;

    /**
     * The sort direction. Either ascending ot descending.
     */
    private SortDirection sortDirection = SortDirection.DESC;

    /* Getters and setters */

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrdersColumn getSortBy() {
        return sortBy;
    }

    public void setSortBy(OrdersColumn sortBy) {
        this.sortBy = sortBy;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    @Override
    public String toString() {
        return "SearchOrdersDto{" +
                "orderId=" + orderId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", userEmail='" + userEmail + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", orderStatus=" + orderStatus +
                ", sortBy=" + sortBy +
                ", sortDirection=" + sortDirection +
                '}';
    }
}
