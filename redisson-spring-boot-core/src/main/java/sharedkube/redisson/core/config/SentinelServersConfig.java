package sharedkube.redisson.core.config;

import org.redisson.api.NatMapper;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code org.redisson.config.SentinelServersConfig}
 * 
 * @author houyehua@gmail.com
 *
 */
@Getter
@Setter
public class SentinelServersConfig extends BaseMasterSlaveServersConfig {

    private String[] sentinelAddresses = {};

    private NatMapper natMapper = NatMapper.direct();

    private String masterName;

    /**
     * Database index used for Redis connection
     */
    private int database = 0;

    /**
     * Sentinel scan interval in milliseconds, default 1000 ms
     */
    private int scanInterval = 1000;

    private boolean checkSentinelsList = true;

}