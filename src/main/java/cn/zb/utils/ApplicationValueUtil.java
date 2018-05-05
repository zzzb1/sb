package cn.zb.utils;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zb Created in 1:59 PM 05/05/2018
 */
@ConfigurationProperties(prefix = "zb")
@Component
@lombok.Getter
@lombok.Setter
public class ApplicationValueUtil {

    private String name;
}
