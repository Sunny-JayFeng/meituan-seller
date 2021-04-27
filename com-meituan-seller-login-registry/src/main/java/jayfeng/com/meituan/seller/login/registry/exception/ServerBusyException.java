package jayfeng.com.meituan.seller.login.registry.exception;

/**
 * 服务端超负荷或停机维护
 * @author JayFeng
 * @date 2021/4/10
 */
public class ServerBusyException extends RuntimeException {

    public ServerBusyException(String message) {
        super(message);
    }

}
