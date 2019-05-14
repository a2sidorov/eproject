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

package dev.a2.estore.test.OrderController;

import dev.a2.estore.test.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Testing class OrderController")
@SpringJUnitWebConfig(TestConfig.class)
class OrderControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .defaultRequest(get("/").with(user("manager@mail.dev").roles("MANAGER")))
                .apply(springSecurity())
                .build();
    }

    @DisplayName("when request GET '/orders' then page 'orders' returns")
    @Test
    void getOrdersPage() throws Exception {
        this.mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"));
    }

    @DisplayName("when request POST '/orders/find' with valid data " +
            "then orders are returned")
    @Test
    void findOrdersRequest1() throws Exception {
        this.mockMvc
                .perform(
                        post("/orders/find")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors());
    }

    @DisplayName("when request POST '/orders/find' with order id 'a' " +
            "then view orders with errors returns")
    @Test
    void findOrdersRequest2() throws Exception {
        this.mockMvc
                .perform(
                        post("/orders/find")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("orderId", "a")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());
    }

    @DisplayName("when request POST '/orders/find' with order id '0' " +
            "then view orders with errors returns")
    @Test
    void findOrdersRequest3() throws Exception {
        this.mockMvc
                .perform(
                        post("/orders/find")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("orderId", "0")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());
    }

    @DisplayName("when request POST '/orders/find' with order id '-1' " +
            "then view orders with errors returns")
    @Test
    void findOrdersRequest4() throws Exception {
        this.mockMvc
                .perform(
                        post("/orders/find")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("orderId", "-1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());
    }

    @DisplayName("when request POST '/orders/find' with empty start date  " +
            "then view orders with errors returns")
    @Test
    void findOrdersRequest5() throws Exception {
        this.mockMvc
                .perform(
                        post("/orders/find")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("startDate", "invalid date")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());
    }
}


