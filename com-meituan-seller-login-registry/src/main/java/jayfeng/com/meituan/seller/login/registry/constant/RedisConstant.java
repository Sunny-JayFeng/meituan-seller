package jayfeng.com.meituan.seller.login.registry.constant;

/**
 * Redis 常量
 * @author JayFeng
 * @date 2020/4/10
 */
public enum RedisConstant {

    SELLER_UUID_MAP("sellerUUIDMap"),

    IDENTIFY_TIMEOUT(60 * 10L); // 验证码的过期时间

    private String message;

    private Long timeout;

    RedisConstant(String message) {
        this.message = message;
    }
    RedisConstant(Long timeout) {
        this.timeout = timeout;
    }

    public String getRedisMapKey() {
        return this.message;
    }

    public String getValue() {
        return this.message;
    }

    public Long getTimeout() {
        return this.timeout;
    }

}
