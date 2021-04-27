package jayfeng.com.meituan.seller.login.registry.dao;

import jayfeng.com.meituan.seller.login.registry.bean.Seller;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author JayFeng
 * @date 2021/4/10
 */
@Repository
public interface SellerDao {

    /**
     * 新增一个商家
     * @param seller 商家
     */
    @Insert("INSERT INTO `seller`(`name`, `age`, `phone`, `password`, `id_card`, `is_valid`, `create_time`, `update_time`) " +
            "VALUES(#{seller.name}, #{seller.age}, #{seller.phone}, #{seller.password}, #{seller.idCard}, #{seller.isValid}, #{seller.createTime}, #{seller.updateTime})")
    void insertSeller(@Param("seller") Seller seller);

    /**
     * 根据 id 修改密码
     * @param sellerId id
     * @param password 密码
     * @param updateTime 更新时间
     */
    @Update("UPDATE FROM `seller` SET `password` = #{password}, `update_time` = #{updateTime} WHERE `id` = #{id} ")
    void updatePasswordById(@Param("sellerId") Integer sellerId, @Param("password") String password, @Param("updateTime") Long updateTime);

    /**
     * 根据手机号修改账号是否有效
     * @param phone 手机号
     * @param isValid 是否有效
     * @param updateTime 更新时间
     */
    @Update("UPDATE FROM `seller` SET `is_valid` = #{isValid}, `update_time` = #{updateTime} WHERE `phone` = #{phone} ")
    void updateSellerIsValid(@Param("phone") String phone, @Param("isValid") Integer isValid, @Param("updateTime") Long updateTime);

    /**
     * 根据手机号查询商家信息
     * @param phone 手机号
     * @return 返回商家对象
     */
    @Select("SELECT `id`, `name`, `age`, `phone`, `password`, `is_valid` FROM `seller` WHERE `phone` = #{phone}")
    Seller selectSellerByPhone(@Param("phone") String phone);

    /**
     * 根据身份证号查询商家信息
     * @param idCard 身份证
     * @return 返回商家对象
     */
    @Select("SELECT `id`, `name`, `age`, `phone`, `password`, `is_valid` FROM `seller` WHERE `id_card` = #{idCard}")
    Seller selectSellerByIdCard(@Param("idCard") String idCard);


}
