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

import dev.a2.estore.dao.OrderDao;
import dev.a2.estore.dto.OrderStatusDto;
import dev.a2.estore.dto.OrdersColumn;
import dev.a2.estore.dto.RevenuePeriodDto;
import dev.a2.estore.dto.SearchOrdersDto;
import dev.a2.estore.dto.SortDirection;
import dev.a2.estore.exception.TimePeriodException;
import dev.a2.estore.model.Order;
import dev.a2.estore.model.OrderProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides implementation for OrderService interface.
 *
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    /**
     * Injects OrderDao.
     */
    @Autowired
    private OrderDao orderDao;

    @Override
    public void save(final Order order) {
        order.setTotalSellingPrice(order.calculateOrderPrice());
        order.getOrderProducts().forEach(orderProduct -> orderProduct
                .setSellingPrice(orderProduct.getProduct().getSellingPrice()));
        BigDecimal totalPurchasingPrice =  order
                .getOrderProducts()
                .stream()
                .map(OrderProduct::calculateTotalPurchasingPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPurchasingPrice(totalPurchasingPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        orderDao.save(order);
    }

    @Override
    public List<Order> findOrdersByCriteria(final SearchOrdersDto searchOrdersDto) {
        List<Order> orders = orderDao.findOrdersByCriteria(searchOrdersDto);
        OrdersColumn column = searchOrdersDto.getSortBy();
        SortDirection sortDirection = searchOrdersDto.getSortDirection();

        if (column == OrdersColumn.ID && sortDirection == SortDirection.ASC) {
            orders.sort(Comparator.comparing(Order::getId));
        }

        if (column == OrdersColumn.DATE && sortDirection == SortDirection.ASC) {
                orders.sort(Comparator.comparing(Order::getId));
            orders.sort(Comparator.comparing(Order::getCreationDateTime));
        }

        if (column == OrdersColumn.DATE && sortDirection == SortDirection.DESC) {
            orders.sort(Comparator.comparing(Order::getCreationDateTime, Collections.reverseOrder()));
        }

        if (column == OrdersColumn.EMAIL && sortDirection == SortDirection.ASC) {
            orders.sort(Comparator.comparing(o -> o.getUser().getEmail()));
        }

        if (column == OrdersColumn.EMAIL && sortDirection == SortDirection.DESC) {
            orders.sort(Comparator.comparing(o -> o.getUser().getEmail(), Collections.reverseOrder()));
        }

        if (column == OrdersColumn.PRICE && sortDirection == SortDirection.ASC) {
            orders.sort(Comparator.comparing(Order::getTotalSellingPrice));
        }

        if (column == OrdersColumn.PRICE && sortDirection == SortDirection.DESC) {
            orders.sort(Comparator.comparing(Order::getTotalSellingPrice, Collections.reverseOrder()));
        }
        return orders;
    }

    @Override
    public Order findById(final Long id) {
        return orderDao.findById(id);
    }

    @Override
    public List<OrderProduct> getOrderProducts(final Long orderId) {
        return orderDao.getOrderProducts(orderId);
    }

    @Override
    public List<Order> getAllUserOrders(final Long userId) {
        return orderDao.getAllUserOrders(userId);
    }

    @Override
    public void updateOrderStatus(final OrderStatusDto orderStatusDto) {
        Order order = orderDao.findById(orderStatusDto.getOrderId());
        order.setOrderStatus(orderStatusDto.getOrderStatus());
        orderDao.update(order);
    }

    @Override
    public Map<YearMonth, BigDecimal> getMonthlyRevenues(final RevenuePeriodDto periodDto) {
        LocalDate startDate = periodDto.getStart();
        LocalDate endDate = periodDto.getEnd();

        if (!startDate.isBefore(endDate)) {
            throw new TimePeriodException();
        }

        YearMonth start = YearMonth.from(periodDto.getStart());
        YearMonth end = YearMonth.from(periodDto.getEnd());
        LocalDateTime startDateTime = periodDto.getStart().atStartOfDay();
        LocalDateTime endDateTime = periodDto.getEnd().atTime(LocalTime.MAX);
        List<Order> orders = orderDao.getOrdersByTimePeroid(startDateTime, endDateTime);
        Map<YearMonth, BigDecimal> result = new LinkedHashMap<>();
        BigDecimal monthlyRevenue = new BigDecimal(0);

        int i = 0;
        while (!end.equals(start)) {
            YearMonth orderMonth = null;

            if (i < orders.size()) {
                orderMonth = YearMonth.from(orders.get(i).getCreationDateTime());
            }

            if (orderMonth != null && orderMonth.equals(end)) {
                monthlyRevenue = monthlyRevenue.add(orders.get(i).getTotalSellingPrice()
                        .subtract(orders.get(i).getTotalPurchasingPrice()));
                i++;

            } else if (orderMonth != null && orderMonth.isAfter(end)) {
                i++;

            } else {
                result.put(end, monthlyRevenue);
                monthlyRevenue = new BigDecimal(0);
                end = end.minusMonths(1);
            }

        }
        result.put(end, monthlyRevenue);
        return result;

    }

    @Override
    public Map<LocalDate, BigDecimal> getWeeklyRevenues(final RevenuePeriodDto periodDto) {
        LocalDate startDate = periodDto.getStart();
        LocalDate endDate = periodDto.getEnd();

        if (!startDate.isBefore(endDate)) {
            throw new TimePeriodException();
        }

        if (!startDate.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            startDate = startDate.minusDays((long) startDate.getDayOfWeek().getValue() - 1);
        }

        if (!endDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            endDate = endDate.plusDays(7 - (long) endDate.getDayOfWeek().getValue());
        }
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<Order> orders = orderDao.getOrdersByTimePeroid(startDateTime, endDateTime);
        Map<LocalDate, BigDecimal> result = new LinkedHashMap<>();
        BigDecimal weeklyRevenue = new BigDecimal(0);

        int i = 0;
        while (!endDate.equals(startDate) && startDate.isBefore(endDate))   {
            LocalDate orderDate = null;
            LocalDate previousMonday = endDate.minusDays(7);

            if (i < orders.size()) {
                orderDate = LocalDate.from(orders.get(i).getCreationDateTime());
            }

            if (orderDate != null
                    && (orderDate.equals(endDate) || (orderDate.isBefore(endDate) && orderDate.isAfter(previousMonday)))) {

                weeklyRevenue = weeklyRevenue.add(orders.get(i).getTotalSellingPrice()
                        .subtract(orders.get(i).getTotalPurchasingPrice()));
                i++;
            } else if (orderDate != null && orderDate.isAfter(endDate)) {
                i++;
            } else {
                result.put(endDate, weeklyRevenue);
                weeklyRevenue = new BigDecimal(0);
                endDate = endDate.minusDays(7);
            }
        }
        result.put(endDate, weeklyRevenue);
        return result;
    }

}
