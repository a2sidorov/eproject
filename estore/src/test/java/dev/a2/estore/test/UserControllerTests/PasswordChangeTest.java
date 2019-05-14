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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Testing password change")
@SpringJUnitWebConfig(TestConfig.class)
class PasswordChangeTest {

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

    @DisplayName("when request GET '/profile/password' then password change page returns")
    @Test
    void makeGetRequest2() throws Exception {
        this.mockMvc.perform(get("/profile/password"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/profile/password' with correct password" +
            "then user password gets changed and password page returns")
    @Test
    void makePostRequest2() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/password")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("currentPassword", "Password1")
                                .param("password", "NewPassword1")
                                .param("confirmPassword", "NewPassword1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/profile/password"));

    }

    @DisplayName("when request POST '/profile/password' with an incorrect old password" +
            "then password change page returns with error")
    @Test
    void makePostRequest3() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/password")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("currentPassword", "incorrectPassword")
                                .param("password", "NewPassword1")
                                .param("confirmPassword", "NewPassword1")
                )
                .andDo(print())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userPasswordDto", "currentPassword"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/profile/password' with empty new password " +
            "then password change page returns with error")
    @Test
    void makePostRequest4() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/password")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("currentPassword", "Password1")
                                .param("password", "")
                                .param("confirmPassword", "NewPassword1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userPasswordDto", "password"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/profile/password' with empty confirm password " +
            "then password change page returns with error")
    @Test
    void makePostRequest5() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/password")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("currentPassword", "Password1")
                                .param("password", "NewPassword1")
                                .param("confirmPassword", "")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userPasswordDto", "password"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/profile/password' with invalid new password " +
            "then password change page returns with error")
    @Test
    void makePostRequest6() throws Exception {
        this.mockMvc
                .perform(
                        post("/profile/password")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("currentPassword", "Password1")
                                .param("password", "password")
                                .param("confirmPassword", "password")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userPasswordDto", "password"))
                .andExpect(status().isOk());
    }

}
