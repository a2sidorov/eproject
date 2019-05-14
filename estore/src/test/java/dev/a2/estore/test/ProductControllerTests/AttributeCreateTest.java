package dev.a2.estore.test.ProductControllerTests;

import dev.a2.estore.service.JmsService;
import dev.a2.estore.test.config.TestConfig;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.jndi.ExpectedLookupTemplate;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import java.util.Hashtable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Testing attribute create")
@SpringJUnitWebConfig(TestConfig.class)
class AttributeCreateTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .defaultRequest(get("/").with(user("manager@mail.dev").roles("MANAGER")))
                .apply(springSecurity())
                .build();
    }

    @DisplayName("when request GET '/product/{productId}/attribute/create' " +
            "then product attribute page returns")
    @Test
    void getAttributeCreatePage() throws Exception {
        this.mockMvc.perform(get("/product/1/attribute/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-attribute"));
    }

    @DisplayName("when request POST '/product/{productId}/attribute/create' with valid name " +
            "then product attribute gets created")
    @Test
    void postProductAttributeCreateRequest1() throws Exception {
        this.mockMvc
                .perform(
                        post("/product/1/attribute/create")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("productId", "1")
                                .param("categoryId", "1")
                                .param("name", "Attribute name")
                )
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("when request GET '/product/{productId}/attribute/delete' " +
            "then product attribute delte page returns")
    @Test
    void getAttributeDeletePage() throws Exception {
        this.mockMvc.perform(get("/product/1/attribute/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("delete-attribute"));
    }

}



