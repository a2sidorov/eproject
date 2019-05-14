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

package dev.a2.estore.service;

import dev.a2.estore.model.*;
import dev.a2.estore.model.Order;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Testing PdfService")
@ExtendWith(MockitoExtension.class)
class PdfServiceTest {

    @Spy
    private static CompanyInfo companyInfo = new CompanyInfo();

    @InjectMocks
    private PdfService pdfService = new PdfServiceImpl();

    @BeforeAll
    static void initAll() {
        companyInfo.setName("ESTORE");
        companyInfo.setEmail("company@email.com");
        companyInfo.setWebsite("www.estore.com");
        companyInfo.setCountry("Country");
        companyInfo.setPostalCode("Postal code");
        companyInfo.setCity("City");
        companyInfo.setStreet("Street");
        companyInfo.setHouse("123");
    }


    @Nested
    @DisplayName("Testing createInvoice method")
    class createInvoiceTest {
        @DisplayName("when this method is called then a pdf file with an invoice is created")
        @Test
        void createInvoiceTest1() throws Exception {
            // given
            Order order = new Order();
            order.setId(1L);
            order.setOrderStatus(OrderStatus.AWAITING_DELIVERY);

            User user = new User();
            user.setFirstName("Firstnme");
            user.setLastName("Lastname");

            Country country = new Country();
            country.setName("Country");

            Address address = new Address();
            address.setCountry(country);
            address.setCity("City");
            address.setPostalCode("Postal code");
            address.setStreet("Street");
            address.setHouse("123");
            address.setApartment("1");
            List<Address> addresses = new ArrayList<>();
            addresses.add(address);
            user.setAddresses(addresses);
            order.setUser(user);

            Price purchasingPrice = new Price();
            purchasingPrice.setPrice(new BigDecimal(20000));

            List<Price> purchasingPrices = new ArrayList<>();
            purchasingPrices.add(purchasingPrice);


            Product product1 = new Product();
            product1.setName("Product1");
            product1.setPurchasingPrices(purchasingPrices);
            product1.setSellingPrice(new BigDecimal(30000));

            Product product2 = new Product();
            product2.setName("Product1");
            product2.setPurchasingPrices(purchasingPrices);
            product2.setSellingPrice(new BigDecimal(30000));

            OrderProductPK orderProductPK1 = new OrderProductPK();
            orderProductPK1.setProduct(product1);

            OrderProduct orderProduct1 = new OrderProduct();
            orderProduct1.setPk(orderProductPK1);
            orderProduct1.setSellingPrice(product1.getSellingPrice());
            orderProduct1.setQuantity(2);

            OrderProductPK orderProductPK2 = new OrderProductPK();
            orderProductPK2.setProduct(product2);

            OrderProduct orderProduct2 = new OrderProduct();
            orderProduct2.setPk(orderProductPK2);
            orderProduct2.setSellingPrice(product2.getSellingPrice());

            List<OrderProduct> orderProducts = new ArrayList<>();
            orderProducts.add(orderProduct1);
            orderProducts.add(orderProduct2);
            order.setOrderProducts(orderProducts);
            order.setTotalSellingPrice(order.calculateOrderPrice());


            // run
            File file =  pdfService.createInvoice(order);

            // assert
            try {
                byte[] fileContext = Files.readAllBytes(file.toPath());
                Path outputFile = Paths.get("invoice-test.pdf");
                Files.write(outputFile, fileContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

