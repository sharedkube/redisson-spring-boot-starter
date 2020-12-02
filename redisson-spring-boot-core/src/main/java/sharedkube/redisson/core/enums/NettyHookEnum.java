package sharedkube.redisson.core.enums;

import org.redisson.client.DefaultNettyHook;
import org.redisson.client.NettyHook;

/**
 * @author huahouye@gmail.com
 * @date 2020/08/15
 */
public enum NettyHookEnum {

    // default
    DEFAULT_NETTY_HOOK {
        @Override
        public NettyHook getInstance() {
            // default
            return new DefaultNettyHook();
        }
    },

    ;

    public abstract NettyHook getInstance();

}