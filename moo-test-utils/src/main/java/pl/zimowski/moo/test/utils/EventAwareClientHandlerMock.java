package pl.zimowski.moo.test.utils;

import java.util.List;

import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ClientHandling;
import pl.zimowski.moo.api.ClientReporting;

/**
 * Handy mock useful for spying and verifying generated client events. 
 * Mocked to assume connection is successfull every time.
 * 
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class EventAwareClientHandlerMock implements ClientHandling {
    
    private List<ClientEvent> eventList;

    
    @Override
    public boolean connect(ClientReporting listener) {
        return true;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void disconnect() {
    }

    @Override
    public void send(ClientEvent event) {
        if(eventList != null) {
            eventList.add(event);
        }
    }

    public void setEventList(List<ClientEvent> eventList) {
        this.eventList = eventList;
    }
}