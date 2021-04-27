package jayfeng.com.meituan.seller.login.registry;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"jayfeng.com.meituan.seller.login.registry.dao"})
public class ComMeituanSellerLoginRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComMeituanSellerLoginRegistryApplication.class, args);
    }

}
