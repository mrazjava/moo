package pl.zimowski.moo.ui.web1;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ServerEvent;

/**
 * Manager of server connection. Exposes REST endpoints for angular UI
 * communication.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@RestController
@Component
public class EventProvider {

    private static final Logger log = LoggerFactory.getLogger(EventProvider.class);

    @Inject
    private SimpMessagingTemplate template;

    private ServerListener serverListener;

    private Socket socket;


    @PostConstruct
    public void init() {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            log.debug("initializing....");
            socket = new Socket("localhost", 8000);
            serverListener = new ServerListener(socket, template);
            executor.submit(serverListener);
        }
        catch(IOException e) {
            log.error("could not connect to server: {}", e.getMessage());
        }

        log.debug("READY!");
    }

    /**
     * User invoked a login operation on the client.
     *
     * @param nick of the user that logged in
     * @return {@code true} on success
     */
    @RequestMapping(value = "/moo/login", method = RequestMethod.POST)
    public boolean mooLogin(@RequestBody String nick) {

        log.debug("login: {}", nick);
        ClientEvent event = new ClientEvent(ClientAction.Signin).withAuthor(nick);
        return sendClientEvent(event);
    }

    /**
     * User invoked a logout operation on a client.
     *
     * @param nick of the user that logged out
     * @return {@code true} on success
     */
    @RequestMapping(value = "/moo/logout", method = RequestMethod.POST)
    public boolean mooLogout(@RequestBody String nick) {

        log.debug("logout: {}", nick);
        ClientEvent event = new ClientEvent(ClientAction.Signoff).withAuthor(nick);
        return sendClientEvent(event);
    }

    /**
     * User transmitted a chat message.
     *
     * @param event holding information about a trasmitted message
     * @return {@code true} on success
     */
    @RequestMapping("/moo/msg")
    public boolean mooMsg(@RequestBody Map<String, String> event) {

        log.debug(event.toString());
        String nickName = event.get("nickName");
        String message = event.get("msg");
        ClientEvent clientEvent = new ClientEvent(ClientAction.Message)
                .withAuthor(nickName).withMessage(message);
        return sendClientEvent(clientEvent);
    }

    /**
     * Allows UI client to peek at few recently processed messages. Useful
     * when initializing new view.
     *
     * @return recently processed messages
     */
    @RequestMapping("/latest-events")
    public List<ServerEvent> getCachedEvents() {
        return serverListener.getRecentEvents();
    }

    private boolean sendClientEvent(ClientEvent event) {

        boolean status = false;

        if(socket == null) {
            log.warn("null socket");
            return status;
        }

        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(event);
            out.flush();
            status = true;
        }
        catch(IOException e) {
            log.error("message transmission failed: {}", e.getMessage());
        }

        return status;
    }
}