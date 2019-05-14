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

package dev.a2.estore.dao;

import dev.a2.estore.dto.SearchOrdersDto;
import dev.a2.estore.model.Order;
import dev.a2.estore.model.OrderProduct;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This interface provides methods to manipulate the order entity.
 *
 * @author Andrei Sidorov
 */
public interface OrderDao {

    /**
     * Saves an order to a database.
     *
     * @param order the order.
     */
    void save(Order order);

    /**
     * Finds all orders.
     *
     * @return the list of orders.
     */
    List<Order> getAllOrders();

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
     * @param orderId the id of the order that needs to found.
     * @return the order.
     */
    Order findById(Long orderId);

    /**
     * Finds order-product entities by their oder id.
     *
     * @param orderId the id of the order whose order-product entities need to found.
     * @return the list of order-product entities.
     */
    List<OrderProduct> getOrderProducts(Long orderId);

    /**
     * Finds orders by their user id.
     *
     * @param userId the id of the user whose orders need to found.
     * @return the list of orders.
     */
    List<Order> getAllUserOrders(Long userId);

    /**
     * Updates an order.
     *
     * @param order the order that needs to be updated.
     */
    void update(Order order);

    /**
     * Finds orders that are made in a certain time period.
     *
     * @param startDateTime the start of the time period.
     * @param endDateTime the end of the time period.
     * @return the list of orders.
     */
    List<Order> getOrdersByTimePeroid(LocalDateTime startDateTime, LocalDateTime endDateTime);

}
