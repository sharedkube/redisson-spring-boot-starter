package sharedkube.redisson.core.properties;

import java.util.concurrent.ExecutorService;

import org.redisson.client.DefaultNettyHook;
import org.redisson.client.NettyHook;
import org.redisson.codec.DefaultReferenceCodecProvider;
import org.redisson.codec.ReferenceCodecProvider;
import org.redisson.config.TransportMode;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import io.netty.channel.EventLoopGroup;
import lombok.Getter;
import lombok.Setter;
import sharedkube.redisson.core.config.ClusterServersConfig;
import sharedkube.redisson.core.config.MasterSlaveServersConfig;
import sharedkube.redisson.core.config.ReplicatedServersConfig;
import sharedkube.redisson.core.config.SentinelServersConfig;
import sharedkube.redisson.core.config.SingleServerConfig;
import sharedkube.redisson.core.enums.AddressResolverGroupFactoryEnum;
import sharedkube.redisson.core.enums.CodecEnum;
import sharedkube.redisson.core.enums.RedissServerModeEnum;

/**
 * Redisson configuration properties {@code org.redisson.config.Config}
 * 
 * @author huahouye@gmail.com
 * @date 2020/08/14
 */
@Setter
@Getter
public class RedissonProperties {

    /**
     * Threads amount shared between all redis node clients
     */
    private int threads = 16;

    private int nettyThreads = 32;

    /**
     * Redis key/value codec. FST codec is used by default
     */
    private CodecEnum codec = CodecEnum.JSON_JACKSON_CODEC;

    private ExecutorService executor;

    /**
     * Config option for enabling Redisson Reference feature. Default value is TRUE
     */
    private boolean referenceEnabled = true;

    private TransportMode transportMode = TransportMode.NIO;

    private EventLoopGroup eventLoopGroup;

    /**
     * 30000 ms by default
     */
    private long lockWatchdogTimeout = 30 * 1000;

    private boolean keepPubSubOrder = true;

    private boolean decodeInExecutor = false;

    private boolean useScriptCache = false;

    private int minCleanUpDelay = 5;

    private int maxCleanUpDelay = 30 * 60;

    private int cleanUpKeysAmount = 100;

    private NettyHook nettyHook = new DefaultNettyHook();

    private boolean useThreadClassLoader = true;

    /**
     * AddressResolverGroupFactory switch between default and round robin, use DnsAddressResolverGroupFactory by default
     */
    private AddressResolverGroupFactoryEnum addressResolverGroupFactory = AddressResolverGroupFactoryEnum.DEFAULT;

    /**
     * Codec 注册和获取功能的提供者，默认值：DefaultReferenceCodecProvider
     */
    private ReferenceCodecProvider referenceCodecProvider = new DefaultReferenceCodecProvider();

    /**
     * Redis server mode, use for initial redisson instance，single by default
     */
    private RedissServerModeEnum mode = RedissServerModeEnum.SINGLE;

    /**
     * single mode
     */
    @NestedConfigurationProperty
    private SingleServerConfig single = new SingleServerConfig();

    /**
     * cluster mode
     */
    @NestedConfigurationProperty
    private ClusterServersConfig cluster = new ClusterServersConfig();

    /**
     * master-slave mode
     */
    @NestedConfigurationProperty
    private MasterSlaveServersConfig masterSlave = new MasterSlaveServersConfig();

    /**
     * sentinel mode
     */
    @NestedConfigurationProperty
    private SentinelServersConfig sentinel = new SentinelServersConfig();

    /**
     * cloud mode
     */
    @NestedConfigurationProperty
    private ReplicatedServersConfig replicated = new ReplicatedServersConfig();

}
