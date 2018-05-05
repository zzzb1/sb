package cn.zb.controller;

import cn.zb.utils.ApplicationValueUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zb Created in 10:54 PM 04/05/2018
 */
@RestController
@RequestMapping("hello")
public class HelloWorldController {

    private final ApplicationValueUtil applicationValueUtil;

    @Value("${zb.name}")
    private String name;

    public HelloWorldController(ApplicationValueUtil applicationValueUtil) {
        this.applicationValueUtil = applicationValueUtil;
    }

    @GetMapping("world")
    public String world(){
//        return String.format("%s say %s", name, "hello world!");
        return String.format("%s say %s", applicationValueUtil.getName(), "hello world!");
    }
}
