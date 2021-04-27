package jayfeng.com.meituan.seller.login.registry.redis;

import jayfeng.com.meituan.seller.login.registry.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * redis 操作
 * @author JayFeng
 * @date 2020/4/10
 */
@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisOperate redisOperate;

    /**
     * 存入 uuid
     * @param redisKey 哪一个 map
     * @param sessionId 存入的 key
     */
    public void addSellerUUID(String redisKey, String sessionId, String objectStr) {
        log.info("addSellerUUID 向redis缓存中添加一个uuid, redisKey: {}, UUID: {}", redisKey, sessionId);
        redisOperate.setForHash(redisKey, sessionId, objectStr);
    }

    /**
     * 获取某个 map 中的某个键值对的值
     * @param redisKey map key
     * @param sessionId 键
     * @return 值
     */
    public Object getSellerJSON(String redisKey, String sessionId) {
        Object sellerObj = redisOperate.getValueForHash(redisKey, sessionId);
        log.info("getSellerJSON 从 redis 缓存中获取商家 sellerObj: {}", sellerObj);
        return sellerObj;
    }

    /**
     * 退出登录
     * 删除 uuid
     * @param redisKey 哪一个 map
     * @param sessionId 删除的 key
     */
    public void deleteSellerUUID(String redisKey, String sessionId) {
        log.info("deleteSellerUUID 从redis缓存中删除一个uuid, redisKey: {}, UUID: {}", redisKey, sessionId);
        redisOperate.removeForHash(redisKey, sessionId);
    }

    /**
     * 夜间删除 redis 缓存中所有的管理员 UUID
     */
    public void deleteAllSellerUUID() {
        log.info("deleteAllSellerUUID 删除 redis 缓存中所有的商家 UUID");
        redisOperate.remove(RedisConstant.SELLER_UUID_MAP.getRedisMapKey());
    }

    /**
     * 获取短信验证码
     * @param phone key-手机号
     * @return 返回验证码的值
     */
    public String getIdentifyCode(String phone) {
        log.info("getIdentifyCode 从redis缓存中获取短信验证码, phone: {}", phone);
        return redisOperate.get(phone);
    }

    /**
     * 向 redis 缓存中添加一个验证码，有效时间为 10 分钟
     * @param phone key-手机号
     * @param identifyCode 验证码
     */
    public void addIdentifyCode(String phone, String identifyCode) {
        log.info("addIdentifyCode 向redis缓存中添加一个验证码, phone: {}, identifyCode: {}", phone, identifyCode);
        redisOperate.set(phone, identifyCode, RedisConstant.IDENTIFY_TIMEOUT.getTimeout(), TimeUnit.SECONDS);
    }

    /**
     * 从 redis 缓存中移除一个验证码
     * @param phone key-手机号
     */
    public Boolean removeIdentifyCode(String phone) {
        log.info("removeIdentifyCode, 从redis缓存中移除一个验证码, phone: {}", phone);
        return redisOperate.remove(phone);
    }

}
