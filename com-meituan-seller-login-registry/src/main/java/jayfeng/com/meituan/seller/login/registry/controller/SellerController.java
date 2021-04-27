package jayfeng.com.meituan.seller.login.registry.controller;

import jayfeng.com.meituan.seller.login.registry.bean.Seller;
import jayfeng.com.meituan.seller.login.registry.response.ResponseMessage;
import jayfeng.com.meituan.seller.login.registry.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 商家登录注册控制层
 * @author JayFeng
 * @date 2021/4/10
 */
@Slf4j
@RestController
@RequestMapping("/meituan/seller/login_registry")
public class SellerController extends BaseController {

    @Autowired
    private SellerService sellerService;

    /**
     * 获取验证码
     * @param phone 手机号
     * @return 返回验证码
     */
    @GetMapping("/getIdentifyCode/{phone}")
    public ResponseMessage getIdentifyCode(@PathVariable("phone") String phone) {
        log.info("getIdentifyCode 商家获取验证码 phone: {}", phone);
        return requestSuccess(sellerService.getIdentifyCode(phone));
    }

    /**
     * 商家通过手机验证码登录
     * @param phone 手机号
     * @param identifyCode 验证码
     * @return 返回登录是否成功
     */
    @PostMapping("loginByCode/{phone}/{identifyCode}")
    public ResponseMessage loginByCode(@PathVariable("phone") String phone,
                                       @PathVariable("identifyCode") String identifyCode) {
        log.info("loginByCode 商家通过验证码登录 phone: {}, identifyCode: {}", phone, identifyCode);
        return requestSuccess(sellerService.loginByCode(phone, identifyCode));
    }

    /**
     * 商家通过手机号密码登录
     * @param phone 手机号
     * @param password 密码
     * @return 返回登录是否成功
     */
    @PostMapping("/loginByPassword/{phone}")
    public ResponseMessage loginByPassword(@PathVariable("phone") String phone,
                                           @RequestParam("password") String password) {
        log.info("loginByPassword 商家通过密码登录 phone: {}, password: {}", phone, password);
        return requestSuccess(sellerService.loginByPassword(phone, password));
    }

    /**
     * 商家退出登录
     * @param request 请求拿 cookie
     * @param response 响应 删 cookie
     * @return 返回
     */
    @PostMapping("/logout")
    public ResponseMessage logout(HttpServletRequest request,
                                  HttpServletResponse response) {
        log.info("logout 商家退出登录");
        return requestSuccess(sellerService.logout(request, response));
    }

    /**
     * 商家注册
     * @param seller 商家信息
     * @param identifyCode 验证码
     * @return 返回
     */
    @PostMapping("/registry/{identifyCode}")
    public ResponseMessage registry(@RequestBody Seller seller, @PathVariable("identifyCode") String identifyCode) {
        log.info("registry 商家注册 seller: {}, identifyCode: {}", seller, identifyCode);
        return requestSuccess(sellerService.registry(seller, identifyCode));
    }

    /**
     * 修改密码
     * @param paramsMap 参数
     * phone -- 手机号
     * newPassword -- 新密码
     * identifyCode -- 验证码
     * @return 返回
     */
    @PutMapping("/changePassword")
    public ResponseMessage changePassword(@RequestBody Map<String, String> paramsMap) {
        log.info("changePassword 商家修改密码 paramsMap: {}", paramsMap);
        return requestSuccess(sellerService.changePassword(paramsMap));
    }

    /**
     * 检查手机号是否已注册
     * @param phone 手机号
     * @return 返回
     */
    @GetMapping("/checkPhoneExists/{phone}")
    public ResponseMessage checkPhoneExists(@PathVariable("phone") String phone) {
        log.info("checkPhoneExists 检查手机号是否已注册: phone: {}", phone);
        return requestSuccess(sellerService.checkPhoneExists(phone));
    }

    /**
     * 检查身份证是否已绑定
     * @param idCard 身份证
     * @return 返回
     */
    @GetMapping("/checkIdCardExists")
    public ResponseMessage checkIdCardExists(@RequestParam("idCard") String idCard) {
        log.info("checkIdCardExists 检查身份证是否已绑定 idCard: {}", idCard);
        return requestSuccess(sellerService.checkIdCardExists(idCard));
    }

    /**
     * 根据手机号修改账号是否有效
     * @param paramsMap 参数
     * phone 手机号
     * isValid 是否有效
     * identifyCode 验证码
     * @return 返回更新结果
     */
    @PutMapping("/setSellerIsValid")
    public ResponseMessage setSellerIsValid(@RequestBody Map<String, String> paramsMap) {
        log.info("setSellerIsValid 更新商家是否有效 paramsMap: {}", paramsMap);
        return requestSuccess(sellerService.setSellerIsValid(paramsMap));
    }

}
