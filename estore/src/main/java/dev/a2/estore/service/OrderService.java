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

package dev.a2.estore.service;

import dev.a2.estore.dto.OrderStatusDto;
import dev.a2.estore.dto.RevenuePeriodDto;
import dev.a2.estore.dto.SearchOrdersDto;
import dev.a2.estore.model.Order;
import dev.a2.estore.model.OrderProduct;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

/**
 * This interface provides methods for managing orders.
 *
 * @author Andrei Sidorov
 */
public interface OrderService {

    /**
     * Saves an order.
     *
     * @param order the order that needs to be saved.
     */
    void save(Order order);

    /**
     * Finds orders by criteria.
     *
     * @param searchOrdersDto the dto that contains search criteria.
     * @return the list of orders.
     */
    List<Order> findOrdersByCriteria(SearchOrdersDto searchOrdersDto);

    /**
     * Finds an order by its id.
     *
     * @param orderId the id of the order that needs to be found.
     * @return an order.
     */
    Order findById(Long orderId);

    /**
     * Finds an order-product entity by an order id.
     *
     * @param orderId the id of an order.
     * @return the list of order-product entities.
     */
    List<OrderProduct> getOrderProducts(Long orderId);

    /**
     * Finds orders by a user id.
     *
     * @param userId the id of the user whoose orders need to be found.
     * @return the list of orders.
     */
    List<Order> getAllUserOrders(Long userId);

    /**
     * Updates an order status.
     *
     * @param orderStatusDto the dto with status information.
     */
    void updateOrderStatus(OrderStatusDto orderStatusDto);

    /**
     * Calculates monthly revenues for a certain time period.
     *
     * @param periodDto the dto with the time period information.
     * @return the map with monthly revenues.
     */
    Map<YearMonth, BigDecimal> getMonthlyRevenues(RevenuePeriodDto periodDto);

    /**
     * Calculates weekly revenues for a certain time period.
     *
     * @param periodDto the dto with the time period information.
     * @return the map with weekly revenues.
     */
    Map<LocalDate, BigDecimal> getWeeklyRevenues(RevenuePeriodDto periodDto);


}
