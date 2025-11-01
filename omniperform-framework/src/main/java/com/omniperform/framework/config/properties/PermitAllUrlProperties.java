package com.omniperform.framework.config.properties;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.omniperform.common.annotation.Anonymous;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 设置Anonymous注解允许匿名访问的url
 * 
 * @author omniperform
 */
@Configuration
public class PermitAllUrlProperties implements InitializingBean, ApplicationContextAware
{
    private static final Logger log = LoggerFactory.getLogger(PermitAllUrlProperties.class);
    private List<String> urls = new ArrayList<>();

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception
    {
        log.info("[PermitAllUrlProperties] 开始扫描@Anonymous注解...");
        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(Controller.class);
        Map<String, Object> restControllers = applicationContext.getBeansWithAnnotation(RestController.class);
        controllers.putAll(restControllers);
        log.info("[PermitAllUrlProperties] 找到控制器数量: " + controllers.size());
        for (Object bean : controllers.values())
        {
            Class<?> beanClass;
            if (bean instanceof Advised)
            {
                beanClass = ((Advised) bean).getTargetSource().getTarget().getClass();
            }
            else
            {
                beanClass = bean.getClass();
            }
            // 处理类级别的匿名访问注解
            if (beanClass.isAnnotationPresent(Anonymous.class))
            {
                RequestMapping baseMapping = beanClass.getAnnotation(RequestMapping.class);
                if (Objects.nonNull(baseMapping))
                {
                    String[] baseUrl = baseMapping.value();
                    for (String url : baseUrl)
                    {
                        urls.add(prefix(url) + "/**");
                    }
                    continue;
                }
            }

            // 处理方法级别的匿名访问注解
            Method[] methods = beanClass.getDeclaredMethods();
            log.info("[PermitAllUrlProperties] 检查控制器: " + beanClass.getSimpleName() + ", 方法数量: " + methods.length);
            
            // 特别关注AuthController
            if (beanClass.getSimpleName().equals("AuthController")) {
                log.info("[PermitAllUrlProperties] *** 正在扫描AuthController ***");
                RequestMapping baseMapping = beanClass.getAnnotation(RequestMapping.class);
                if (baseMapping != null) {
                    log.info("[PermitAllUrlProperties] AuthController基础路径: " + String.join(",", baseMapping.value()));
                }
            }
            
            for (Method method : methods)
            {
                // 特别关注AuthController的getUserPermissions方法
                if (beanClass.getSimpleName().equals("AuthController") && method.getName().equals("getUserPermissions")) {
                    log.info("[PermitAllUrlProperties] *** 检查getUserPermissions方法 ***");
                    log.info("[PermitAllUrlProperties] 方法名: " + method.getName());
                    log.info("[PermitAllUrlProperties] 是否有@Anonymous注解: " + method.isAnnotationPresent(Anonymous.class));
                    log.info("[PermitAllUrlProperties] 是否有@GetMapping注解: " + method.isAnnotationPresent(GetMapping.class));
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        GetMapping getMapping = method.getAnnotation(GetMapping.class);
                        log.info("[PermitAllUrlProperties] @GetMapping值: " + String.join(",", getMapping.value()));
                    }
                }
                
                if (method.isAnnotationPresent(Anonymous.class))
                {
                    log.info("[PermitAllUrlProperties] 找到@Anonymous方法: " + beanClass.getSimpleName() + "." + method.getName());
                    RequestMapping baseMapping = beanClass.getAnnotation(RequestMapping.class);
                    String[] baseUrl = {};
                    if (Objects.nonNull(baseMapping))
                    {
                        baseUrl = baseMapping.value();
                        log.info("[PermitAllUrlProperties] 基础URL: " + String.join(",", baseUrl));
                    }
                    if (method.isAnnotationPresent(RequestMapping.class))
                    {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        String[] uri = requestMapping.value();
                        List<String> builtUrls = rebuildUrl(baseUrl, uri);
                        urls.addAll(builtUrls);
                        log.info("[PermitAllUrlProperties] 添加RequestMapping URL: " + builtUrls);
                    }
                    else if (method.isAnnotationPresent(GetMapping.class))
                    {
                        GetMapping requestMapping = method.getAnnotation(GetMapping.class);
                        String[] uri = requestMapping.value();
                        List<String> builtUrls = rebuildUrl(baseUrl, uri);
                        urls.addAll(builtUrls);
                        log.info("[PermitAllUrlProperties] 添加GetMapping URL: " + builtUrls);
                    }
                    else if (method.isAnnotationPresent(PostMapping.class))
                    {
                        PostMapping requestMapping = method.getAnnotation(PostMapping.class);
                        String[] uri = requestMapping.value();
                        urls.addAll(rebuildUrl(baseUrl, uri));
                    }
                    else if (method.isAnnotationPresent(PutMapping.class))
                    {
                        PutMapping requestMapping = method.getAnnotation(PutMapping.class);
                        String[] uri = requestMapping.value();
                        urls.addAll(rebuildUrl(baseUrl, uri));
                    }
                    else if (method.isAnnotationPresent(DeleteMapping.class))
                    {
                        DeleteMapping requestMapping = method.getAnnotation(DeleteMapping.class);
                        String[] uri = requestMapping.value();
                        urls.addAll(rebuildUrl(baseUrl, uri));
                    }
                }
            }
        }
        log.info("[PermitAllUrlProperties] 扫描完成，匿名访问URL列表: " + urls);
    }

    private List<String> rebuildUrl(String[] bases, String[] uris)
    {
        List<String> urls = new ArrayList<>();
        for (String base : bases)
        {
            if (uris.length > 0)
            {
                for (String uri : uris)
                {
                    urls.add(prefix(base) + prefix(uri));
                }
            }
            else
            {
                urls.add(prefix(base));
            }
        }
        return urls;
    }

    private String prefix(String seg)
    {
        return seg.startsWith("/") ? seg : "/" + seg;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException
    {
        this.applicationContext = context;
    }

    public List<String> getUrls()
    {
        return urls;
    }

    public void setUrls(List<String> urls)
    {
        this.urls = urls;
    }
}
