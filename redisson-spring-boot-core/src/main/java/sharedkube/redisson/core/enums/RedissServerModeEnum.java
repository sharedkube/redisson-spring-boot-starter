package sharedkube.redisson.core.enums;

/**
 * @author huahouye@gmail.com
 * @date 2020/08/15
 */
public enum RedissServerModeEnum {

    /**
     * single mode
     */
    SINGLE,

    /**
     * cluster mode
     */
    CLUSTER,

    /**
     * master_slave mode
     */
    MASTER_SLAVE,

    /**
     * sentinel mode
     */
    SENTINEL,

    /**
     * cloud mode
     */
    REPLICATED,

    /**
     * disable
     */
    NONE

}