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
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * This class provides implementation for OrderDao interface.
 *
 * @author Andrei Sidorov
 */
@Repository
public class OrderDaoImpl implements OrderDao {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(OrderDao.class);
    /**
     * Injects bean SessionFactory.
     */
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(final Order order) {
        sessionFactory.getCurrentSession().save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        @SuppressWarnings("unchecked")
        TypedQuery<Order> query = sessionFactory
                .getCurrentSession()
                .createQuery("FROM Order o ORDER BY o.creationDateTime DESC");
        logger.info("Fetched all " + query.getResultList());
        return query.getResultList();
    }

    @Override
    public List<Order> findOrdersByCriteria(final SearchOrdersDto searchOrdersDto) {
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (searchOrdersDto.getStartDate() != null) {
            startDateTime = searchOrdersDto.getStartDate().atStartOfDay();
        }

        if (searchOrdersDto.getEndDate() != null) {
            endDateTime = searchOrdersDto.getEndDate().atTime(LocalTime.MAX);
        }
        @SuppressWarnings("unchecked")
        TypedQuery<Order> query = sessionFactory
                .getCurrentSession()
                .createQuery("FROM Order o JOIN FETCH o.user user " +
                                        "WHERE (:id is null or o.id = :id) " +
                                        "AND (:startDateTime is null or o.creationDateTime >= :startDateTime) " +
                                        "AND (:endDateTime is null or o.creationDateTime <= :endDateTime) " +
                                        "AND (:email is null or :email is '' or user.email = :email) " +
                                        "AND (:minPrice is null or o.totalSellingPrice >= :minPrice) " +
                                        "AND (:maxPrice is null or o.totalSellingPrice <= :maxPrice) " +
                                        "AND (:status is null  or o.orderStatus = :status) " +
                                        "ORDER BY o.id DESC");
        query.setParameter("id", searchOrdersDto.getOrderId());
        query.setParameter("startDateTime", startDateTime);
        query.setParameter("endDateTime", endDateTime);
        query.setParameter("email", searchOrdersDto.getUserEmail());
        query.setParameter("minPrice", searchOrdersDto.getMinPrice());
        query.setParameter("maxPrice", searchOrdersDto.getMaxPrice());
        query.setParameter("status", searchOrdersDto.getOrderStatus());
        logger.info("Fetched by critera " + searchOrdersDto + " result: " + query.getResultList());
        return query.getResultList();
    }

    @Override
    public Order findById(final Long orderId) {
        Order order =  sessionFactory.getCurrentSession().get(Order.class, orderId);
        logger.info("Fetched by order id '" + orderId + "' " + order);
        return order;
    }

    @Override
    public List<OrderProduct> getOrderProducts(final Long orderId) {
        @SuppressWarnings("unchecked")
        TypedQuery<OrderProduct> query = sessionFactory
                .getCurrentSession()
                .createQuery("FROM OrderProduct op JOIN FETCH op.pk.product " +
                                        "WHERE op.pk.order.id = :id");
        query.setParameter("id", orderId);
        return query.getResultList();
    }

    @Override
    public List<Order> getAllUserOrders(final Long userId) {
        @SuppressWarnings("unchecked")
        TypedQuery<Order> query = sessionFactory
                .getCurrentSession()
                .createQuery("FROM Order o " +
                                        "WHERE o.user.id = :id " +
                                        "ORDER BY o.creationDateTime DESC");
        query.setParameter("id", userId);
        logger.info("Fetched by user id '" + userId + "' " + query.getResultList());
        return query.getResultList();
    }

    @Override
    public void update(final Order order) {
        sessionFactory.getCurrentSession().update(order);
    }

    @Override
    public List<Order> getOrdersByTimePeroid(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        @SuppressWarnings("unchecked")
        TypedQuery<Order> query = sessionFactory
                .getCurrentSession()
                .createQuery("FROM Order o " +
                                        "WHERE o.creationDateTime " +
                                        "BETWEEN :startDateTime " +
                                        "AND :endDateTime " +
                                        "ORDER BY o.creationDateTime DESC");
        query.setParameter("startDateTime", startDateTime);
        query.setParameter("endDateTime", endDateTime);
        logger.info("Fetched by time period from '" + startDateTime + "' to" + endDateTime + " " +
                query.getResultList());
        return query.getResultList();
    }

}
