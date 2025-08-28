package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.annotation.Anonymous;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统控制器
 * 
 * @author omniperform
 */
@RestController
public class SystemController {

    /**
     * 健康检查接口
     */
    @Anonymous
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "ok");
        data.put("timestamp", new Date());
        data.put("version", "1.0.0");
        data.put("service", "omniperform-web");
        data.put("message", "服务运行正常");
        
        return Result.success(data);
    }
}
