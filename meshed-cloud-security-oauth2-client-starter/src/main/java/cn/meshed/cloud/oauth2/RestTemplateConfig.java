package cn.meshed.cloud.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
public class RestTemplateConfig {


    /**
     * 没有实例化RestTemplate时，初始化RestTemplate
     *
     * @return
     */
    @ConditionalOnMissingBean(RestTemplate.class)
    @Primary
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
