package sharedkube.redisson.core.config;

import java.util.ArrayList;
import java.util.List;

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

    private List<String> sentinelAddresses = new ArrayList<>();

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