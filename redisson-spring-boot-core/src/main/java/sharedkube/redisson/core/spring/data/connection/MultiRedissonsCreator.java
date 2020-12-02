package sharedkube.redisson.core.spring.data.connection;

import java.util.LinkedHashMap;
import java.util.Map;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import sharedkube.redisson.core.properties.MultiRedissonsProperties;

public class MultiRedissonsCreator {

    public static Map<String, RedissonClient> createRedissonClientMap(MultiRedissonsProperties properties) {

        Map<String, RedissonClient> result = new LinkedHashMap<>();
        properties.getRedissons().forEach((k, v) -> {
            result.put(k, Redisson.create(v.toRedissonConfig()));
        });
        return result;
    }

    public static Map<String, RedisConnectionFactory>
        createRedisConnectionFactoryMap(Map<String, RedissonClient> redissonClientMap) {

        Map<String, RedisConnectionFactory> result = new LinkedHashMap<>();
        redissonClientMap.forEach((k, v) -> {
            result.put(k, new RedissonConnectionFactory(v));
        });
        return result;
    }

}
