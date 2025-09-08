package com.omniperform.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 控制器配置类
 * 确保所有控制器都能被正确扫描和注册
 * 
 * @author omniperform
 */
@Configuration
@ComponentScan(basePackages = {
    "com.omniperform.web.controller"
})
public class ControllerConfig {
    // 配置类，用于确保控制器组件扫描
}