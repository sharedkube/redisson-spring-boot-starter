package sharedkube.redisson.core.config;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code org.redisson.config.SingleServerConfig}
 * 
 * @author houyehua@gmail.com
 *
 */
@Getter
@Setter
public class SingleServerConfig extends BaseConfig {

    /**
     * Redis server address
     *
     */
    private String address = "redis://127.0.0.1:6379";

    /**
     * Minimum idle subscription connection amount
     */
    private int subscriptionConnectionMinimumIdleSize = 1;

    /**
     * Redis subscription connection maximum pool size
     *
     */
    private int subscriptionConnectionPoolSize = 50;

    /**
     * Minimum idle Redis connection amount
     */
    private int connectionMinimumIdleSize = 24;

    /**
     * Redis connection maximum pool size
     */
    private int connectionPoolSize = 64;

    /**
     * Database index used for Redis connection
     */
    private int database = 0;

    /**
     * Interval in milliseconds to check DNS, default 5000 ms
     */
    private long dnsMonitoringInterval = 5000;

}
