package dev.a2.estore.test.ProductControllerTests;

import dev.a2.estore.test.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Testing product create")
@SpringJUnitWebConfig(TestConfig.class)
class ProductCreateTest {

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

    @DisplayName("when request GET '/product/create' then create product page returns")
    @Test
    void getCreateProductPageRequest() throws Exception {
        this.mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-product"));
    }

    @DisplayName("when request POST '/product/create' with valid data " +
            "then product is created")
    @Test
    void postProductCreateRequest1() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "20.00")
                                .param("sellingPrice", "30")
                                .param("height", "0.5")
                                .param("width", "0.5")
                                .param("depth", "0.5")
                                .param("weight", "0.5")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/product/create"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message",
                        "Product is successfully created."));
    }

    @DisplayName("when request POST '/product/create' without category id " +
            "then error returns")
    @Test
    void postProductCreateRequest2() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("name", "Product name")
                                .param("purchasingPrice", "20.00")
                                .param("sellingPrice", "1")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "categoryId"));
    }

    @DisplayName("when request POST '/product/create' with empty category id   " +
            "then error returns")
    @Test
    void postProductCreateRequest3() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "")
                                .param("name", "Product name")
                                .param("purchasingPrice", "20.00")
                                .param("sellingPrice", "1")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "categoryId"));
    }

    @DisplayName("when request POST '/product/create' with category id '0' " +
            "then error returns")
    @Test
    void postProductCreateRequest4() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "0")
                                .param("name", "Product name")
                                .param("purchasingPrice", "20.00")
                                .param("sellingPrice", "1")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "categoryId"));
    }

    @DisplayName("when request POST '/product/create' with category id '-1' " +
            "then error returns")
    @Test
    void postProductCreateRequest5() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "-1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "20.00")
                                .param("sellingPrice", "1")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "categoryId"));
    }

    @DisplayName("when request POST '/product/create' with a letter as category id " +
            "then error returns")
    @Test
    void postProductCreateRequest6() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "a")
                                .param("name", "Product name")
                                .param("purchasingPrice", "20.00")
                                .param("sellingPrice", "1")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "categoryId"));
    }

    @DisplayName("when request POST '/product/create' with white space as category id " +
            "then error returns")
    @Test
    void postProductCreateRequest7() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", " ")
                                .param("name", "Product name")
                                .param("purchasingPrice", "20.00")
                                .param("sellingPrice", "1")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "categoryId"));
    }

    @DisplayName("when request POST '/product/create' without name " +
            "then error returns")
    @Test
    void postProductCreateRequest8() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("purchasingPrice", "20.00")
                                .param("sellingPrice", "1")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "name"));
    }

    @DisplayName("when request POST '/product/create' with empty name " +
            "then error returns")
    @Test
    void postProductCreateRequest9() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "")
                                .param("purchasingPrice", "20.00")
                                .param("sellingPrice", "1")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "name"));
    }

    @DisplayName("when request POST '/product/create' with white space as name " +
            "then error returns")
    @Test
    void postProductCreateRequest10() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", " ")
                                .param("purchasingPrice", "20.00")
                                .param("sellingPrice", "1")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "name"));
    }

    @DisplayName("when request POST '/product/create' without purchasingPrice " +
            "then error returns")
    @Test
    void postProductCreateRequest11() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("sellingPrice", "1")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "purchasingPrice"));
    }

    @DisplayName("when request POST '/product/create' with empty purchasingPrice " +
            "then error returns")
    @Test
    void postProductCreateRequest12() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "")
                                .param("sellingPrice", "1")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "purchasingPrice"));
    }

    @DisplayName("when request POST '/product/create' with white space as purchasingPrice " +
            "then error returns")
    @Test
    void postProductCreateRequest13() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", " ")
                                .param("sellingPrice", "1")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "purchasingPrice"));
    }

    @DisplayName("when request POST '/product/create' with negative purchasingPrice " +
            "then error returns")
    @Test
    void postProductCreateRequest14() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "-1")
                                .param("sellingPrice", "1")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "purchasingPrice"));
    }

    @DisplayName("when request POST '/product/create' without sellingPrice " +
            "then error returns")
    @Test
    void postProductCreateRequest15() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "1")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "sellingPrice"));
    }

    @DisplayName("when request POST '/product/create' with empty sellingPrice " +
            "then error returns")
    @Test
    void postProductCreateRequest16() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "1")
                                .param("sellingPrice", "")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "sellingPrice"));
    }



    @DisplayName("when request POST '/product/create' with sellingPrice '-1' " +
            "then error returns")
    @Test
    void postProductCreateRequest18() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "1")
                                .param("sellingPrice", "-1")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "sellingPrice"));
    }

    @DisplayName("when request POST '/product/create' with a letter as sellingPrice " +
            "then error returns")
    @Test
    void postProductCreateRequest19() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "1")
                                .param("sellingPrice", "a")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "sellingPrice"));
    }

    @DisplayName("when request POST '/product/create' with white space as sellingPrice " +
            "then error returns")
    @Test
    void postProductCreateRequest20() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "1")
                                .param("sellingPrice", " ")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "sellingPrice"));
    }

    @DisplayName("when request POST '/product/create' without product height " +
            "then error returns")
    @Test
    void postProductCreateRequest21() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "1")
                                .param("sellingPrice", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "height"));
    }

    @DisplayName("when request POST '/product/create' with empty product height " +
            "then error returns")
    @Test
    void postProductCreateRequest22() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "1")
                                .param("sellingPrice", "1")
                                .param("height", " ")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "height"));
    }

    @DisplayName("when request POST '/product/create' with negative product height " +
            "then error returns")
    @Test
    void postProductCreateRequest23() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "1")
                                .param("sellingPrice", "1")
                                .param("height", "-1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "height"));
    }

    @DisplayName("when request POST '/product/create' with a letter as product height " +
            "then error returns")
    @Test
    void postProductCreateRequest24() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "1")
                                .param("sellingPrice", "1")
                                .param("height", "a")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "height"));
    }

    @DisplayName("when request POST '/product/create' with white space as product height " +
            "then error returns")
    @Test
    void postProductCreateRequest25() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "1")
                                .param("sellingPrice", "1")
                                .param("height", " ")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "height"));
    }

    @DisplayName("when request POST '/product/create' without quantity inStock " +
            "then error returns")
    @Test
    void postProductCreateRequest26() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "1")
                                .param("sellingPrice", "1")
                                .param("height", "1")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "inStock"));
    }

    @DisplayName("when request POST '/product/create' with empty quantity inStock " +
            "then error returns")
    @Test
    void postProductCreateRequest27() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "1")
                                .param("sellingPrice", "1")
                                .param("height", " ")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "inStock"));
    }

    @DisplayName("when request POST '/product/create' with negative quantity inStock " +
            "then error returns")
    @Test
    void postProductCreateRequest28() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "1")
                                .param("sellingPrice", "1")
                                .param("height", " ")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "-1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "inStock"));
    }

    @DisplayName("when request POST '/product/create' with a letter as quantity inStock " +
            "then error returns")
    @Test
    void postProductCreateRequest29() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "1")
                                .param("sellingPrice", "1")
                                .param("height", " ")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", "a")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "inStock"));
    }

    @DisplayName("when request POST '/product/create' with white space as quantity inStock " +
            "then error returns")
    @Test
    void postProductCreateRequest30() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "1")
                                .param("name", "Product name")
                                .param("purchasingPrice", "1")
                                .param("sellingPrice", "1")
                                .param("height", " ")
                                .param("width", "1")
                                .param("depth", "1")
                                .param("weight", "1")
                                .param("inStock", " ")
                                .param("measureUnitsId", "1")
                )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("productDto", "inStock"));
    }

}
