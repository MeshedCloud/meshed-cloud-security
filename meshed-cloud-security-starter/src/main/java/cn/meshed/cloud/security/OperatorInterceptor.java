package cn.meshed.cloud.security;

import cn.meshed.cloud.context.SecurityContext;
import cn.meshed.cloud.dto.Operator;
import cn.meshed.cloud.exception.security.SysSecurityException;
import cn.meshed.cloud.security.config.SecurityConfig;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Set;


/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class OperatorInterceptor implements HandlerInterceptor {

    private final SecurityConfig securityConfig;
    private final AccessTokenService accessTokenService;
    private final AntPathMatcher antPathMatcher;
    private final DevelopMockService developMockService;
    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sign = request.getHeader("sign");
        /**
         * 模拟数据优先处理
         * 存在签名校验
         * 非排除接口禁止访问抛出
         */
        if (developMockService.isMock()) {
            //模拟用户，仅在测试使用
            SecurityContext.setOperator(developMockService.mockOperator());
            SecurityContext.setSign(developMockService.mockSign());
        } else if (StringUtils.isNotBlank(sign)) {
            //校验签名
            String data = accessTokenService.verifyToken(sign);
            //解析数据
            SecurityContext.setOperator(parsingOperator(data));
            //设置内部服务签名
            SecurityContext.setSign(sign);
        } else if (!isExcludeUris(request)) {
            //不是放行接口抛出异常
            throw new SysSecurityException("服务禁止访问");
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SecurityContext.clear();
    }

    private boolean isExcludeUris(HttpServletRequest request) {
        if (securityConfig == null) {
            return false;
        }
        Set<String> excludeUris = securityConfig.getExcludeUris();
        if (CollectionUtils.isEmpty(excludeUris)) {
            return false;
        }
        String requestUri = request.getRequestURI();
        //去除上下文
        if (StringUtils.isNotBlank(contextPath) && requestUri.startsWith(contextPath)) {
            requestUri = requestUri.substring(contextPath.length());
        }

        String finalRequestUri = requestUri;
        return excludeUris.stream().anyMatch(excludeUri -> antPathMatcher.match(excludeUri, finalRequestUri));
    }

    public Operator parsingOperator(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        //目前仅存在有用户信息和无用户信息两种签名
        if (jsonObject.size() == 0) {
            return null;
        }
        Operator operator = new Operator(jsonObject.getString("id"), jsonObject.getString("realName"));
        Set<String> grantedAuthority = getSet(jsonObject, "grantedAuthority");
        Set<String> grantedRole = getSet(jsonObject, "grantedRole");

        operator.setAccess(grantedAuthority);
        operator.setRoles(grantedRole);
        log.debug("操作用户: {}| {} ", operator, JSONObject.toJSONString(operator));
        return operator;
    }

    public Set<String> getSet(JSONObject jsonObject, String key) {
        String str = jsonObject.getString(key);
        if (StringUtils.isNotBlank(str)) {
            return JSONObject.parseObject(str, Set.class);
        }
        return Collections.emptySet();
    }
}
