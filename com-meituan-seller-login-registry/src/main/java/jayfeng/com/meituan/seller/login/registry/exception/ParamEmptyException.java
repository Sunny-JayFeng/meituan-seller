package jayfeng.com.meituan.seller.login.registry.exception;

/**
 * 参数为空异常
 * @author JayFeng
 * @date 2021/4/27
 */
public class ParamEmptyException extends RuntimeException {

    public ParamEmptyException(String message) {
        super(message);
    }

}
