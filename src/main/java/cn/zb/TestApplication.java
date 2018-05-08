package cn.zb;

import cn.zb.utils.IdGenerate;
import cn.zb.utils.SnowflakeIdGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * create by zb on 2018-05-04
 * 这是我的第一个项目
 * @author zb
 */
@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Bean
    public IdGenerate IdGenerate() {
        return new SnowflakeIdGenerator();
    }

}
