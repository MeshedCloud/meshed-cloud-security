package cn.meshed.cloud.security;

import cn.meshed.cloud.dto.Operator;
import cn.meshed.cloud.security.AccessTokenService;
import cn.meshed.cloud.security.config.SecurityConfig;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class DevelopMockService {

    private final SecurityConfig securityConfig;
    private final AccessTokenService accessTokenService;
    @Value("${spring.profiles.active:prod}")
    private String env;

    public Operator mockOperator() {
        SecurityConfig.MockConfig mockConfig = getMockConfig();
        Operator operator = new Operator(mockConfig.getUserId(), mockConfig.getUsername());
        if (StringUtils.isNotBlank(mockConfig.getAccess())){
            String[] split = mockConfig.getAccess().split(",");
            operator.setAccess(Arrays.stream(split).collect(Collectors.toSet()));
        }
        if (StringUtils.isNotBlank(mockConfig.getRoles())){
            String[] split = mockConfig.getRoles().split(",");
            operator.setRoles(Arrays.stream(split).collect(Collectors.toSet()));
        }
        log.warn("采用模拟用户: {} | {} ", operator, JSONObject.toJSONString(operator));
        return operator;
    }

    public SecurityConfig.MockConfig getMockConfig() {
        SecurityConfig.MockConfig mockConfig = securityConfig.getMock();
        return mockConfig;
    }

    public String mockSign() {
        SecurityConfig.MockConfig mockConfig = getMockConfig();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",mockConfig.getUserId());
        jsonObject.put("realName",mockConfig.getUsername());
        if (StringUtils.isNotBlank(mockConfig.getAccess())){
            String[] split = mockConfig.getAccess().split(",");
            jsonObject.put("grantedAuthority",Arrays.stream(split).collect(Collectors.toSet()));
        }
        if (StringUtils.isNotBlank(mockConfig.getRoles())){
            String[] split = mockConfig.getRoles().split(",");
            jsonObject.put("grantedRole",Arrays.stream(split).collect(Collectors.toSet()));
        }
        return accessTokenService.generateToken(jsonObject.toJSONString());
    }

    public boolean isMock() {
        if (securityConfig.getMock() == null || securityConfig.getMock().getEnable() == null){
            return false;
        }
        return !"prod".equals(env) && securityConfig.getMock().getEnable();
    }
}
