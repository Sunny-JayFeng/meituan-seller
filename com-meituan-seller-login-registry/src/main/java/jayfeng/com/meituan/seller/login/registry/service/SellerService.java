package jayfeng.com.meituan.seller.login.registry.service;

import jayfeng.com.meituan.seller.login.registry.bean.Seller;
import jayfeng.com.meituan.seller.login.registry.response.ResponseData;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author JayFeng
 * @date 2021/4/10
 */
@Service
public interface SellerService {

    /**
     * 获取短信验证码
     * @param phone 手机号
     * @return 返回
     */
    ResponseData getIdentifyCode(String phone);

    /**
     * 商家通过手机号验证码登录
     * @param phone 手机号
     * @param identifyCode 验证码
     * @return 返回数据
     */
    ResponseData loginByCode(String phone, String identifyCode);

    /**
     * 商家通过手机号密码登录
     * @param phone 手机号
     * @param password 密码
     * @return 返回数据
     */
    ResponseData loginByPassword(String phone, String password);

    /**
     * 商家退出登录
     * @param request 获取 cookie
     * @param response 删 cookie
     * @return 返回
     */
    ResponseData logout(HttpServletRequest request, HttpServletResponse response);

    /**
     * 商家注册
     * @param seller 商家信息
     * @param identifyCode 验证码
     * @return 返回
     */
    ResponseData registry(Seller seller, String identifyCode);

    /**
     * 修改密码
     * @param paramsMap 参数
     * phone -- 手机号
     * newPassword -- 新密码
     * identifyCode -- 验证码
     * @return 返回
     */
    ResponseData changePassword(Map<String, String> paramsMap);

    /**
     * 检查手机号是否已注册
     * @param phone 手机号
     * @return 返回
     */
    ResponseData checkPhoneExists(String phone);

    /**
     * 检查身份证是否已绑定
     * @param idCard 身份证
     * @return 返回
     */
    ResponseData checkIdCardExists(String idCard);

    /**
     * 根据手机号修改账号是否有效
     * @param paramsMap 参数
     * phone 手机号
     * isValid 是否有效
     * identifyCode 验证码
     * @return 返回更新结果
     */
    ResponseData setSellerIsValid(Map<String, String> paramsMap);

}
