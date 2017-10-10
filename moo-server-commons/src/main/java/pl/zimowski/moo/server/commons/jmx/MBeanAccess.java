package pl.zimowski.moo.server.commons.jmx;

import static java.util.Objects.requireNonNull;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * Mixin utility methods for managing MBeans.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public interface MBeanAccess {

    /**
     * Add MBean into Platform MBean Server. Bean will be registered using its
     * own package and its class name.
     *
     * @param log logger
     * @param mbean your MBean instance
     */
    default void registerMBean(Logger log, Object mbean) {
        registerMBean(log, mbean, null, null);
    }

    /**
     * Add MBean into Platform MBean Server.
     *
     * @param log logger
     * @param mbean your MBean instance
     * @param artifactPackage of an artifact, such as "pl.zimowski.moo.server.jmx";
     *  this becomes the JMX namespace in JConsole; may be {@code null} in which case package of mbean is used.
     * @param name provide the human-readable name of bean; may be {@code null} in which case class name of mbean is used
     */
    default void registerMBean(Logger log, Object mbean, String artifactPackage, String name) {

        requireNonNull(log);
        requireNonNull(mbean);

        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName oname = new ObjectName(mbeanName(mbean, artifactPackage, name));
            mbs.registerMBean(mbean, oname);
            log.info("registered {}", oname);
        }
        catch(Exception e) {
            throw new RuntimeException("error, failed to register MBean " + name + ", exception occurred: " + e, e);
        }
    }

    /**
     * Given the MBean and optionally a package and name of an MBean, constructs
     * JMX compliant name. The MBean argument is used in case package and name
     * arguments are not provided, in which case package and name are derived
     * from an MBean (which is usually the case anyways).
     *
     * @param mbean from which to derive package and name
     * @param artifactPackage to use (if defined, package will not be derived from MBean)
     * @param name to use (if defined, name will not be derived from MBean)
     * @return fully compliant, standard JMX name of an MBean
     */
    default String mbeanName(Object mbean, String artifactPackage, String name) {

        if(StringUtils.isBlank(artifactPackage))
            artifactPackage = mbean.getClass().getPackage().getName();

        if(StringUtils.isBlank(name))
            name = mbean.getClass().getSimpleName();

        return artifactPackage + ":type=" + name;
    }

    /**
     * Given package and name of an MBean, constructs JMX compliant name.
     *
     * @param artifactPackage (usually the package of a bean)
     * @param name (usually the name of a bean)
     * @return fully compliant, standard JMX name of an MBean
     */
    default String mbeanName(String artifactPackage, String name) {
        return artifactPackage + ":type=" + name;
    }
}