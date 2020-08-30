package sharedkube.redisson.core.config;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

/**
 * {@link org.redisson.config.MasterSlaveServersConfig }
 * 
 * @author houyehua@gmail.com
 *
 */
@Getter
@Setter
public class MasterSlaveServersConfig extends BaseMasterSlaveServersConfig {

    /**
     * Redis slave servers addresses
     */
    private Set<String> slaveAddresses = new HashSet<String>();

    /**
     * Redis master server address
     */
    private String masterAddress;

    /**
     * Database index used for Redis connection
     */
    private int database = 0;

}