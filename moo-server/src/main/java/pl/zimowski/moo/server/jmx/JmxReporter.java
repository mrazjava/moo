package pl.zimowski.moo.server.jmx;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Registers MBean for JMX reporting and computes various statistics real-time
 * while updating the MBean internally.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class JmxReporter implements JmxReportingSupport, MBeanAccess {

    @Inject
    private Logger log;

    private ClientAnalytics clientAnalytics;


    @Override
    public void initialize() {
        registerMBean(log, clientAnalytics = new ClientAnalytics());
    }

    @Override
    public synchronized void clientConnected() {
        clientAnalytics.connectedClientCount++;
    }

    @Override
    public synchronized void clientDisconnected() {
        clientAnalytics.connectedClientCount--;
        clientAnalytics.disconnectedClientCount++;
    }
}
