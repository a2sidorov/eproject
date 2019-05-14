package dev.a2.eproject.billboard;

import org.apache.log4j.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * This bean acts as a JMS listener.
 *
 * @author Andrei Sidorov
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destination",
                propertyValue = "java:/jms/queue/MyQueue"
        )
})
public class ReceivingMdb implements MessageListener {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(ReceivingMdb.class);

    /**
     * Injects bean Broadcaster.
     */
    @EJB
    private Broadcaster broadcaster;

    @Override
    public void onMessage(final Message message) {
        logger.info("Message received " + message);

        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;

            try {
                logger.info("Message content " + ((TextMessage) message).getText());
                broadcaster.onMessage(textMessage.getText());
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }


}

