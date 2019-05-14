package dev.a2.eproject.billboard;

import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This bean provides a WebSocket endpoint and broadcast messsages to connected users.
 *
 * @author Andrei Sidorov
 */
@Stateless
@ServerEndpoint(value = "/websocket")
public class Broadcaster {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(Broadcaster.class);

    /**
     * The set of active connections.
     */
    private static final Set<Session> clients = Collections.synchronizedSet(new HashSet<>());

    /**
     * Broadcasts a message to currently connected clients.
     *
     * @param message a message to broadcast.
     * @throws IOException the IOException.
     */
    @OnMessage
    public void onMessage(final String message) throws IOException {
        logger.info("Broadcasting message " + message);
        synchronized (clients) {
            // Iterates over the connected sessions and broadcast the received message.
            for (Session client : clients) {
                client.getBasicRemote().sendText(message);
            }
        }
    }

    /**
     * Adds a session to the set when it gets opened.
     *
     * @param session an opened session.
     */
    @OnOpen
    public void onOpen(final Session session) {
        logger.info("A new user has connected.");
        clients.add(session);
    }

    /**
     * Removes a session from the set when it gets closed.
     *
     * @param session a closed session.
     */
    @OnClose
    public void onClose(final Session session) {
        logger.info("A user has disconnected.");
        clients.remove(session);
    }

    /**
     * Handles errors.
     *
     * @param session a session.
     * @param throwable en exception.
     */
    @OnError
    public void onError(final Session session, final Throwable throwable) {
        logger.error(throwable);
    }

}

