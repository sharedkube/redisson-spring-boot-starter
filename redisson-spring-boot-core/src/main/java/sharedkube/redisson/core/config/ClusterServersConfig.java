package sharedkube.redisson.core.config;

import org.redisson.api.NatMapper;

import lombok.Getter;
import lombok.Setter;

/**
 * {@link org.redisson.config.ClusterServersConfig}
 * 
 * @author houyehua@gmail.com
 *
 */
@Getter
@Setter
public class ClusterServersConfig extends BaseMasterSlaveServersConfig {

    private NatMapper natMapper = NatMapper.direct();

    /**
     * Redis cluster node urls list
     */
    private String[] nodeAddresses = {};

    /**
     * Redis cluster scan interval in milliseconds
     */
    private int scanInterval = 5000;

    private boolean checkSlotsCoverage = true;

}
