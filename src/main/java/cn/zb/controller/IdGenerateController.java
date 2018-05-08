package cn.zb.controller;

import cn.zb.utils.IdGenerate;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zb Created in 11:21 PM 2018/5/8
 */
@RestController
@AllArgsConstructor
@RequestMapping("id")
public class IdGenerateController {

    private IdGenerate idGenerate;

    @GetMapping("get")
    public Long idGenerate() {
        Number id = idGenerate.generateId();
        return id.longValue();
    }
}
