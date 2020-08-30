package sharedkube.redisson.core.config;

import org.redisson.config.ReadMode;
import org.redisson.config.SubscriptionMode;

import lombok.Getter;
import lombok.Setter;
import sharedkube.redisson.core.enums.LoadBalancerEnum;

/**
 * {@link org.redisson.config.BaseMasterSlaveServersConfig}
 * 
 * @author houyehua@gmail.com
 *
 */
@Getter
@Setter
public class BaseMasterSlaveServersConfig extends BaseConfig {

    /**
     * Сonnection load balancer for multiple Redis slave servers
     */
    private LoadBalancerEnum loadBalancer = LoadBalancerEnum.ROUND_ROBIN;
    /**
     * Redis 'slave' node minimum idle connection amount for <b>each</b> slave node
     */
    private int slaveConnectionMinimumIdleSize = 24;

    /**
     * Redis 'slave' node maximum connection pool size for <b>each</b> slave node
     */
    private int slaveConnectionPoolSize = 64;

    private int failedSlaveReconnectionInterval = 3000;

    private int failedSlaveCheckInterval = 180000;

    /**
     * Redis 'master' node minimum idle connection amount for <b>each</b> slave node
     */
    private int masterConnectionMinimumIdleSize = 24;

    /**
     * Redis 'master' node maximum connection pool size
     */
    private int masterConnectionPoolSize = 64;

    private ReadMode readMode = ReadMode.SLAVE;

    private SubscriptionMode subscriptionMode = SubscriptionMode.MASTER;

    /**
     * Redis 'slave' node minimum idle subscription (pub/sub) connection amount for <b>each</b> slave node
     */
    private int subscriptionConnectionMinimumIdleSize = 1;

    /**
     * Redis 'slave' node maximum subscription (pub/sub) connection pool size for <b>each</b> slave node
     */
    private int subscriptionConnectionPoolSize = 50;

    private long dnsMonitoringInterval = 5000;

}