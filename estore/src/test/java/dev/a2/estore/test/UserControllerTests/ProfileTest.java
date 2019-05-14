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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Testing profile")
@SpringJUnitWebConfig(TestConfig.class)
class ProfileTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .defaultRequest(get("/").with(user("client@mail.dev")))
                .apply(springSecurity())
                //.alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @DisplayName("when request GET '/profile' then profile page returns")
    @Test
    void getProfile() throws Exception {
        this.mockMvc.perform(get("/profile"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/profile/personal-details/update' with empty first name " +
            "then error returns")
    @Test
    void postProfilePersonalDetailsUpdate1() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/personal-details/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "")
                                .param("lastName", "Lastname")
                                .param("dateOfBirth", "1986-02-24")
                                .param("email", "client@mail.dev")
                )
                .andDo(print())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userDetailsDto", "firstName"));
    }

    @DisplayName("when request POST '/profile/personal-details/update' with valid changed first name " +
            "then user first name is updated")
    @Test
    void postProfilePersonalDetailsUpdate1a() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/personal-details/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "NewName")
                                .param("lastName", "LastName")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "client@mail.dev")
                )
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user",
                        hasProperty("firstName", is("NewName"))));
    }

    @DisplayName("when request POST '/profile/personal-details/update' with empty last name " +
            "then error returns")
    @Test
    void postProfilePersonalDetailsUpdate2() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/personal-details/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Client")
                                .param("lastName", "")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "client@mail.dev")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userDetailsDto", "lastName"));
    }

    @DisplayName("when request POST '/profile/personal-details/update' with valid changed last name " +
            "then user last name is updated")
    @Test
    void postProfilePersonalDetailsUpdate2a() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/personal-details/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Client")
                                .param("lastName", "NewLastName")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "client@mail.dev")
                )
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user",
                        hasProperty("lastName", is("NewLastName"))));
    }

    @DisplayName("when request POST '/profile/personal-details/update' with empty date of birth " +
            "then error returns")
    @Test
    void postProfilePersonalDetailsUpdate3() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/personal-details/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Client")
                                .param("lastName", "Lastname")
                                .param("dateOfBirth", "")
                                .param("email", "client@mail.dev")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userDetailsDto", "dateOfBirth"));
    }


    @DisplayName("when request POST '/profile/personal-details/update' with an invalid date of birth " +
            "then error returns")
    @Test
    void postProfilePersonalDetailsUpdate3a() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/personal-details/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Client")
                                .param("lastName", "Lastname")
                                .param("dateOfBirth", "2222-22-22")
                                .param("email", "client@mail.dev")
                )
                .andDo(print())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userDetailsDto", "dateOfBirth"));
    }

    @DisplayName("when request POST '/profile/personal-details/update' with a valid date of birth" +
            "then user date of birth is updated")
    @Test
    void postProfilePersonalDetailsUpdate4() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/personal-details/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Client")
                                .param("lastName", "Lastname")
                                .param("dateOfBirth", "1111-11-12")
                                .param("email", "client@mail.dev")
                )
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user",
                        hasProperty("dateOfBirth", is(LocalDate.parse("1111-11-12")))));
    }

    @DisplayName("when request POST '/profile/address/update' with valid data" +
            "then the specified address gets updated and profile page returns")
    @Test
    void postProfileAddressUpdate1() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/address/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("id", "1")
                                .param("countryId", "1")
                                .param("city", "AnotherCity")
                                .param("postalCode", "PostalCode")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("apartment", "1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/profile"));
    }

    @DisplayName("when request POST '/profile/address/update' with empty country id" +
            "then error returns")
    @Test
    void postProfileAddressUpdate2() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/address/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("id", "1")
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

    @DisplayName("when request POST '/profile/address/update' with empty city " +
            "then error returns")
    @Test
    void postProfileAddressUpdate3() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/address/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("id", "1")
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

    @DisplayName("when request POST '/profile/address/update' with empty postalCode " +
            "then error returns")
    @Test
    void postProfileAddressUpdate4() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/address/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("id", "1")
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

    @DisplayName("when request POST '/profile/address/update' with empty street name " +
            "then error returns")
    @Test
    void postProfileAddressUpdate5() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/address/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("id", "1")
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

    @DisplayName("when request POST '/profile/address/update' with empty house number " +
            "then error returns")
    @Test
    void postProfileAddressUpdate6() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/address/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("id", "1")
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

    @DisplayName("when request POST '/profile/address/update' with empty house number " +
            "then error returns")
    @Test
    void postProfileAddressUpdate7() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/address/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("id", "1")
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

    @DisplayName("when request GET '/reports/top-clients' then top-clients page returns")
    @Test
    void getTopCLients() throws Exception {
        this.mockMvc.perform(get("/reports/top-clients"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
