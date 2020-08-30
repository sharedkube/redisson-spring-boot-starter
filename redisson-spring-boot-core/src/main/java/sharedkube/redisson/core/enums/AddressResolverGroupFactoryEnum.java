package sharedkube.redisson.core.enums;

import org.redisson.connection.AddressResolverGroupFactory;
import org.redisson.connection.DnsAddressResolverGroupFactory;
import org.redisson.connection.RoundRobinDnsAddressResolverGroupFactory;

/**
 * @author huahouye@gmail.com
 * @date 2020/08/15
 */
public enum AddressResolverGroupFactoryEnum {

    DEFAULT {
        @Override
        public AddressResolverGroupFactory getInstance() {
            return new DnsAddressResolverGroupFactory();
        }
    },

    /**
     * round robin
     */
    ROUND_ROBIN {
        @Override
        public AddressResolverGroupFactory getInstance() {
            return new RoundRobinDnsAddressResolverGroupFactory();
        }
    };

    public abstract AddressResolverGroupFactory getInstance();

}