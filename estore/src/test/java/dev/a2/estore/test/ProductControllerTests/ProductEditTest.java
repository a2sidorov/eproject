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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Testing product edit")
@SpringJUnitWebConfig(TestConfig.class)
class ProductEditTest {

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

    @DisplayName("when request GET '/product/{productId}/edit' with valid product id " +
            "then edit product page returns")
    @Test
    @WithMockUser(username = "manager@mail.dev", roles = {"MANAGER"})
    void getCreateProductPageRequest1() throws Exception {
        this.mockMvc.perform(get("/product/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-product"));
    }

    @DisplayName("when request GET '/product/{productId}/edit' with empty product id " +
            "then 404 page returns")
    @Test
    @WithMockUser(username = "manager@mail.dev", roles = {"MANAGER"})
    void getCreateProductPageRequest2() throws Exception {
        this.mockMvc.perform(get("/product//edit"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("when request GET '/product/{productId}/edit' with white space as product id " +
            "then 404 page returns")
    @Test
    @WithMockUser(username = "manager@mail.dev", roles = {"MANAGER"})
    void getCreateProductPageRequest3() throws Exception {
        this.mockMvc.perform(get("/product/ /edit"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("when request GET '/product/{productId}/edit' a letter as product id " +
            "then 404 page returns")
    @Test
    @WithMockUser(username = "manager@mail.dev", roles = {"MANAGER"})
    void getCreateProductPageRequest4() throws Exception {
        this.mockMvc.perform(get("/product/a/edit"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("when request GET '/product/{productId}/edit' product id '0' " +
            "then 404 page returns")
    @Test
    @WithMockUser(username = "manager@mail.dev", roles = {"MANAGER"})
    void getCreateProductPageRequest5() throws Exception {
        this.mockMvc.perform(get("/product/0/edit"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("when request GET '/product/{productId}/edit' with not existing product id " +
            "then 404 page returns")
    @Test
    @WithMockUser(username = "manager@mail.dev", roles = {"MANAGER"})
    void getCreateProductPageRequest6() throws Exception {
        this.mockMvc.perform(get("/product/99/edit"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("when request POST '/product/create' with changed category id " +
            "then product category id changed")
    @Test
    void postProductUpdate() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/1/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("categoryId", "2")
                                .param("name", "Product name")
                                .param("purchasingPrice", "20.00")
                                .param("sellingPrice", "30.00")
                                .param("height", "0.5")
                                .param("width", "0.5")
                                .param("depth", "0.5")
                                .param("weight", "0.5")
                                .param("inStock", "1")
                                .param("measureUnitsId", "1")
                )
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("when request GET '/product/{productId}/image/update' " +
            "then update image page returns")
    @Test
    @WithMockUser(username = "manager@mail.dev", roles = {"MANAGER"})
    void getImageUpdatePage() throws Exception {
        this.mockMvc.perform(get("/product/1/image/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("update-image"));
    }

}

