package dev.a2.estore.service;

import dev.a2.estore.model.Price;
import dev.a2.estore.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("Testing JmsService")
@ExtendWith(MockitoExtension.class)
class JmsServiceTest {

    @Mock
    ConnectionFactory connectionFactory;

    @Mock
    QueueConnection queueConnection;

    @Mock
    QueueSession queueSession;

    @Mock
    Destination destination;

    @Mock
    MessageProducer messageProducer;

    @Spy
    TextMessage message;

    @InjectMocks
    private JmsService jmsService = new JmsServiceImpl();

    @Nested
    @DisplayName("Testing send method")
    class sendTest {
        @DisplayName("when this method is called then the top-selling products are sent")
        @Test
        void sendTest1() throws JMSException {
            // given
            Price price = new Price();
            price.setPrice(new BigDecimal(10));

            List<Price> prices = new ArrayList<>();
            prices.add(price);

            Product product = new Product();
            product.setName("Product name");
            product.setImageUrl("Image url");
            product.setPurchasingPrices(prices);
            product.setSellingPrice(new BigDecimal(20));

            List<Product> products = new ArrayList<>();
            products.add(product);

            when(connectionFactory.createConnection()).thenReturn(queueConnection);
            when(queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE)).thenReturn(queueSession);
            when(queueSession.createProducer(destination)).thenReturn(messageProducer);
            when(queueSession.createTextMessage(anyString())).thenReturn(message);

            // run
            jmsService.send(products);

            // assert
            String outgoingJson = "[{\"sellingPrice\":\"10.00\",\"imageUrl\":\"Image url\",\"name\":\"Product name\"}]";
            verify(messageProducer, times(1)).send(message);
        }
    }
}

