package jayfeng.com.meituan.seller.login.registry.service.impl;

import jayfeng.com.meituan.seller.login.registry.bean.Seller;
import jayfeng.com.meituan.seller.login.registry.constant.CookieConstant;
import jayfeng.com.meituan.seller.login.registry.constant.RedisConstant;
import jayfeng.com.meituan.seller.login.registry.dao.SellerDao;
import jayfeng.com.meituan.seller.login.registry.exception.RequestForbiddenException;
import jayfeng.com.meituan.seller.login.registry.response.ResponseData;
import jayfeng.com.meituan.seller.login.registry.service.SellerService;
import jayfeng.com.meituan.seller.login.registry.utiil.CookieManagement;
import jayfeng.com.meituan.seller.login.registry.utiil.EncryptUtil;
import jayfeng.com.meituan.seller.login.registry.utiil.IdentifyCodeManagement;
import jayfeng.com.meituan.seller.login.registry.utiil.PatternMatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.util.Map;

/**
 * 商家登录注册逻辑
 * @author JayFeng
 * @date 2021/4/10
 */
@Slf4j
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerDao sellerDao;
    @Autowired
    private PatternMatch patternMatch;
    @Autowired
    private IdentifyCodeManagement identifyCodeManagement;
    @Autowired
    private CookieManagement cookieManagement;
    @Autowired
    private EncryptUtil encryptUtil;

    /**
     * 获取短信验证码
     * @param phone 手机号
     * @return 返回
     */
    @Override
    public ResponseData getIdentifyCode(String phone) {
        if (ObjectUtils.isEmpty(phone) || !patternMatch.isPhone(phone)) throw new RequestForbiddenException("您无权访问该服务");
        String identifyCode = identifyCodeManagement.getIdentifyCode(phone);
        log.info("getIdentifyCode 获取到验证码 phone: {}, identifyCode: {}", phone, identifyCode);
        return ResponseData.createSuccessResponseData("getIdentifyCodeInfo", identifyCode);
    }

    /**
     * 商家通过手机号验证码登录
     * @param phone 手机号
     * @param identifyCode 验证码
     * @return 返回数据
     */
    @Override
    public ResponseData loginByCode(String phone, String identifyCode) {
        if (ObjectUtils.isEmpty(phone) || ObjectUtils.isEmpty(identifyCode) ||
            !patternMatch.isPhone(phone)) throw new RequestForbiddenException("您无权访问该服务");
        Seller seller = sellerDao.selectSellerByPhone(phone);
        if (seller == null) {
            log.info("loginByCode 验证码登录失败, 商家不存在");
            return ResponseData.createFailResponseData("loginByCodeInfo", null, "商家未注册", "never_registry");
        }
        String realIdentifyCode = identifyCodeManagement.getIdentifyCode(phone);
        if (!identifyCode.equals(realIdentifyCode)) {
            log.info("loginByCode 验证码登录失败, 验证码不正确 identifyCode: {}, realIdentifyCode: {}", identifyCode, realIdentifyCode);
            return ResponseData.createFailResponseData("loginByCodeInfo", null, "验证码错误", "identify_code_error");
        }
        log.info("loginByCode 验证码登录成功 seller: {}", seller);
        seller.setPassword(null);
        return ResponseData.createSuccessResponseData("loginByCodeInfo", seller);
    }

    /**
     * 商家通过手机号密码登录
     * @param phone 手机号
     * @param password 密码
     * @return 返回数据
     */
    @Override
    public ResponseData loginByPassword(String phone, String password) {
        if (ObjectUtils.isEmpty(phone) || ObjectUtils.isEmpty(password) ||
            !patternMatch.isPhone(phone)) throw new RequestForbiddenException("您无权访问该服务");
        Seller seller = sellerDao.selectSellerByPhone(phone);
        if (seller == null) {
            log.info("loginByPassword 密码登录失败, 商家不存在");
            return ResponseData.createFailResponseData("loginByPasswordInfo", null, "商家未注册", "seller_never_registry");
        }
        if (!encryptUtil.matches(password, seller.getPassword())) {
            log.info("loginByPassword 密码错误, password: {}", password);
            return ResponseData.createFailResponseData("loginByPasswordInfo", null, "密码错误", "password_error");
        }
        log.info("loginByPassword 密码登录成功 seller: {}", seller);
        seller.setPassword(null);
        return ResponseData.createSuccessResponseData("loginByPasswordInfo", seller);
    }

    /**
     * 商家退出登录
     * @param request 获取 cookie
     * @param response 删 cookie
     * @return 返回
     */
    @Override
    public ResponseData logout(HttpServletRequest request, HttpServletResponse response) {
        Object sellerObj = cookieManagement.getLoginSeller(request, CookieConstant.SELLER_KEY.getCookieKey(), RedisConstant.SELLER_UUID_MAP.getRedisMapKey());
        if (sellerObj == null) throw new RequestForbiddenException("您无权访问该服务");
        log.info("logout 商家退出登录 sellerObj: {}", sellerObj);
        cookieManagement.removeCookie(response, request, CookieConstant.SELLER_KEY.getCookieKey());
        return ResponseData.createSuccessResponseData("logoutInfo", true);
    }

    /**
     * 商家注册
     * @param seller 商家信息
     * @param identifyCode 验证码
     * @return 返回
     */
    @Override
    public ResponseData registry(Seller seller, String identifyCode) {
        String phone = seller.getPhone();
        if (ObjectUtils.isEmpty(phone) || ObjectUtils.isEmpty(seller.getPassword()) ||
            ObjectUtils.isEmpty(seller.getIdCard()) || ObjectUtils.isEmpty(seller.getName()) ||
            ObjectUtils.isEmpty(seller.getAge()) || ObjectUtils.isEmpty(identifyCode) ||
            !patternMatch.isPhone(phone) || patternMatch.checkPassword(seller.getPassword()) ||
            sellerDao.selectSellerByPhone(phone) != null || sellerDao.selectSellerByIdCard(seller.getIdCard()) != null) {
            // 请求非法, 拒绝处理
            throw new RequestForbiddenException("您无权访问该服务");
        }
        String realIdentifyCode = identifyCodeManagement.getIdentifyCode(phone);
        if (identifyCode.equals(realIdentifyCode)) {
            seller.setIsValid(1);
            seller.setCreateTime(System.currentTimeMillis());
            seller.setUpdateTime(seller.getCreateTime());
            sellerDao.insertSeller(seller);
            log.info("registry 商家注册成功, seller: {}", seller);
            identifyCodeManagement.removeIdentifyCode(phone);
            return ResponseData.createSuccessResponseData("registryInfo", true);
        } else {
            log.info("registry 商家注册失败, 验证码错误 identifyCode: {}, realIdentifyCode: {}", identifyCode, realIdentifyCode);
            return ResponseData.createFailResponseData("registryInfo", false, "验证码错误", "identify_code_error");
        }
    }

    /**
     * 商家修改密码
     * @param paramsMap 参数
     * phone -- 手机号
     * newPassword -- 新密码
     * identifyCode -- 验证码
     * @return
     */
    @Override
    public ResponseData changePassword(Map<String, String> paramsMap) {
        String phone = paramsMap.get("phone");
        String newPassword = paramsMap.get("newPassword");
        String identifyCode = paramsMap.get("identifyCode");

        if (ObjectUtils.isEmpty(phone) || ObjectUtils.isEmpty(newPassword) || ObjectUtils.isEmpty(identifyCode) ||
            !patternMatch.isPhone(phone) || !patternMatch.checkPassword(newPassword)) {
            throw new RequestForbiddenException("您无权访问该服务");
        }

        Seller seller = sellerDao.selectSellerByPhone(phone);
        if (seller == null) {
            log.info("changePassword 商家密码修改失败, 商家未注册");
            return ResponseData.createFailResponseData("changePasswordInfo", false, "商家未注册", "seller_is_not_exists");
        }
        String realIdentifyCode = identifyCodeManagement.getIdentifyCode(phone);
        if (identifyCode.equals(realIdentifyCode)) {
            log.info("changePassword 商家修改密码 oldSeller: {}", seller);
            sellerDao.updatePasswordById(seller.getId(), newPassword, System.currentTimeMillis());
            identifyCodeManagement.removeIdentifyCode(phone);
            log.info("changePassword 商家修改密码成功");
            return ResponseData.createSuccessResponseData("changePasswordInfo", true);
        } else {
            log.info("changePassword 商家修改密码失败, 验证码错误 identifyCode: {}, realIdentifyCode: {}", identifyCode, realIdentifyCode);
            return ResponseData.createFailResponseData("changePasswordInfo", false, "验证码错误", "identify_code_error");
        }
    }

    /**
     * 检查手机号是否已注册
     * @param phone 手机号
     * @return 返回
     */
    @Override
    public ResponseData checkPhoneExists(String phone) {
        if (ObjectUtils.isEmpty(phone) || !patternMatch.isPhone(phone)) throw new RequestForbiddenException("您无权访问该服务");
        Seller seller = sellerDao.selectSellerByPhone(phone);
        if (seller == null) return ResponseData.createSuccessResponseData("checkPhoneExistsInfo", false);
        log.info("checkPhoneExists 手机号已注册 seller: {}", seller);
        return ResponseData.createFailResponseData("checkPhoneExistsInfo", true, "手机号已", "phone_already_registry");
    }

    /**
     * 检查身份证是否已绑定
     * @param idCard 身份证
     * @return 返回
     */
    @Override
    public ResponseData checkIdCardExists(String idCard) {
        if (ObjectUtils.isEmpty(idCard)) throw new RequestForbiddenException("您无权访问该服务");
        Seller seller = sellerDao.selectSellerByIdCard(idCard);
        if (seller == null) return ResponseData.createSuccessResponseData("checkIdCardExistsInfo", false);
        log.info("checkIdCardExists 身份证已被绑定 idCard: {}", idCard);
        return ResponseData.createFailResponseData("checkIdCardExistsInfo", true, "身份证已被绑定", "id_card_already_bind");
    }

    /**
     * 根据手机号修改账号是否有效
     * @param paramsMap 参数
     * phone 手机号
     * isValid 是否有效
     * identifyCode 验证码
     * @return 返回更新结果
     */
    @Override
    public ResponseData setSellerIsValid(Map<String, String> paramsMap) {
        String phone = paramsMap.get("phone");
        String isValid = paramsMap.get("isValid");
        String identifyCode = paramsMap.get("identifyCode");
        if (ObjectUtils.isEmpty(phone) || ObjectUtils.isEmpty(isValid) || ObjectUtils.isEmpty(identifyCode) ||
            !patternMatch.isPhone(phone) || sellerDao.selectSellerByPhone(phone) == null) {
            throw new RequestForbiddenException("您无权访问该服务");
        }
        String realIdentifyCode = identifyCodeManagement.getIdentifyCode(phone);
        if (identifyCode.equals(realIdentifyCode)) {
            try {
                sellerDao.updateSellerIsValid(phone, Integer.parseInt(isValid), System.currentTimeMillis());
                identifyCodeManagement.removeIdentifyCode(phone);
                log.info("setSellerIsValid 更新商家是否有效成功 isValid: {}", isValid);
                return ResponseData.createSuccessResponseData("setSellerIsValidInfo", true);
            } catch (NumberFormatException e) {
                log.info("setSellerIsValid 更新商家是否有效失败, 参数不正确 isValid: {}", isValid);
                throw new RequestForbiddenException("您无权访问该服务");
            }
        } else {
            log.info("setSellerIsValid 更新商家是否有效失败, 验证码不正确 identifyCode: {}, realIdentifyCode: {}", identifyCode, realIdentifyCode);
            return ResponseData.createFailResponseData("setSellerIsValidInfo", false, "验证码错误", "identify_code_error");
        }
    }


}
