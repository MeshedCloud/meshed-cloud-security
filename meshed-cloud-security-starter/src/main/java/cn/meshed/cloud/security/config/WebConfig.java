package cn.meshed.cloud.security.config;

import cn.meshed.cloud.security.OperatorInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <h1>拦截器配置</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public class WebConfig implements WebMvcConfigurer {

    private final OperatorInterceptor operatorInterceptor;

    public WebConfig(OperatorInterceptor operatorInterceptor) {
        this.operatorInterceptor = operatorInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(operatorInterceptor);
    }
}
