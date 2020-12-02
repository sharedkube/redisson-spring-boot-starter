package sharedkube.redisson.core.properties;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Multi-Redissons configuration properties
 * 
 * @author huahouye@gmail.com
 * @date 2020/08/23
 */
@Data
@ConfigurationProperties(prefix = "sharedkube.multi-redissons")
public class MultiRedissonsProperties {

    private boolean enabled = true;

    /**
     * the name of default redisson instance.
     */
    private String defaultRedisson;

    /**
     * multi redisson instance configuration.
     */
    private Map<String, RedissonProperties> redissons = new LinkedHashMap<>();

}
