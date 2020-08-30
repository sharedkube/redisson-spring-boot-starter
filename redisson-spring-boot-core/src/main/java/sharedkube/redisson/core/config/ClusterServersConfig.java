package sharedkube.redisson.core.config;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Redis cluster node urls list
     */
    private List<String> nodeAddresses = new ArrayList<>();

    /**
     * Redis cluster scan interval in milliseconds
     */
    private int scanInterval = 5000;

    private boolean checkSlotsCoverage = true;

}
