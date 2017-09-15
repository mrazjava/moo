package pl.zimowski.moo.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.zimowski.moo.api.ApiUtils;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;

/**
 * Listens for incoming events from the server and echoes them back to the
 * client.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerListener extends Thread {

    private static final Logger log = LoggerFactory.getLogger(ServerListener.class);

    private Socket socket;

    private ConnectionManagement connectionManagement;

    private NickNameAssigning nickNameAssigner;


    public ServerListener(Socket socket, ConnectionManagement connectionManagement) {
        this.socket = socket;
        this.connectionManagement = connectionManagement;
    }

    public ServerListener withNickNameAssigner(NickNameAssigning assigner) {
        this.nickNameAssigner = assigner;
        return this;
    }

    @Override
    public void run() {

        try {
            while(!socket.isInputShutdown()) {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ServerEvent serverEvent = (ServerEvent)in.readObject();
                if(serverEvent.getAction() == ServerAction.ConnectionEstablished) {
                    serverEvent.setMessage("connected, client id: " + serverEvent.getClientId());
                }
                if(serverEvent.getAction() == ServerAction.NickGenerated && nickNameAssigner != null) {
                    String assignedNickName = serverEvent.getMessage();
                    nickNameAssigner.setNickName(assignedNickName);
                    serverEvent.setMessage(String.format("You will be known as '%s'", assignedNickName));

                }
                App.LOG_CHAT.info("({}) {}", serverEvent.getAuthor(), serverEvent.getMessage());
            }
        }
        catch(EOFException e) {
            App.LOG_CHAT.info("({}) connection terminated by server; bye!", ApiUtils.APP_NAME);
        }
        catch (IOException | ClassNotFoundException e) {
            log.error("unexpected connection error: {}; aborting!", e.getMessage());
        }

        connectionManagement.disconnect();
        System.exit(MAX_PRIORITY);
    }
}