package dev.a2.estore.test.UserControllerTests;

import dev.a2.estore.test.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@DisplayName("Testing sign up")
@SpringJUnitWebConfig(TestConfig.class)
class SignupTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @DisplayName("when request GET '/signup' then sign up page returns" )
    @Test
    void makeGetRequest() throws Exception {
        this.mockMvc.perform(get("/signup"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with empty first name then error invalid first name returns")
    @Test
    void makePostRequest1b() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "firstName"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with first name longer than 35 characters " +
            "then error invalid first name returns")
    @Test
    void makePostRequest2() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "123456789012345678901234567890123456")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "firstName"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with no last name then error invalid last name returns")
    @Test
    void makePostRequest3() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "lastName"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with no date of birth " +
            "then error invalid date of birth returns")
    @Test
    void makePostRequest4() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "dateOfBirth"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with invalid date of birth " +
            "then error invalid date of birth returns")
    @Test
    void makePostRequest5() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "11-11-1111")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("apartment", "1")
                )
                .andDo(print())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "dateOfBirth"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with no email then error invalid email returns")
    @Test
    void makePostRequest6() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "email"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with invalid email then error invalid email returns")
    @Test
    void makePostRequest7() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "invalid@email")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "email"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with no password then error invalid password returns")
    @Test
    void makePostRequest8() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "")
                                .param("confirmPassword", "")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "password"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with invalid password then error invalid password returns")
    @Test
    void makePostRequest9() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "password")
                                .param("confirmPassword", "password")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "password"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with no confirm password then error fields do not match returns")
    @Test
    void makePostRequest10() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with no country id then error invalid country returns")
    @Test
    void makePostRequest11() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "countryId"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with country id not a number then error invalid country returns")
    @Test
    void makePostRequest11a() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "a")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "countryId"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with no city then error invalid city returns")
    @Test
    void makePostRequest12() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "city"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with city longer than 35 characters then error invalid city returns")
    @Test
    void makePostRequest13() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "123456789012345678901234567890123456")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "city"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with no postal code then error invalid postal code returns")
    @Test
    void makePostRequest15() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "postalCode"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with postal code longer than 16 characters " +
            "then error invalid postal code returns")
    @Test
    void makePostRequest16() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "12345678901234567")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "postalCode"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with no street then error invalid street returns")
    @Test
    void makePostRequest17() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "street"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with street longer than 35 characters " +
            "then error invalid postal code returns")
    @Test
    void makePostRequest18() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "123456789012345678901234567890123456")
                                .param("house", "1")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "street"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with no house number then error invalid house number returns")
    @Test
    void makePostRequest19() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "house"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with house number longer than 16 characters " +
            "then error invalid house number returns")
    @Test
    void makePostRequest20() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "12345678901234567")
                                .param("aparment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "house"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with no apartment number then error invalid house number returns")
    @Test
    void makePostRequest21() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("apartment", "")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "apartment"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with apartment number longer than 16 characters " +
            "then error invalid house number returns")
    @Test
    void makePostRequest22() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "test@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "12345678901234567")
                                .param("apartment", "12345678901234567")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "apartment"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with email that is already registered " +
            "then error user exists returns")
    @Test
    void makePostRequest23() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "client@mail.dev")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("apartment", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userSignupDto", "email"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/signup' with valid data then success page returns")
    @Test
    void makePostRequest24() throws Exception {
        this.mockMvc
                .perform(
                        post("/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("firstName", "Foo")
                                .param("lastName", "Bar")
                                .param("dateOfBirth", "1111-11-11")
                                .param("email", "new-user@test.test")
                                .param("password", "Password1234")
                                .param("confirmPassword", "Password1234")
                                .param("countryId", "1")
                                .param("city", "City")
                                .param("postalCode", "11111")
                                .param("street", "Street")
                                .param("house", "1")
                                .param("apartment", "1")
                )
                .andExpect(status().is3xxRedirection());
    }










}
