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
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package dev.a2.estore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.a2.estore.model.Product;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides implementation for the JmsService interface.
 *
 * @author Andrei Sidorov
 */
@Service
public class JmsServiceImpl implements JmsService {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(JmsService.class);

    /**
     * Injects the JMS ConnectionFactory.
     */
    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    /**
     * Injects the JMS Destination.
     */
    @Resource(mappedName = "java:/jms/queue/MyQueue")
    private Destination destination;

    @Async
    @Override
    public void send(final List<Product> topTenProducts) {

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> list = new ArrayList<>();

        topTenProducts.forEach(product -> {
            Map<String, String> map = new HashMap<>();
            map.put("imageUrl", product.getImageUrl());
            map.put("name", product.getName());
            map.put("sellingPrice", product.getSellingPrice().toString());
            list.add(map);
        });

        String json = "";
        try {
            json = mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            logger.error(e);
        }

        try {
            QueueConnection connection = (QueueConnection) connectionFactory.createConnection();
            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage(json);

            producer.send(message);
            logger.info("The top-ten-selling products have ben sent to the billboard.");
            session.close();
            connection.close();
        } catch (JMSException e) {
            logger.error(e);
        }
    }

}
