package sharedkube.redisson.core.spring.data.connection;

import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;

import lombok.extern.slf4j.Slf4j;
import sharedkube.redisson.core.exception.MultiRedissionsNotFoundException;

@Slf4j
public class MutilRedissonsConnectionFactory implements RedisConnectionFactory {

    private String defaultRoutingKey;
    private Map<String, RedisConnectionFactory> redisConnectionFactoryMap;

    public MutilRedissonsConnectionFactory(String defaultRedisson, Map<String, RedisConnectionFactory> factoryMap) {
        this.defaultRoutingKey = defaultRedisson;
        this.redisConnectionFactoryMap = factoryMap;
    }

    @Override
    public RedisConnection getConnection() {
        return getFactoryInstance().getConnection();
    }

    @Override
    public RedisClusterConnection getClusterConnection() {
        return getFactoryInstance().getClusterConnection();
    }

    @Override
    public boolean getConvertPipelineAndTxResults() {
        return getFactoryInstance().getConvertPipelineAndTxResults();
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        return getFactoryInstance().getSentinelConnection();
    }

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException e) {
        return getFactoryInstance().translateExceptionIfPossible(e);
    }

    private RedisConnectionFactory getFactoryInstance() {

        String routingKey = Optional.ofNullable(MultiRedissonsRoutingManager.getRoutingKey()).orElse(defaultRoutingKey);

        if (log.isDebugEnabled()) {
            log.debug("routing key '{}'", routingKey);
        }

        RedisConnectionFactory redisConnectionFactory = redisConnectionFactoryMap.get(routingKey);

        if (redisConnectionFactory == null) {
            throw new MultiRedissionsNotFoundException(
                String.format("'%s' redis connect factory not found", routingKey));
        }

        return redisConnectionFactory;
    }

}
