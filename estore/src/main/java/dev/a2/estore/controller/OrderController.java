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

package dev.a2.estore.controller;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import dev.a2.estore.dto.CheckoutDto;
import dev.a2.estore.dto.ProductQuantityDto;
import dev.a2.estore.dto.RevenuePeriodDto;
import dev.a2.estore.dto.SearchOrdersDto;
import dev.a2.estore.exception.PaymentException;
import dev.a2.estore.exception.ProductReserveException;
import dev.a2.estore.model.Address;
import dev.a2.estore.model.CompanyInfo;
import dev.a2.estore.model.Order;
import dev.a2.estore.model.OrderProduct;
import dev.a2.estore.model.OrderStatus;
import dev.a2.estore.model.PaymentMethod;
import dev.a2.estore.model.PaymentStatus;
import dev.a2.estore.model.User;
import dev.a2.estore.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * This class provides mapping for order related requests.
 *
 * @author Andrei Sidorov
 */
@Controller
@Validated
@SessionAttributes({"order", "executorService"})
public class OrderController {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(OrderController.class);

    /**
     * Injects bean CategoryService.
     */
    @Autowired
    private CategoryService categoryService;

    /**
     * Injects bean UserService.
     */
    @Autowired
    private UserService userService;

    /**
     * Injects bean ProductService.
     */
    @Autowired
    private ProductService  productService;

    /**
     * Injects bean OrderService.
     */
    @Autowired
    private OrderService orderService;

    /**
     * Injects bean MessageSource.
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Injects bean PaymentService.
     */
    @Autowired
    private PaymentServiceImpl paymentService;

    /**
     * Injects JmsService.
     */
    @Autowired
    private JmsService jmsService;

    /**
     * Injects bean EmailService.
     */
    @Autowired
    private EmailService emailService;

    /**
     * Injects bean PdfService.
     */
    @Autowired
    private PdfService pdfService;

    /**
     * Injects bean CompanyInfo.
     */
    @Autowired
    private CompanyInfo companyInfo;

    /**
     * Product reserve time in seconds before checkout. Specified in 'application.aproperties'.
     */
    @Value("${product.reserve.time}")
    private Long productReserveTime;

    /**
     * Initializes the shopping cart.
     *
     * @return order.
     */
    @ModelAttribute("order")
    public Order initOrder() {
        return new Order();
    }

    /**
     * Initializes the executor for the product reserve timer.
     *
     * @return the executor.
     */
    @ModelAttribute("executorService")
    public ScheduledExecutorService initScheduler() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * The maximum products in the list of top-selling-products.
     */
    @Value("${top.products.max.length}")
    private int topProductsLength;

    /**
     * Generates the home page.
     *
     * @param  model the model
     * @return the view name 'home'.
     */
    @GetMapping("/")
    public String showHomePage(final Model model) {
        model.addAttribute("categories", categoryService.getTopLevelCategories());
        model.addAttribute("order");
        return "home";
    }

    /**
     * Generates the cart page.
     *
     * @param  model the model
     * @param session the user session
     * @param order the user order
     * @return the view name 'order'.
     */
    @GetMapping("/order")
    public String showCart(final Model model,
                           final HttpSession session,
                           final @SessionAttribute("order") Order order) {
        Future timerTask = (Future) session.getAttribute("timerTask");
        List<OrderProduct> orderProducts = order.getOrderProducts();

        // Unreserves products if the products were reserved.
        if (timerTask != null && !timerTask.isDone()) {
            productService.unreserveProducts(orderProducts);
            timerTask.cancel(true);
            logger.info("Products have been unreserved.");
        }

        // Refetches products in case there were changes to available quantities.
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.getPk().setProduct(productService.findById(orderProduct.getProduct().getId()));
        }
        model.addAttribute("orderProducts", order.getOrderProducts());
        model.addAttribute("productQuantityDto", new ProductQuantityDto());
        return "order";
    }

    /**
     * Removes a product from the cart.
     *
     * @param order the user order.
     * @param  productId the id of the product that needs to be removed from the cart.
     * @return redirects to view 'order'.
     */
    @GetMapping("/order/remove")
    public String removeProductFromCart(final @SessionAttribute("order") Order order,
                                        final @RequestParam @NotNull @Min(1) @Max(Long.MAX_VALUE) Long  productId) {
        logger.info("A product remove request: Product id '" + productId + "'");
        order.getOrderProducts().removeIf(orderProduct -> orderProduct.getProduct().getId().equals(productId));
        logger.info("A product has been removed from the cart.");
        return "redirect:/order";
    }

    /**
     * Reserves products in the cart.
     *
     * @param model the model.
     * @param order the user order.
     * @param executorService the executor service for running the product reserve timer.
     * @param session the user session.
     * @param redirectAttributes the attributes for flash messages.
     * @return view 'reserve'.
     */
    @GetMapping("/checkout/reserve")
    public String checkout(final Model model,
                           final @SessionAttribute("order") Order order,
                           final @SessionAttribute("executorService") ScheduledExecutorService executorService,
                           final HttpSession session,
                           final RedirectAttributes redirectAttributes) {
        if (order.getOrderProducts().isEmpty()) {
            return "redirect:/home";
        }
        List<OrderProduct> orderProducts = order.getOrderProducts();
        Future timerTask = (Future) session.getAttribute("timerTask");
        LocalDateTime startTime = (LocalDateTime) session.getAttribute("startTime");

        // Reserves products.
        if (timerTask == null || timerTask.isDone()) {
            try {
                productService.reserveProducts(orderProducts);
                logger.info("Products have been reserved.");
            } catch (ProductReserveException e) {
                String error = messageSource.getMessage("product.reserve.failure", null, Locale.US);
                redirectAttributes.addFlashAttribute("error", error);
                return "redirect:/order";
            }

            // Sets the task to unreserve products for the timer when the product reserve time runs out.
            Runnable unreserveProducts = () -> {
                productService.unreserveProducts(orderProducts);
                logger.info("Time for completing purchase has run out.");
            };

            // Initializes the timer.
            timerTask = executorService.schedule(unreserveProducts, productReserveTime, TimeUnit.SECONDS);
            session.setAttribute("timerTask", timerTask);
            startTime = LocalDateTime.now();
            session.setAttribute("startTime", startTime);
        }
        // Calculates how many seconds passed since a reserve was made.
        Duration timeLeft = Duration.between(startTime, LocalDateTime.now());
        Long secondsPassed = timeLeft.getSeconds() < productReserveTime ? timeLeft.getSeconds() : productReserveTime;

        // Refetches order products to refresh available quantitiea.
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.getPk().setProduct(productService.findById(orderProduct.getProduct().getId()));
        }
        model.addAttribute("productReserveTime", productReserveTime.toString());
        model.addAttribute("secondsPassed", secondsPassed.toString());
        model.addAttribute("orderProducts", order.getOrderProducts());
        String message = messageSource.getMessage("product.reserve.success", null, Locale.US);
        model.addAttribute("message", message);
        return "reserve";
    }

    /**
     * Generates the checkout page.
     *
     * @param model the model.
     * @param startTime the time when products were reserved.
     * @param timeZone the user time zone
     * @param authentication the current user credentials.
     * @param redirectAttributes the attributes for flash messages.
     * @return view 'checkout'.
     */
    @GetMapping("/checkout/payment")
    public String showCheckoutPage(final Model model,
                                   final @SessionAttribute("startTime") LocalDateTime startTime,
                                   final TimeZone timeZone,
                                   final Authentication authentication,
                                   final RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(authentication.getName());

        // Calculates the list years for the expiratation date in the credit card form.
        Year currentYear = Year.now(timeZone.toZoneId());
        List<Integer> expirationYears = IntStream.range(currentYear.getValue(), currentYear.getValue() + 10)
                .boxed()
                .collect(Collectors.toList());

        // Calculates the amount of seconds since reserve were made.
        Duration timePassed = Duration.between(startTime, LocalDateTime.now());
        Long secondsPassed = timePassed.getSeconds() < productReserveTime ? timePassed.getSeconds() : productReserveTime;

        // Redirects request to the cart page if the product reserve time has run out.
        if (secondsPassed.equals(productReserveTime)) {
            String error = messageSource.getMessage("product.reserve.ended", null, Locale.US);
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/order";
        }
        model.addAttribute("checkoutDto", new CheckoutDto());
        model.addAttribute("expirationYears", expirationYears);
        model.addAttribute("productReserveTime", productReserveTime);
        model.addAttribute("secondsPassed", secondsPassed.toString());
        model.addAttribute("addresses", user.getAddresses());
        return "checkout";
    }

    /**
     * Buys products.
     *
     * @param order the user order.
     * @param timerTask the timer task.
     * @param checkoutDto the dto with payment and shipping information.
     * @param startTime the time when products were reserved.
     * @param authentication the current user credentials.
     * @param redirectAttributes the attributes for flash messages.
     * @return redirects to view 'message'.
     */
    @PostMapping("/checkout")
    public String buyProducts(final @SessionAttribute("order") Order order,
                              final @SessionAttribute("timerTask") Future timerTask,
                              final @ModelAttribute("checkoutDto") @Validated CheckoutDto checkoutDto,
                              final @SessionAttribute("startTime") LocalDateTime startTime,
                              final SessionStatus sessionStatus,
                              final Authentication authentication,
                              final RedirectAttributes redirectAttributes) {
        logger.info("An order checkout request " + checkoutDto);
        User user = userService.findByEmail(authentication.getName());
        Duration timePassed = Duration.between(startTime, LocalDateTime.now());
        Long secondsPassed = timePassed.getSeconds() < productReserveTime ? timePassed.getSeconds() : productReserveTime;

        // checking that reserve time is not run out
        if (secondsPassed.equals(productReserveTime)) {
            String error = messageSource.getMessage("product.reserve.ended", null, Locale.US);
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/order";
        }

        if (checkoutDto.getPaymentMethod() == PaymentMethod.CARD) {
            try {
                paymentService.charge(order, checkoutDto);
                order.setPaymentMethod(PaymentMethod.CARD);
                order.setPaymentStatus(PaymentStatus.PAID);
            } catch (PaymentException e) {
                String error = messageSource.getMessage("product.payment.failure", null, Locale.US);
                redirectAttributes.addFlashAttribute("error", error);
                return "redirect:/order";
            }
        } else {
            order.setPaymentMethod(PaymentMethod.CASH);
            order.setPaymentStatus(PaymentStatus.AWAITING_PAYMENT);
        }
        productService.buyProducts(order.getOrderProducts());
        timerTask.cancel(true);


        Address address = user
                .getAddresses()
                .stream()
                .filter(a -> a.getId().equals(checkoutDto.getAddressId()))
                .findFirst()
                .orElse(null);
        order.setAddress(address);
        order.setOrderStatus(OrderStatus.AWAITING_DELIVERY);
        order.setShippingMethod(checkoutDto.getShippingMethod());
        order.setCreationDateTime(LocalDateTime.now());
        order.setUser(user);
        orderService.save(order);

        // creating and sending an order invoice to a user email
        try {
            File invoicePdfFile = pdfService.createInvoice(order);
            String subject = "Invoice from " + companyInfo.getName() + " INV" + order.getId();

            emailService.sendInvoice(user.getEmail(), subject, invoicePdfFile);
        } catch (MessagingException e) {
            logger.error("Invoice sending failed", e);
        } catch (Exception e1) {
            logger.error("Invoice creation failed", e1);
        }

        // Sends the updated list of top-selling-products to the billboard application.
        jmsService.send(productService.getTopSellingProducts(topProductsLength));
        logger.info("Products have been purchased.");

        String message = messageSource.getMessage("product.checkout.success", null, Locale.US);
        redirectAttributes.addFlashAttribute("message", message);
        sessionStatus.setComplete();
        return "redirect:/message";
    }

    /**
     * Generates the message view after checkout.
     *
     * @param model the model.
     * @return view 'message'.
     */
    @GetMapping("/message")
    public String showMessage(final Model model) {
        model.addAttribute("order", new Order());
        return "message";
    }

    /**
     * Generates the orders page for clients.
     *
     * @param model the model.
     * @param authentication the user credentials.
     * @return view 'my-orders'.
     */
    @GetMapping("/my-orders")
    public String showUserOrders(final Model model,
                                 final Authentication authentication) {
        SearchOrdersDto searchOrdersDto = new SearchOrdersDto();
        searchOrdersDto.setUserEmail(authentication.getName());
        model.addAttribute("orders", orderService.findOrdersByCriteria(searchOrdersDto));
        model.addAttribute("searchOrdersDto", searchOrdersDto);
        model.addAttribute("orderStatuses", OrderStatus.values());
        return "my-orders";
    }

    /**
     * Finds orders by criteria for clients.
     *
     * @param model the model.
     * @param authentication the user credentials.
     * @param searchOrdersDto the dto with search criteria.
     * @return the list of user orders.
     */
    @PostMapping("/my-orders/find")
    public String findUserOrders(final Model model,
                                 final Authentication authentication,
                                 final SearchOrdersDto searchOrdersDto) {
        logger.info("A find orders request " + searchOrdersDto);
        searchOrdersDto.setUserEmail(authentication.getName());
        model.addAttribute("orders", orderService.findOrdersByCriteria(searchOrdersDto));
        model.addAttribute("orderStatuses", OrderStatus.values());
        model.addAttribute("searchOrdersDto", searchOrdersDto);
        return "my-orders";
    }

    /**
     * Repeats an order.
     *
     * @param  orderId the id of the order that needs to be repeated.
     * @param order the user order.
     * @return redirects to view 'order'.
     */
    @GetMapping("/order/repeat/{orderId}")
    public String repeatOrder(final @PathVariable @NotNull @Min(1) @Max(Long.MAX_VALUE) Long orderId,
                              final @SessionAttribute("order") Order order) {
        List<OrderProduct> orderProducts = orderService.getOrderProducts(orderId);
        order.setOrderProducts(orderProducts);
        return "redirect:/order";
    }

    /**
     * Generates orders page.
     *
     * @param model the model.
     * @return view 'orders'.
     */
    @GetMapping("/orders")
    public String showAllOrders(final Model model) {
        SearchOrdersDto searchOrdersDto = new SearchOrdersDto();
        model.addAttribute("orders", orderService.findOrdersByCriteria(searchOrdersDto));
        model.addAttribute("orderStatuses", OrderStatus.values());
        model.addAttribute("searchOrdersDto", searchOrdersDto);
        return "orders";
    }

    /**
     * Finds oders by citeria.
     *
     * @param model the model.
     * @param searchOrdersDto the dto with search criteria.
     * @param result the holder for binding validation errors.
     * @return view 'orders' with the list of orders.
     */
    @PostMapping("/orders/find")
    public String findOrders(final Model model,
                             final @Validated SearchOrdersDto searchOrdersDto,
                             final BindingResult result) {
        logger.info("A find orders request " + searchOrdersDto);
        model.addAttribute("orders", orderService.findOrdersByCriteria(searchOrdersDto));
        model.addAttribute("orderStatuses", OrderStatus.values());
        model.addAttribute("searchOrdersDto", searchOrdersDto);
        return "orders";
    }

    /**
     * Generates the revenue report page.
     *
     * @param model the model.
     * @return view 'revenue'.
     */
    @GetMapping("/reports/revenue")
    public String showRevenuePage(final Model model) {
        model.addAttribute("periodDto", new RevenuePeriodDto());
        return "revenue";
    }

}
