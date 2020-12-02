package sharedkube.redisson.core.spring.data.connection;

import java.util.Optional;

import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * manage multi-redissons routing rules
 * 
 * @author houyehua
 *
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MultiRedissonsRoutingManager implements AutoCloseable {

    private String routingKey;
    private static final ThreadLocal<MultiRedissonsRoutingManager> MANAGER_HOLDER = new ThreadLocal<>();

    public static MultiRedissonsRoutingManager getInstance() {
        Assert.isNull(MANAGER_HOLDER.get(),
            "MultiRedissonsManager still hold the previous value, please clear it first.");
        MultiRedissonsRoutingManager result = new MultiRedissonsRoutingManager();
        MANAGER_HOLDER.set(result);
        return result;
    }

    public void setRoutingKey(String key) {
        if (routingKey != null) {
            if (log.isWarnEnabled()) {
                log.warn("previous routing key has been set to '{}', update to '{}' now.", routingKey, key);
            }
        }
        this.routingKey = key;
    }

    public static String getRoutingKey() {
        return Optional.ofNullable(MANAGER_HOLDER.get()).map(m -> m.routingKey).orElse(null);
    }

    public static void clear() {
        MANAGER_HOLDER.remove();
    }

    @Override
    public void close() {
        MultiRedissonsRoutingManager.clear();
    }
}