package sharedkube.redisson.core.spring.data.connection;

import java.util.Map;
import java.util.Objects;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.util.StringUtils;

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
        return getConnectionsFactory().getConnection();
    }

    @Override
    public RedisClusterConnection getClusterConnection() {
        return getConnectionsFactory().getClusterConnection();
    }

    @Override
    public boolean getConvertPipelineAndTxResults() {
        return getConnectionsFactory().getConvertPipelineAndTxResults();
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        return getConnectionsFactory().getSentinelConnection();
    }

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException e) {
        return getConnectionsFactory().translateExceptionIfPossible(e);
    }

    private RedisConnectionFactory getConnectionsFactory() {

        String routingKey = MultiRedissonsRoutingManager.getRoutingKey();

        if (StringUtils.isEmpty(routingKey)) {
            if (log.isWarnEnabled()) {
                log.warn("routing key is empty! default value will be setted.");
            }

            routingKey = defaultRoutingKey;
        }

        RedisConnectionFactory redisConnectionFactory = redisConnectionFactoryMap.get(routingKey);

        if (Objects.isNull(redisConnectionFactory)) {
            throw new MultiRedissionsNotFoundException(
                String.format("'%s' redis connect factory not found", routingKey));
        }

        return redisConnectionFactory;
    }

}
