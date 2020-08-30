package sharedkube.redisson.core.enums;

import org.redisson.connection.balancer.LoadBalancer;
import org.redisson.connection.balancer.RandomLoadBalancer;
import org.redisson.connection.balancer.RoundRobinLoadBalancer;

/**
 * @author huahouye@gmail.com
 * @date 2020/08/15
 */
public enum LoadBalancerEnum {

    /**
     * round robin
     */
    ROUND_ROBIN {
        @Override
        public LoadBalancer getInstance() {
            return new RoundRobinLoadBalancer();
        }
    },

    WEIGHTED_ROUND_ROBIN {
        @Override
        public LoadBalancer getInstance() {
            throw new UnsupportedOperationException("please create a Customizer<Config>.");
        }
    },

    /**
     * random
     */
    RANDOM {
        @Override
        public LoadBalancer getInstance() {
            return new RandomLoadBalancer();
        }
    };

    public abstract LoadBalancer getInstance();

}