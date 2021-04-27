package jayfeng.com.meituan.seller.login.registry.bean;

import lombok.Data;

/**
 * 商家
 * @author JayFeng
 * @date 2021/4/10
 */
@Data
public class Seller {

    /**
     * 主键 id
     */
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 手机号 -- 账号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 是否有效
     */
    private Integer isValid;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

}
