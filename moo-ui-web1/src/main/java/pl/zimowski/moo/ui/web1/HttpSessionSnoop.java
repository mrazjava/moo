package pl.zimowski.moo.ui.web1;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class HttpSessionSnoop implements HttpSessionListener {

    private static final Logger log = LoggerFactory.getLogger(HttpSessionSnoop.class);

    @Inject
    private EventProvider eventProvider;

    @Inject
    private SimpMessagingTemplate sender;


    @Override
    public void sessionCreated(HttpSessionEvent event) {

        if(log.isDebugEnabled()) {
            HttpSession session = event.getSession();
            log.debug("created session {}", session.getId());
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {

        HttpSession session = event.getSession();
        String nick = (String)session.getAttribute(App.SESSION_ATTR_NICK);

        log.debug("destrogying session {} | user = {}", session.getId(), nick);

        if(StringUtils.isNotEmpty(nick)) {
            // user never signed off and session was destoryed, so invoke the
            // signoff event; had user signed off, session would no longer
            // contain nick attribute
            eventProvider.mooLogout(nick, null);
            sender.convertAndSend("/topic/session-expired", new String[] { nick });
        }
    }
}