package jayfeng.com.meituan.seller.login.registry.exception;

/**
 * 拒绝处理请求，抛出这个异常
 * @author JayFeng
 * @date 2021/4/10
 */
public class RequestForbiddenException extends RuntimeException {

    public RequestForbiddenException(String message) {
        super(message);
    }

}
