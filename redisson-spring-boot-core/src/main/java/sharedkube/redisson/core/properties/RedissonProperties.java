package sharedkube.redisson.core.properties;

import java.util.concurrent.ExecutorService;

import org.redisson.config.Config;
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
import sharedkube.redisson.core.enums.NettyHookEnum;
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
     * Redis key/value codec. MarshallingCodec codec is used by default
     */
    private CodecEnum codec = CodecEnum.MARSHALLING_CODEC;

    private ExecutorService executor;

    /**
     * Config option for enabling Redisson Reference feature. Default value is TRUE
     */
    private boolean referenceEnabled = true;

    private TransportMode transportMode = TransportMode.NIO;

    private EventLoopGroup eventLoopGroup; // TODO share or split EventLoopGroup

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

    private NettyHookEnum nettyHook = NettyHookEnum.DEFAULT_NETTY_HOOK;

    private boolean useThreadClassLoader = true;

    /**
     * AddressResolverGroupFactory switch between default and round robin, use DnsAddressResolverGroupFactory by default
     */
    private AddressResolverGroupFactoryEnum addressResolverGroupFactory = AddressResolverGroupFactoryEnum.DEFAULT;

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

    public Config toRedissonConfig() {
        Config config = new Config();
        initRedissonConfig(config);
        switch (mode) {
            case SINGLE:
                initRedissonSingleServerConfig(config);
                break;
            case MASTER_SLAVE:
                initRedissonMasterSlaveServersConfig(config);
                break;
            case SENTINEL:
                initSentinelServersConfig(config);
                break;
            case CLUSTER:
                initClusterServersConfig(config);
                break;
            case REPLICATED:
                initReplicatedServersConfig(config);
                break;
            default:
                throw new IllegalArgumentException("illegal redisson server mode: " + mode);
        }
        return config;
    }

    /**
     * 初始化 org.redisson.config.Config 部分
     * 
     * @param config
     */
    private void initRedissonConfig(Config config) {
        // @formatter:off
        config
            .setThreads(threads)
            .setNettyThreads(nettyThreads)
            .setCodec(codec.getInstance())
            .setExecutor(executor)
            .setTransportMode(transportMode)
            .setEventLoopGroup(eventLoopGroup)
            .setLockWatchdogTimeout(lockWatchdogTimeout)
            .setKeepPubSubOrder(keepPubSubOrder)
            // .setDecodeInExecutor(decodeInExecutor) // FIXME 
            .setUseScriptCache(useScriptCache)
            .setMinCleanUpDelay(minCleanUpDelay)
            .setMaxCleanUpDelay(maxCleanUpDelay)
            .setCleanUpKeysAmount(cleanUpKeysAmount)
            .setNettyHook(nettyHook.getInstance())
            .setUseThreadClassLoader(useThreadClassLoader)
            .setAddressResolverGroupFactory(addressResolverGroupFactory.getInstance())
            .setReferenceEnabled(referenceEnabled);
        // @formatter:on
    }

    /**
     * 初始化 org.redisson.config.BaseConfig 部分
     * 
     * @param redissonBaseConfig
     * @param sharedkubeBaseConfig
     */
    private void initRedissonBaseConfig(org.redisson.config.BaseConfig<?> redissonBaseConfig,
        sharedkube.redisson.core.config.BaseConfig sharedkubeBaseConfig) {
        redissonBaseConfig.setIdleConnectionTimeout(sharedkubeBaseConfig.getIdleConnectionTimeout())
            .setConnectTimeout(sharedkubeBaseConfig.getConnectTimeout()).setTimeout(sharedkubeBaseConfig.getTimeout())
            .setRetryAttempts(sharedkubeBaseConfig.getRetryAttempts())
            .setRetryInterval(sharedkubeBaseConfig.getRetryInterval()).setUsername(sharedkubeBaseConfig.getUsername())
            .setPassword(sharedkubeBaseConfig.getPassword())
            .setSubscriptionsPerConnection(sharedkubeBaseConfig.getSubscriptionsPerConnection())
            .setClientName(sharedkubeBaseConfig.getClientName())
            .setSslEnableEndpointIdentification(sharedkubeBaseConfig.isSslEnableEndpointIdentification())
            .setSslProvider(sharedkubeBaseConfig.getSslProvider())
            .setSslTruststore(sharedkubeBaseConfig.getSslTruststore())
            .setSslTruststorePassword(sharedkubeBaseConfig.getSslTruststorePassword())
            .setPingConnectionInterval(sharedkubeBaseConfig.getPingConnectionInterval())
            .setKeepAlive(sharedkubeBaseConfig.isKeepAlive()).setTcpNoDelay(sharedkubeBaseConfig.isTcpNoDelay());
    }

    private void initRedissonSingleServerConfig(Config config) {
        // @formatter:off
        config.useSingleServer()
            .setAddress(single.getAddress())
            .setSubscriptionConnectionMinimumIdleSize(single.getSubscriptionConnectionMinimumIdleSize())
            .setSubscriptionConnectionPoolSize(single.getSubscriptionConnectionPoolSize())
            .setConnectionMinimumIdleSize(single.getConnectionMinimumIdleSize())
            .setConnectionPoolSize(single.getConnectionPoolSize())
            .setDatabase(single.getDatabase())
            .setDnsMonitoringInterval(single.getDnsMonitoringInterval())
            .setTimeout(single.getTimeout());
        //
        initRedissonBaseConfig(config.useSingleServer(), single);
        // @formatter:on
    }

    private void initRedissonBaseMasterSlaveServersConfig(
        org.redisson.config.BaseMasterSlaveServersConfig<?> redissonBaseMasterSlaveServersConfig,
        sharedkube.redisson.core.config.BaseMasterSlaveServersConfig sharedkubeBaseMasterSlaveServersConfig) {
        redissonBaseMasterSlaveServersConfig.setLoadBalancer(sharedkubeBaseMasterSlaveServersConfig.getLoadBalancer().getInstance())
            .setMasterConnectionPoolSize(sharedkubeBaseMasterSlaveServersConfig.getMasterConnectionPoolSize())
            .setSlaveConnectionPoolSize(sharedkubeBaseMasterSlaveServersConfig.getSlaveConnectionPoolSize())
            .setSubscriptionConnectionPoolSize(sharedkubeBaseMasterSlaveServersConfig.getSubscriptionConnectionPoolSize())
            .setMasterConnectionMinimumIdleSize(sharedkubeBaseMasterSlaveServersConfig.getMasterConnectionMinimumIdleSize())
            .setSlaveConnectionMinimumIdleSize(sharedkubeBaseMasterSlaveServersConfig.getSlaveConnectionMinimumIdleSize())
            .setSubscriptionConnectionMinimumIdleSize(sharedkubeBaseMasterSlaveServersConfig.getSubscriptionConnectionMinimumIdleSize())
            .setReadMode(sharedkubeBaseMasterSlaveServersConfig.getReadMode())
            .setSubscriptionMode(sharedkubeBaseMasterSlaveServersConfig.getSubscriptionMode())
            .setDnsMonitoringInterval(sharedkubeBaseMasterSlaveServersConfig.getDnsMonitoringInterval())
            .setFailedSlaveCheckInterval(sharedkubeBaseMasterSlaveServersConfig.getFailedSlaveCheckInterval())
            .setFailedSlaveReconnectionInterval(sharedkubeBaseMasterSlaveServersConfig.getFailedSlaveReconnectionInterval());
        //
        initRedissonBaseConfig(redissonBaseMasterSlaveServersConfig, sharedkubeBaseMasterSlaveServersConfig);
    }

    private void initRedissonMasterSlaveServersConfig(Config config) {
        // @formatter:off
        config.useMasterSlaveServers()
            .setMasterAddress(masterSlave.getMasterAddress())
            .setDatabase(masterSlave.getDatabase())
            .setSlaveAddresses(masterSlave.getSlaveAddresses());
        //
        initRedissonBaseMasterSlaveServersConfig(config.useMasterSlaveServers(), masterSlave);
        // @formatter:on
    }

    private void initSentinelServersConfig(Config config) {
        // @formatter:off
        config.useSentinelServers()
            .setMasterName(sentinel.getMasterName())
            .setDatabase(sentinel.getDatabase())
            .setScanInterval(sentinel.getScanInterval())
            .setNatMapper(sentinel.getNatMapper())
            .addSentinelAddress(sentinel.getSentinelAddresses());
        //
        initRedissonBaseMasterSlaveServersConfig(config.useSentinelServers(), sentinel);
        // @formatter:on
    }

    private void initClusterServersConfig(Config config) {
        // @formatter:off
        config.useClusterServers()
            .setNatMapper(cluster.getNatMapper())
            .addNodeAddress(cluster.getNodeAddresses())
            .setScanInterval(cluster.getScanInterval())
            .setCheckSlotsCoverage(cluster.isCheckSlotsCoverage());
        //
        initRedissonBaseMasterSlaveServersConfig(config.useClusterServers(), cluster);
        // @formatter:on
    }

    private void initReplicatedServersConfig(Config config) {
        // @formatter:off
        config.useReplicatedServers()
            .addNodeAddress(replicated.getNodeAddresses())
            .setScanInterval(replicated.getScanInterval())
            .setDatabase(replicated.getDatabase());
        //
        initRedissonBaseMasterSlaveServersConfig(config.useReplicatedServers(), replicated);
        // @formatter:on
    }
}
