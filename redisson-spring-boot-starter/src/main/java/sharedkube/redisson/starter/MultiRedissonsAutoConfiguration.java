package sharedkube.redisson.starter;

import java.util.Map;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import sharedkube.redisson.core.api.MultiRedissons;
import sharedkube.redisson.core.properties.MultiRedissonsProperties;
import sharedkube.redisson.core.spring.data.connection.MultiRedissonsCreator;
import sharedkube.redisson.core.spring.data.connection.MutilRedissonsConnectionFactory;

@Slf4j
@Configuration
@ConditionalOnClass({Redisson.class, RedisOperations.class})
@AutoConfigureBefore(RedisAutoConfiguration.class)
@EnableConfigurationProperties(MultiRedissonsProperties.class)
@ConditionalOnProperty(name = "sharedkube.multi-redissons.enabled", havingValue = "true", matchIfMissing = true)
public class MultiRedissonsAutoConfiguration {

    private String defaultRedisson;
    private Map<String, RedissonClient> redissonClientMap;
    private Map<String, RedisConnectionFactory> redisConnectionFactoryMap;

    public MultiRedissonsAutoConfiguration(MultiRedissonsProperties properties) {

        if (log.isDebugEnabled()) {
            log.debug("MultiRedissonsAutoConfiguration start init. MultiRedissonsProperties {}", properties);
        }

        this.defaultRedisson = properties.getDefaultRedisson();
        // check
        Assert.isTrue(!StringUtils.isEmpty(defaultRedisson), "The default redisson's name must be present");
        Assert.isTrue(properties.getRedissons().containsKey(defaultRedisson),
            "The default redisson instance configuration must be present");

        // init
        this.redissonClientMap = MultiRedissonsCreator.createRedissonClientMap(properties);
        this.redisConnectionFactoryMap = MultiRedissonsCreator.createRedisConnectionFactoryMap(redissonClientMap);
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        return new MultiRedissons(defaultRedisson, redissonClientMap);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new MutilRedissonsConnectionFactory(defaultRedisson, redisConnectionFactoryMap);
    }

}
