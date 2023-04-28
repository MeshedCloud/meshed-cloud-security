package cn.meshed.cloud.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.Set;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Data
@RefreshScope
@ConfigurationProperties("service.security")
public class SecurityConfig {

    private Set<String> excludeUris;

    private MockConfig mock;

    @Data
    public static class MockConfig {
        private Boolean enable;
        private String userId;
        private String username;
        private String access;
        private String roles;
    }
}
