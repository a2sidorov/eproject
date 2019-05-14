package dev.a2.estore.service;

import dev.a2.estore.dao.OrderDao;
import dev.a2.estore.dto.OrderStatusDto;
import dev.a2.estore.dto.RevenuePeriodDto;
import dev.a2.estore.dto.SearchOrdersDto;
import dev.a2.estore.exception.TimePeriodException;
import dev.a2.estore.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@DisplayName("Testing OrderService")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private OrderService orderService = new OrderServiceImpl();

    @Nested
    @DisplayName("Testing save method")
    class saveTest {
        @DisplayName("when this method is called then an order gets saved")
        @Test
        void saveTest1() {
            // given
            Price price = new Price();
            price.setPrice(new BigDecimal(1.01).setScale(2, RoundingMode.HALF_EVEN));
            List<Price> prices = new ArrayList<>();
            prices.add(price);

            // order product 1
            Product product1 = new Product();
            product1.setPurchasingPrices(prices);
            product1.setSellingPrice(new BigDecimal(2.01).setScale(2, RoundingMode.HALF_EVEN));

            OrderProductPK orderProductPK1 = new OrderProductPK();
            orderProductPK1.setProduct(product1);

            OrderProduct orderProduct1 = new OrderProduct();
            orderProduct1.setPk(orderProductPK1);
            orderProduct1.setQuantity(2);

            // order product 2
            Product product2 = new Product();
            product2.setPurchasingPrices(prices);
            product2.setSellingPrice(new BigDecimal(2.01).setScale(2, BigDecimal.ROUND_HALF_EVEN));

            OrderProductPK orderProductPK2 = new OrderProductPK();
            orderProductPK2.setProduct(product2);

            OrderProduct orderProduct2 = new OrderProduct();
            orderProduct2.setPk(orderProductPK2);
            orderProduct2.setQuantity(2);

            List<OrderProduct> orderProducts = new ArrayList<>();
            orderProducts.add(orderProduct1);
            orderProducts.add(orderProduct2);

            Order order = new Order();
            order.setOrderProducts(orderProducts);

            // run
            orderService.save(order);

            // assert
            assertEquals(new BigDecimal(8.04).setScale(2, RoundingMode.HALF_EVEN),
                    order.calculateOrderPrice());
            assertEquals(new BigDecimal(2.01).setScale(2, RoundingMode.HALF_EVEN),
                    orderProduct1.getSellingPrice());
            assertEquals(new BigDecimal(2.01).setScale(2, RoundingMode.HALF_EVEN),
                    orderProduct2.getSellingPrice());
            assertEquals(new BigDecimal(4.04).setScale(2, RoundingMode.HALF_EVEN),
                    order.getTotalPurchasingPrice());
            assertEquals(new BigDecimal(8.04).setScale(2, RoundingMode.HALF_EVEN),
                    order.getTotalSellingPrice());
            verify(orderDao, times(1)).save(any(Order.class));
        }
    }

    @Nested
    @DisplayName("Testing findOrdersByCriteria method")
    class getAllOrdersTest {
        @DisplayName("when this method called then method OrderDao.findOrdersByCriteria gets called")
        @Test
        void getAllOrdersTest1() {
            // given
            SearchOrdersDto searchOrdersDto = new SearchOrdersDto();


            // run
            orderService.findOrdersByCriteria(searchOrdersDto);

            //assert
            verify(orderDao, times(1)).findOrdersByCriteria(searchOrdersDto);
        }
    }

    @Nested
    @DisplayName("Testing findById method")
    class findByIdTest {
        @DisplayName("when this method is called then method method orderDao.findById gets called")
        @Test
        void findByIdTest1() {
            // given
            Long orderId = 1L;

            // run
            orderService.findById(orderId);

            //assert
            verify(orderDao, times(1)).findById(orderId);

        }
    }

    @Nested
    @DisplayName("Testing getOrderProducts method")
    class getOrderProductsTest {
        @DisplayName("when this method is called then method orderDao.getOrderProducts gets called")
        @Test
        void findByIdTest1() {
            // given
            Long orderId = 1L;

            // run
            orderService.getOrderProducts(orderId);

            //assert
            verify(orderDao, times(1)).getOrderProducts(orderId);

        }
    }

    @Nested
    @DisplayName("Testing updateOrderStatus method")
    class updateOrderStatusTest {
        @DisplayName("when this method is called then an order's status gets updated")
        @Test
        void updateOrderStatusTest1() {
            // given
            Order order = new Order();

            OrderStatusDto orderStatusDto = new OrderStatusDto();
            orderStatusDto.setOrderId(1L);
            orderStatusDto.setOrderStatus(OrderStatus.DELIVERED);

            when(orderDao.findById(1L)).thenReturn(order);

            // run
            orderService.updateOrderStatus(orderStatusDto);

            // assert
            assertEquals(OrderStatus.DELIVERED, order.getOrderStatus());
            verify(orderDao, times(1)).update(order);

        }
    }

    @Nested
    @DisplayName("Testing getMonthlyRevenues method")
    class getMonthlyRevenuesTest {
        @DisplayName("when this method is called " +
                "then the list of monthly revenues are returned")
        @Test
        void getMonthlyRevenuesTest1() {
            // given
            Price price = new Price();
            price.setPrice(new BigDecimal(1));
            List<Price> prices = new ArrayList<>();
            prices.add(price);

            // order product 1
            Product product1 = new Product();
            product1.setPurchasingPrices(prices);
            product1.setSellingPrice(new BigDecimal(2));

            OrderProductPK orderProductPK1 = new OrderProductPK();
            orderProductPK1.setProduct(product1);

            OrderProduct orderProduct1 = new OrderProduct();
            orderProduct1.setPk(orderProductPK1);
            orderProduct1.setQuantity(2);

            // order product 2
            Product product2 = new Product();
            product2.setPurchasingPrices(prices);
            product2.setSellingPrice(new BigDecimal(2));

            OrderProductPK orderProductPK2 = new OrderProductPK();
            orderProductPK2.setProduct(product2);

            OrderProduct orderProduct2 = new OrderProduct();
            orderProduct2.setPk(orderProductPK2);
            orderProduct2.setQuantity(2);

            List<OrderProduct> orderProducts = new ArrayList<>();
            orderProducts.add(orderProduct1);
            orderProducts.add(orderProduct2);

            Order order = new Order();
            order.setOrderProducts(orderProducts);
            order.setCreationDateTime(LocalDateTime.now());
            order.setTotalPurchasingPrice(new BigDecimal(20));
            order.setTotalSellingPrice(new BigDecimal(25));

            List<Order> orders = new ArrayList<>();
            orders.add(order);

            RevenuePeriodDto revenuePeriodDto = new RevenuePeriodDto();
            revenuePeriodDto.setStart(LocalDate.parse("2019-01-01"));
            revenuePeriodDto.setEnd(LocalDate.now());
            revenuePeriodDto.setInterval("month");

            when(orderDao.getOrdersByTimePeroid(any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(orders);

            // run
            Map<YearMonth, BigDecimal> result = orderService.getMonthlyRevenues(revenuePeriodDto);

            // assert
            assertEquals(new BigDecimal(5), result.get(YearMonth.now()));
        }


        @DisplayName("when a start date is not before an end date " +
                "then exception TimePeriodException is thrown")
        @Test
        void getMonthlyRevenuesTest3() {
            // given
            RevenuePeriodDto revenuePeriodDto = new RevenuePeriodDto();
            revenuePeriodDto.setStart(LocalDate.now());
            revenuePeriodDto.setEnd(LocalDate.now());
            revenuePeriodDto.setInterval("month");

            // run

            // run and assert
            assertThrows(TimePeriodException.class, () -> {
                orderService.getMonthlyRevenues(revenuePeriodDto);
            });
        }
    }

    @Nested
    @DisplayName("Testing getWeeklyRevenues method")
    class getWeeklyRevenuesTest {
        @DisplayName("when this method is called " +
                "then the list of weekly revenues are returned")
        @Test
        void getMonthlyRevenuesTest2() {
            // given
            Price price = new Price();
            price.setPrice(new BigDecimal(1));
            List<Price> prices = new ArrayList<>();
            prices.add(price);

            // order product 1
            Product product1 = new Product();
            product1.setPurchasingPrices(prices);
            product1.setSellingPrice(new BigDecimal(2));

            OrderProductPK orderProductPK1 = new OrderProductPK();
            orderProductPK1.setProduct(product1);

            OrderProduct orderProduct1 = new OrderProduct();
            orderProduct1.setPk(orderProductPK1);
            orderProduct1.setQuantity(2);

            // order product 2
            Product product2 = new Product();
            product2.setPurchasingPrices(prices);
            product1.setSellingPrice(new BigDecimal(2));

            OrderProductPK orderProductPK2 = new OrderProductPK();
            orderProductPK2.setProduct(product2);

            OrderProduct orderProduct2 = new OrderProduct();
            orderProduct2.setPk(orderProductPK2);
            orderProduct2.setQuantity(2);

            List<OrderProduct> orderProducts = new ArrayList<>();
            orderProducts.add(orderProduct1);
            orderProducts.add(orderProduct2);

            Order order = new Order();
            order.setOrderProducts(orderProducts);
            order.setCreationDateTime(LocalDateTime.now());
            order.setTotalPurchasingPrice(new BigDecimal(20));
            order.setTotalSellingPrice(new BigDecimal(25));

            List<Order> orders = new ArrayList<>();
            orders.add(order);

            RevenuePeriodDto revenuePeriodDto = new RevenuePeriodDto();
            revenuePeriodDto.setStart(LocalDate.parse("2019-01-01"));
            revenuePeriodDto.setEnd(LocalDate.now());
            revenuePeriodDto.setInterval("month");

            when(orderDao.getOrdersByTimePeroid(any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(orders);

            // run
            Map<LocalDate, BigDecimal> result = orderService.getWeeklyRevenues(revenuePeriodDto);

            // assert
            LocalDate today = LocalDate.now();
            LocalDate thisSunday = today.plusDays(7 - today.getDayOfWeek().getValue());
            assertEquals(new BigDecimal(5), result.get(thisSunday));

        }

        @DisplayName("when a start date is not before an end date " +
                "then exception TimePeriodException is thrown")
        @Test
        void getWeeklyRevenuesTest3() {
            // given
            RevenuePeriodDto revenuePeriodDto = new RevenuePeriodDto();
            revenuePeriodDto.setStart(LocalDate.now());
            revenuePeriodDto.setEnd(LocalDate.now());
            revenuePeriodDto.setInterval("month");

            // run

            // run and assert
            assertThrows(TimePeriodException.class, () -> {
                orderService.getWeeklyRevenues(revenuePeriodDto);
            });
        }
    }
}


