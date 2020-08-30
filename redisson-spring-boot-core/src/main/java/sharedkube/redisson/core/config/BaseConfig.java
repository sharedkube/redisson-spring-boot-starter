package sharedkube.redisson.core.config;

import java.net.URL;

import org.redisson.config.SslProvider;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code org.redisson.config.BaseConfig}
 * 
 * @author houyehua@gmail.com
 *
 */
@Getter
@Setter
public class BaseConfig {

    /**
     * If pooled connection not used for a <code>timeout</code> time and current connections amount bigger than minimum
     * idle connections pool size, then it will closed and removed from pool. Value in milliseconds.
     *
     */
    private int idleConnectionTimeout = 10000;

    /**
     * Timeout during connecting to any Redis server. Value in milliseconds.
     *
     */
    private int connectTimeout = 10000;

    /**
     * Redis server response timeout. Starts to countdown when Redis command was succesfully sent. Value in
     * milliseconds.
     *
     */
    private int timeout = 3000;

    private int retryAttempts = 3;

    private int retryInterval = 1500;

    /**
     * Password for Redis authentication. Should be null if not needed
     */
    private String password;

    private String username;

    /**
     * Subscriptions per Redis connection limit
     */
    private int subscriptionsPerConnection = 5;

    /**
     * Name of client connection
     */
    private String clientName;

    private boolean sslEnableEndpointIdentification = true;

    private SslProvider sslProvider = SslProvider.JDK;

    private URL sslTruststore;

    private String sslTruststorePassword;

    private URL sslKeystore;

    private String sslKeystorePassword;

    private int pingConnectionInterval;

    private boolean keepAlive;

    private boolean tcpNoDelay;

}
