package cn.meshed.cloud.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.util.AntPathMatcher;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public class AntPathMatcherConfig {

    @Bean
    public AntPathMatcher antPathMatcher(){
        return new AntPathMatcher();
    }
}
