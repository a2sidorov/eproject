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

package dev.a2.estore.test.UserControllerTests;

import dev.a2.estore.service.JmsService;
import dev.a2.estore.test.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.naming.NamingException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Testing sign in")
@SpringJUnitWebConfig(TestConfig.class)
class SigninTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws NamingException {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
    }

    @DisplayName("when request GET '/signin' then sign in page returns")
    @Test
    void makeGetRequest() throws Exception {
        this.mockMvc.perform(get("/signin"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signin' with empty password then authentication fails")
    @Test
    void makePostRequest1() throws Exception {
        this.mockMvc.perform(formLogin("/signin").user("client@mail.dev").password(""))
                .andExpect(unauthenticated());
    }

    @DisplayName("when request POST '/signin' with empty email then authentication fails")
    @Test
    void makePostRequest2() throws Exception {
        this.mockMvc.perform(formLogin("/signin").user("").password("Password1"))
                .andExpect(unauthenticated());
    }

    @DisplayName("when request POST '/signin' with an incorrect password then authentication fails")
    @Test
    void makePostRequest3() throws Exception {
        this.mockMvc.perform(formLogin("/signin").user("client@mail.dev").password("Incorrect1"))
                .andExpect(unauthenticated());
    }


    @DisplayName("when request POST '/signin' with an incorrect email then authentication fails")
    @Test
    void makePostRequest4() throws Exception {
        this.mockMvc.perform(formLogin("/signin").user("client@mail,dev").password("Password1"))
                .andExpect(unauthenticated());
    }

    @DisplayName("when request POST '/signin' with correct email and password as a manager then authentication succeeds")
    @Test
    void makePostRequest6() throws Exception {
        this.mockMvc.perform(formLogin("/signin").user("manager@mail.dev").password("Password1"))
                .andExpect(authenticated());
    }

}
