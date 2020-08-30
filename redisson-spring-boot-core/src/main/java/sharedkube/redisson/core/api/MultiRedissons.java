package sharedkube.redisson.core.api;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.redisson.api.BatchOptions;
import org.redisson.api.ClusterNodesGroup;
import org.redisson.api.ExecutorOptions;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.MapOptions;
import org.redisson.api.Node;
import org.redisson.api.NodesGroup;
import org.redisson.api.RAtomicDouble;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBatch;
import org.redisson.api.RBinaryStream;
import org.redisson.api.RBitSet;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RBoundedBlockingQueue;
import org.redisson.api.RBucket;
import org.redisson.api.RBuckets;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RDeque;
import org.redisson.api.RDoubleAdder;
import org.redisson.api.RGeo;
import org.redisson.api.RHyperLogLog;
import org.redisson.api.RKeys;
import org.redisson.api.RLexSortedSet;
import org.redisson.api.RList;
import org.redisson.api.RListMultimap;
import org.redisson.api.RListMultimapCache;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RLock;
import org.redisson.api.RLongAdder;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RPatternTopic;
import org.redisson.api.RPermitExpirableSemaphore;
import org.redisson.api.RPriorityBlockingDeque;
import org.redisson.api.RPriorityBlockingQueue;
import org.redisson.api.RPriorityDeque;
import org.redisson.api.RPriorityQueue;
import org.redisson.api.RQueue;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RRemoteService;
import org.redisson.api.RRingBuffer;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RScript;
import org.redisson.api.RSemaphore;
import org.redisson.api.RSet;
import org.redisson.api.RSetCache;
import org.redisson.api.RSetMultimap;
import org.redisson.api.RSetMultimapCache;
import org.redisson.api.RSortedSet;
import org.redisson.api.RStream;
import org.redisson.api.RTimeSeries;
import org.redisson.api.RTopic;
import org.redisson.api.RTransaction;
import org.redisson.api.RTransferQueue;
import org.redisson.api.RedissonClient;
import org.redisson.api.TransactionOptions;
import org.redisson.api.redisnode.BaseRedisNodes;
import org.redisson.api.redisnode.RedisNodes;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;
import sharedkube.redisson.core.exception.MultiRedissionsNotFoundException;
import sharedkube.redisson.core.spring.data.connection.MultiRedissonsRoutingManager;

@Slf4j
public class MultiRedissons implements RedissonClient {

    private String defaultRedisson;
    private Map<String, RedissonClient> redissonClientMap;

    public MultiRedissons(String defaultRedisson, Map<String, RedissonClient> redissonClientMap) {
        this.defaultRedisson = defaultRedisson;
        this.redissonClientMap = redissonClientMap;
    }

    public RedissonClient getRedissonClient() {

        String routingKey = Optional.ofNullable(MultiRedissonsRoutingManager.getRoutingKey()).orElse(defaultRedisson);
        RedissonClient redissonClient = redissonClientMap.get(routingKey);

        if (log.isDebugEnabled()) {
            log.debug("redis routing to '{}'", routingKey);
        }

        if (redissonClient == null) {
            throw new MultiRedissionsNotFoundException(String.format("'%s' instance not found", routingKey));
        }

        return redissonClient;
    }

    public RedissonClient getRedissonClient(String instance) {

        RedissonClient redissonClient = redissonClientMap.get(instance);
        Assert.notNull(redissonClient, String.format("'%s' instance not found", instance));
        return redissonClient;
    }

    @Override
    public void shutdown() {
        shutdown(0, 2, TimeUnit.SECONDS);
    }

    @Override
    public void shutdown(long quietPeriod, long timeout, TimeUnit unit) {
        if (log.isInfoEnabled()) {
            log.info("now to shut down multi redisson client quietPeriod {} timeout {} unit {}", quietPeriod, timeout,
                unit);
        }
        for (Map.Entry<String, RedissonClient> entry : redissonClientMap.entrySet()) {
            if (log.isInfoEnabled()) {
                log.info("shutting down '{}' redisson client", entry.getKey());
            }
            final RedissonClient client = entry.getValue();
            if (client.isShutdown() || client.isShuttingDown()) {
                if (log.isInfoEnabled()) {
                    log.info("client is already shutdown [{}] or is shutting down [{}]", client.isShutdown(),
                        client.isShuttingDown());
                }
            } else {
                client.shutdown(quietPeriod, timeout, unit);
            }
        }
        if (log.isInfoEnabled()) {
            log.info("shut down multi redisson client completed");
        }
    }

    @Override
    public boolean isShutdown() {
        return redissonClientMap.values().stream().allMatch(RedissonClient::isShutdown);
    }

    @Override
    public boolean isShuttingDown() {
        return redissonClientMap.values().stream().anyMatch(RedissonClient::isShuttingDown);
    }

    @Override
    public <V> RTimeSeries<V> getTimeSeries(String s) {
        return getRedissonClient().getTimeSeries(s);
    }

    @Override
    public <V> RTimeSeries<V> getTimeSeries(String s, Codec codec) {
        return getRedissonClient().getTimeSeries(s, codec);
    }

    @Override
    public <K, V> RStream<K, V> getStream(String name) {
        return getRedissonClient().getStream(name);
    }

    @Override
    public <K, V> RStream<K, V> getStream(String name, Codec codec) {
        return getRedissonClient().getStream(name, codec);
    }

    @Override
    public RRateLimiter getRateLimiter(String name) {
        return getRedissonClient().getRateLimiter(name);
    }

    @Override
    public RBinaryStream getBinaryStream(String name) {
        return getRedissonClient().getBinaryStream(name);
    }

    @Override
    public <V> RGeo<V> getGeo(String name) {
        return getRedissonClient().getGeo(name);
    }

    @Override
    public <V> RGeo<V> getGeo(String name, Codec codec) {
        return getRedissonClient().getGeo(name, codec);
    }

    @Override
    public <V> RSetCache<V> getSetCache(String name) {
        return getRedissonClient().getSetCache(name);
    }

    @Override
    public <V> RSetCache<V> getSetCache(String name, Codec codec) {
        return getRedissonClient().getSetCache(name, codec);
    }

    @Override
    public <K, V> RMapCache<K, V> getMapCache(String name, Codec codec) {
        return getRedissonClient().getMapCache(name, codec);
    }

    @Override
    public <K, V> RMapCache<K, V> getMapCache(String name, Codec codec, MapOptions<K, V> options) {
        return getRedissonClient().getMapCache(name, codec, options);
    }

    @Override
    public <K, V> RMapCache<K, V> getMapCache(String name) {
        return getRedissonClient().getMapCache(name);
    }

    @Override
    public <K, V> RMapCache<K, V> getMapCache(String name, MapOptions<K, V> options) {
        return getRedissonClient().getMapCache(name, options);
    }

    @Override
    public <V> RBucket<V> getBucket(String name) {
        return getRedissonClient().getBucket(name);
    }

    @Override
    public <V> RBucket<V> getBucket(String name, Codec codec) {
        return getRedissonClient().getBucket(name, codec);
    }

    @Override
    public RBuckets getBuckets() {
        return getRedissonClient().getBuckets();
    }

    @Override
    public RBuckets getBuckets(Codec codec) {
        return getRedissonClient().getBuckets(codec);
    }

    @Override
    public <V> RHyperLogLog<V> getHyperLogLog(String name) {
        return getRedissonClient().getHyperLogLog(name);
    }

    @Override
    public <V> RHyperLogLog<V> getHyperLogLog(String name, Codec codec) {
        return getRedissonClient().getHyperLogLog(name, codec);
    }

    @Override
    public <V> RList<V> getList(String name) {
        return getRedissonClient().getList(name);
    }

    @Override
    public <V> RList<V> getList(String name, Codec codec) {
        return getRedissonClient().getList(name, codec);
    }

    @Override
    public <K, V> RListMultimap<K, V> getListMultimap(String name) {
        return getRedissonClient().getListMultimap(name);
    }

    @Override
    public <K, V> RListMultimap<K, V> getListMultimap(String name, Codec codec) {
        return getRedissonClient().getListMultimap(name, codec);
    }

    @Override
    public <K, V> RListMultimapCache<K, V> getListMultimapCache(String name) {
        return getRedissonClient().getListMultimapCache(name);
    }

    @Override
    public <K, V> RListMultimapCache<K, V> getListMultimapCache(String name, Codec codec) {
        return getRedissonClient().getListMultimapCache(name, codec);
    }

    @Override
    public <K, V> RLocalCachedMap<K, V> getLocalCachedMap(String name, LocalCachedMapOptions<K, V> options) {
        return getRedissonClient().getLocalCachedMap(name, options);
    }

    @Override
    public <K, V> RLocalCachedMap<K, V> getLocalCachedMap(String name, Codec codec,
        LocalCachedMapOptions<K, V> options) {
        return getRedissonClient().getLocalCachedMap(name, codec, options);
    }

    @Override
    public <K, V> RMap<K, V> getMap(String name) {
        return getRedissonClient().getMap(name);
    }

    @Override
    public <K, V> RMap<K, V> getMap(String name, MapOptions<K, V> options) {
        return getRedissonClient().getMap(name, options);
    }

    @Override
    public <K, V> RMap<K, V> getMap(String name, Codec codec) {
        return getRedissonClient().getMap(name, codec);
    }

    @Override
    public <K, V> RMap<K, V> getMap(String name, Codec codec, MapOptions<K, V> options) {
        return getRedissonClient().getMap(name, codec, options);
    }

    @Override
    public <K, V> RSetMultimap<K, V> getSetMultimap(String name) {
        return getRedissonClient().getSetMultimap(name);
    }

    @Override
    public <K, V> RSetMultimap<K, V> getSetMultimap(String name, Codec codec) {
        return getRedissonClient().getSetMultimap(name, codec);
    }

    @Override
    public <K, V> RSetMultimapCache<K, V> getSetMultimapCache(String name) {
        return getRedissonClient().getSetMultimapCache(name);
    }

    @Override
    public <K, V> RSetMultimapCache<K, V> getSetMultimapCache(String name, Codec codec) {
        return getRedissonClient().getSetMultimapCache(name, codec);
    }

    @Override
    public RSemaphore getSemaphore(String name) {
        return getRedissonClient().getSemaphore(name);
    }

    @Override
    public RPermitExpirableSemaphore getPermitExpirableSemaphore(String name) {
        return getRedissonClient().getPermitExpirableSemaphore(name);
    }

    @Override
    public RLock getLock(String name) {
        return getRedissonClient().getLock(name);
    }

    @Override
    public RLock getMultiLock(RLock... locks) {
        return getRedissonClient().getMultiLock(locks);
    }

    @Override
    public RLock getRedLock(RLock... locks) {
        return getRedissonClient().getRedLock(locks);
    }

    @Override
    public RLock getFairLock(String name) {
        return getRedissonClient().getFairLock(name);
    }

    @Override
    public RReadWriteLock getReadWriteLock(String name) {
        return getRedissonClient().getReadWriteLock(name);
    }

    @Override
    public <V> RSet<V> getSet(String name) {
        return getRedissonClient().getSet(name);
    }

    @Override
    public <V> RSet<V> getSet(String name, Codec codec) {
        return getRedissonClient().getSet(name, codec);
    }

    @Override
    public <V> RSortedSet<V> getSortedSet(String name) {
        return getRedissonClient().getSortedSet(name);
    }

    @Override
    public <V> RSortedSet<V> getSortedSet(String name, Codec codec) {
        return getRedissonClient().getSortedSet(name, codec);
    }

    @Override
    public <V> RScoredSortedSet<V> getScoredSortedSet(String name) {
        return getRedissonClient().getScoredSortedSet(name);
    }

    @Override
    public <V> RScoredSortedSet<V> getScoredSortedSet(String name, Codec codec) {
        return getRedissonClient().getScoredSortedSet(name, codec);
    }

    @Override
    public RLexSortedSet getLexSortedSet(String name) {
        return getRedissonClient().getLexSortedSet(name);
    }

    @Override
    public RTopic getTopic(String name) {
        return getRedissonClient().getTopic(name);
    }

    @Override
    public RTopic getTopic(String name, Codec codec) {
        return getRedissonClient().getTopic(name, codec);
    }

    @Override
    public RPatternTopic getPatternTopic(String pattern) {
        return getRedissonClient().getPatternTopic(pattern);
    }

    @Override
    public RPatternTopic getPatternTopic(String pattern, Codec codec) {
        return getRedissonClient().getPatternTopic(pattern, codec);
    }

    @Override
    public <V> RQueue<V> getQueue(String name) {
        return getRedissonClient().getQueue(name);
    }

    @Override
    public <V> RTransferQueue<V> getTransferQueue(String s) {
        return getRedissonClient().getTransferQueue(s);
    }

    @Override
    public <V> RTransferQueue<V> getTransferQueue(String s, Codec codec) {
        return getRedissonClient().getTransferQueue(s, codec);
    }

    @Override
    public <V> RDelayedQueue<V> getDelayedQueue(RQueue<V> destinationQueue) {
        return getRedissonClient().getDelayedQueue(destinationQueue);
    }

    @Override
    public <V> RQueue<V> getQueue(String name, Codec codec) {
        return getRedissonClient().getQueue(name, codec);
    }

    @Override
    public <V> RRingBuffer<V> getRingBuffer(String name) {
        return getRedissonClient().getRingBuffer(name);
    }

    @Override
    public <V> RRingBuffer<V> getRingBuffer(String name, Codec codec) {
        return getRedissonClient().getRingBuffer(name, codec);
    }

    @Override
    public <V> RPriorityQueue<V> getPriorityQueue(String name) {
        return getRedissonClient().getPriorityQueue(name);
    }

    @Override
    public <V> RPriorityQueue<V> getPriorityQueue(String name, Codec codec) {
        return getRedissonClient().getPriorityQueue(name, codec);
    }

    @Override
    public <V> RPriorityBlockingQueue<V> getPriorityBlockingQueue(String name) {
        return getRedissonClient().getPriorityBlockingQueue(name);
    }

    @Override
    public <V> RPriorityBlockingQueue<V> getPriorityBlockingQueue(String name, Codec codec) {
        return getRedissonClient().getPriorityBlockingQueue(name, codec);
    }

    @Override
    public <V> RPriorityBlockingDeque<V> getPriorityBlockingDeque(String name) {
        return getRedissonClient().getPriorityBlockingDeque(name);
    }

    @Override
    public <V> RPriorityBlockingDeque<V> getPriorityBlockingDeque(String name, Codec codec) {
        return getRedissonClient().getPriorityBlockingDeque(name, codec);
    }

    @Override
    public <V> RPriorityDeque<V> getPriorityDeque(String name) {
        return getRedissonClient().getPriorityDeque(name);
    }

    @Override
    public <V> RPriorityDeque<V> getPriorityDeque(String name, Codec codec) {
        return getRedissonClient().getPriorityDeque(name, codec);
    }

    @Override
    public <V> RBlockingQueue<V> getBlockingQueue(String name) {
        return getRedissonClient().getBlockingQueue(name);
    }

    @Override
    public <V> RBlockingQueue<V> getBlockingQueue(String name, Codec codec) {
        return getRedissonClient().getBlockingQueue(name, codec);
    }

    @Override
    public <V> RBoundedBlockingQueue<V> getBoundedBlockingQueue(String name) {
        return getRedissonClient().getBoundedBlockingQueue(name);
    }

    @Override
    public <V> RBoundedBlockingQueue<V> getBoundedBlockingQueue(String name, Codec codec) {
        return getRedissonClient().getBoundedBlockingQueue(name, codec);
    }

    @Override
    public <V> RDeque<V> getDeque(String name) {
        return getRedissonClient().getDeque(name);
    }

    @Override
    public <V> RDeque<V> getDeque(String name, Codec codec) {
        return getRedissonClient().getDeque(name, codec);
    }

    @Override
    public <V> RBlockingDeque<V> getBlockingDeque(String name) {
        return getRedissonClient().getBlockingDeque(name);
    }

    @Override
    public <V> RBlockingDeque<V> getBlockingDeque(String name, Codec codec) {
        return getRedissonClient().getBlockingDeque(name, codec);
    }

    @Override
    public RAtomicLong getAtomicLong(String name) {
        return getRedissonClient().getAtomicLong(name);
    }

    @Override
    public RAtomicDouble getAtomicDouble(String name) {
        return getRedissonClient().getAtomicDouble(name);
    }

    @Override
    public RLongAdder getLongAdder(String name) {
        return getRedissonClient().getLongAdder(name);
    }

    @Override
    public RDoubleAdder getDoubleAdder(String name) {
        return getRedissonClient().getDoubleAdder(name);
    }

    @Override
    public RCountDownLatch getCountDownLatch(String name) {
        return getRedissonClient().getCountDownLatch(name);
    }

    @Override
    public RBitSet getBitSet(String name) {
        return getRedissonClient().getBitSet(name);
    }

    @Override
    public <V> RBloomFilter<V> getBloomFilter(String name) {
        return getRedissonClient().getBloomFilter(name);
    }

    @Override
    public <V> RBloomFilter<V> getBloomFilter(String name, Codec codec) {
        return getRedissonClient().getBloomFilter(name, codec);
    }

    @Override
    public RScript getScript() {
        return getRedissonClient().getScript();
    }

    @Override
    public RScript getScript(Codec codec) {
        return getRedissonClient().getScript(codec);
    }

    @Override
    public RScheduledExecutorService getExecutorService(String name) {
        return getRedissonClient().getExecutorService(name);
    }

    @Override
    public RScheduledExecutorService getExecutorService(String name, ExecutorOptions options) {
        return getRedissonClient().getExecutorService(name, options);
    }

    @Override
    public RScheduledExecutorService getExecutorService(String name, Codec codec) {
        return getRedissonClient().getExecutorService(name, codec);
    }

    @Override
    public RScheduledExecutorService getExecutorService(String name, Codec codec, ExecutorOptions options) {
        return getRedissonClient().getExecutorService(name, codec, options);
    }

    @Override
    public RRemoteService getRemoteService() {
        return getRedissonClient().getRemoteService();
    }

    @Override
    public RRemoteService getRemoteService(Codec codec) {
        return getRedissonClient().getRemoteService(codec);
    }

    @Override
    public RRemoteService getRemoteService(String name) {
        return getRedissonClient().getRemoteService(name);
    }

    @Override
    public RRemoteService getRemoteService(String name, Codec codec) {
        return getRedissonClient().getRemoteService(name, codec);
    }

    @Override
    public RTransaction createTransaction(TransactionOptions options) {
        return getRedissonClient().createTransaction(options);
    }

    @Override
    public RBatch createBatch(BatchOptions options) {
        return getRedissonClient().createBatch(options);
    }

    @Override
    public RBatch createBatch() {
        return getRedissonClient().createBatch();
    }

    @Override
    public RKeys getKeys() {
        return getRedissonClient().getKeys();
    }

    @Override
    public RLiveObjectService getLiveObjectService() {
        return getRedissonClient().getLiveObjectService();
    }

    @Override
    public Config getConfig() {
        return getRedissonClient().getConfig();
    }

    @Override
    public <T extends BaseRedisNodes> T getRedisNodes(RedisNodes<T> redisNodes) {
        return getRedissonClient().getRedisNodes(redisNodes);
    }

    @Override
    public NodesGroup<Node> getNodesGroup() {
        return getRedissonClient().getNodesGroup();
    }

    @Override
    public ClusterNodesGroup getClusterNodesGroup() {
        return getRedissonClient().getClusterNodesGroup();
    }

    @Override
    public String getId() {
        return getRedissonClient().getId();
    }
}
