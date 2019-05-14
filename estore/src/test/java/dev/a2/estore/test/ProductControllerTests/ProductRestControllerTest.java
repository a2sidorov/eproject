package dev.a2.estore.test.ProductControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.a2.estore.dto.CategoryDto;
import dev.a2.estore.test.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Testing product rest controller")
@SpringJUnitWebConfig(TestConfig.class)
class ProductRestControllerTest {

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

    @DisplayName("when request GET '/products' then products returns")
    @Test
    void getProducts() throws Exception {
        this.mockMvc.perform(get("/products"))
                .andExpect(status().isOk());
    }

    @DisplayName("when request GET '/products/category' with valid category id " +
            "then products filtered by category return")
    @Test
    void getProductsByCategory1() throws Exception {
        this.mockMvc.perform(get("/products/category?id=1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("when request GET '/products/category' without category id " +
            "then error returns")
    @Test
    void getProductsByCategory2() throws Exception {
        this.mockMvc.perform(get("/products/category"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("when request GET '/products/category' with empty category id " +
            "then error returns")
    @Test
    void getProductsByCategory3() throws Exception {
        this.mockMvc.perform(get("/products/category?id="))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("when request GET '/products/category' with white space as category id " +
            "then error returns")
    @Test
    void getProductsByCategory4() throws Exception {
        this.mockMvc.perform(get("/products/category?id= "))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("when request GET '/products/category' with category id '0' " +
            "then error returns")
    @Test
    void getProductsByCategory5() throws Exception {
        this.mockMvc.perform(get("/products/category?id=0"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("when request GET '/products/category' with a letter as category id " +
            "then error returns")
    @Test
    void getProductsByCategory6() throws Exception {
        this.mockMvc.perform(get("/products/category?id=a"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("when request GET '/attributes/category' with valid category id " +
            "then product attributes return")
    @Test
    void getAttributesByCategory() throws Exception {
        this.mockMvc.perform(get("/attributes/category?id=1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/category/create' with valid data " +
            "then category gets created")
    @Test
    void postCategoryRequest1() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setParentCategoryId(0L);
        categoryDto.setName("New category");
        categoryDto.setType("category");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(categoryDto);
        this.mockMvc
                .perform(
                        post("/category/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)

                )
                .andExpect(status().isOk());
    }

    @DisplayName("when request POST '/category/create' without parent category id " +
            "then error returns")
    @Test
    void postCategoryRequest2() throws Exception {
        String json = "{\"name\":\"NewCategory\", \"type\":\"category\"}";
        this.mockMvc
                .perform(
                        post("/category/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)

                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("when request POST '/category/create' with empty category id " +
            "then error returns")
    @Test
    void postCategoryRequest3() throws Exception {
        String json = "{\"parentCategoryId\":\"\", \"name\":\"NewCategory\", \"type\":\"category\"}";
        this.mockMvc
                .perform(
                        post("/category/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)

                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("when request POST '/category/create' with white space as category id " +
            "then error returns")
    @Test
    void postCategoryRequest4() throws Exception {
        String json = "{\"parentCategoryId\":\" \", \"name\":\"NewCategory\", \"type\":\"category\"}";
        this.mockMvc
                .perform(
                        post("/category/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)

                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("when request POST '/category/create' without name " +
            "then error returns")
    @Test
    void postCategoryRequest5() throws Exception {
        String json = "{\"parentCategoryId\":\"0\", \"type\":\"category\"}";
        this.mockMvc
                .perform(
                        post("/category/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)

                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("when request POST '/category/create' with empty name " +
            "then error returns")
    @Test
    void postCategoryRequest6() throws Exception {
        String json = "{\"parentCategoryId\":\"0\", \"name\":\"\", \"type\":\"category\"}";
        this.mockMvc
                .perform(
                        post("/category/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)

                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("when request PUT '/category' with valid name " +
            "then category name gets updated")
    @Test
    void renameCategory() throws Exception {
        this.mockMvc
                .perform(
                        put("/category?id=1&name=newCategoryName")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("when request DELETE '/category' with products in category then error teturns")
    @Test
    void deleteCategory() throws Exception {
        this.mockMvc
                .perform(
                        delete("/category?id=1")

                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("when request GET '/api/top-products' then top-products return")
    @Test
    void getTopProducts() throws Exception {
        this.mockMvc
                .perform(
                        get("/api/top-products")

                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}


