package dev.a2.estore.test.ProductControllerTests;

import dev.a2.estore.test.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Testing product controller")
@SpringJUnitWebConfig(TestConfig.class)
class ProductControllerTest {

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

    @DisplayName("when request GET '/product' then with valid product id then product page returns")
    @Test
    void getProductPageRequest1() throws Exception {
        this.mockMvc.perform(get("/product?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("product"));
    }

    @DisplayName("when request GET '/product' with empty product id then 404 page returns")
    @Test
    void getProductPageRequest2() throws Exception {
        this.mockMvc.perform(get("/product?id="))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error-400"));
    }

    @DisplayName("when request GET '/product' with invalid product id then 404 page returns")
    @Test
    void getProductPageRequest3() throws Exception {
        this.mockMvc.perform(get("/product?id=a"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error-400"));
    }

    @DisplayName("when request GET '/product' with product id '0' then 404 page returns")
    @Test
    void getProductPageRequest4() throws Exception {
        this.mockMvc.perform(get("/product?id=0"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error-400"));
    }

    @DisplayName("when request GET '/product' with missing product id parameter then 404 page returns")
    @Test
    void getProductPageRequest5() throws Exception {
        this.mockMvc.perform(get("/product"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error-400"));
    }

    @DisplayName("when request GET '/categories/edit' " +
            "then categories edit page returns")
    @Test
    @WithMockUser(username = "manager@mail.dev", roles = {"MANAGER"})
    void getAttributeCreatePage() throws Exception {
        this.mockMvc.perform(get("/categories/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-categories"));
    }

    @DisplayName("when request GET '/categories/edit' " +
            "then categories edit page returns")
    @Test
    @WithMockUser(username = "manager@mail.dev", roles = {"MANAGER"})
    void getTopProductsPage() throws Exception {
        this.mockMvc.perform(get("/reports/top-products"))
                .andExpect(status().isOk())
                .andExpect(view().name("top-products"));
    }

}

