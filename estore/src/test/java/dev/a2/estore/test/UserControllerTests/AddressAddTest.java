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

import dev.a2.estore.model.User;
import dev.a2.estore.test.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Testing address removing")
@SpringJUnitWebConfig(TestConfig.class)
class AddressAddTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .defaultRequest(get("/").with(user("client@mail.dev")))
                .apply(springSecurity())
                .build();
    }

    @DisplayName("when request GET '/profile/address/add' then new address page returns")
    @Test
    void getProfileAddressAdd() throws Exception {
        this.mockMvc.perform(get("/profile/address/add"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/profile/address/add' with valid address information " +
            "then new address gets added and profile page returns")
    @Test
    void postProfileAddressAdd1() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/address/add")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "PostalCode")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("apartment", "1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/profile/address/add"));
    }


    @DisplayName("when request POST '/profile/address/add' with empty countryId" +
            "then error returns")
    @Test
    void postProfileAddressAdd2() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/address/add")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("countryId", "")
                                .param("city", "City")
                                .param("postalCode", "PostalCode")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("apartment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userAddressDto", "countryId"));
    }

    @DisplayName("when request POST '/profile/address/add' with countryId not a number" +
            "then error returns")
    @Test
    void postProfileAddressAdd3() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/address/add")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("countryId", "a")
                                .param("city", "City")
                                .param("postalCode", "PostalCode")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("apartment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userAddressDto", "countryId"));
    }

    @DisplayName("when request POST '/profile/address/add' with empty city" +
            "then error returns")
    @Test
    void postProfileAddressAdd4() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/address/add")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("countryId", "1")
                                .param("city", "")
                                .param("postalCode", "PostalCode")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("apartment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userAddressDto", "city"));
    }

    @DisplayName("when request POST '/profile/address/add' with empty postal code" +
            "then error returns")
    @Test
    void postProfileAddressAdd5() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/address/add")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("apartment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userAddressDto", "postalCode"));
    }

    @DisplayName("when request POST '/profile/address/add' with empty street name " +
            "then error returns")
    @Test
    void postProfileAddressAdd6() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/address/add")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "PostalCode")
                                .param("street", "")
                                .param("house", "1")
                                .param("apartment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userAddressDto", "street"));
    }

    @DisplayName("when request POST '/profile/address/add' with empty house number " +
            "then error returns")
    @Test
    void postProfileAddressAdd7() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/address/add")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "PostalCode")
                                .param("street", "Street")
                                .param("house", "")
                                .param("apartment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userAddressDto", "house"));
    }

    @DisplayName("when request POST '/profile/address/add' with empty apartment number " +
            "then error returns")
    @Test
    void postProfileAddressAdd8() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/address/add")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "PostalCode")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("apartment", "")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userAddressDto", "apartment"));
    }


    @DisplayName("when request POST '/profile/address/update' " +
            "then the specified address gets updated and profile page returns")
    @Test
    void postProfileAddressUpdate1() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/address/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("id", "1")
                                .param("countryId", "1")
                                .param("city", "newCity")
                                .param("postalCode", "newPostalCode")
                                .param("street", "newStreet")
                                .param("house", "newHouse")
                                .param("apartment", "newApartment")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/profile"));
    }

}

