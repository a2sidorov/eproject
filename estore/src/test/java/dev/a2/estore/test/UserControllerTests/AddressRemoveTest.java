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

import dev.a2.estore.test.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Testing address removing")
@SpringJUnitWebConfig(TestConfig.class)
class AddressRemoveTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .defaultRequest(get("/").with(user("client@mail.dev")))
                .apply(springSecurity())
                .build();
        this.mockMvc
                .perform(
                        post("/profile/address/add")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "PostalCode")
                                .param("Street", "Street")
                                .param("House", "1")
                                .param("Apartment", "1")
                );
    }

    @DisplayName("when request POST '/profile/address/remove/{addressId}' " +
            "then the specified address gets removed and profile page returns")
    @Test
    void postProfileAddressRemove() throws Exception {
        this.mockMvc
                .perform( get("/profile/address/remove/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/profile"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message",
                        "Address is successfully removed."));
    }

}


