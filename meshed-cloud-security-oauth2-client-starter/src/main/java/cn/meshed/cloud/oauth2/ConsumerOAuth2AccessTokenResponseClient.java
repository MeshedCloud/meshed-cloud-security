package cn.meshed.cloud.oauth2;

import com.alibaba.cola.exception.SysException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
public class ConsumerOAuth2AccessTokenResponseClient
        implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private static final String INVALID_TOKEN_RESPONSE_ERROR_CODE = "invalid_token_response";
    private Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> requestEntityConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();

    private final RestTemplate restTemplate;

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        Assert.notNull(authorizationCodeGrantRequest, "authorizationCodeGrantRequest cannot be null");
        RequestEntity<?> request = this.requestEntityConverter.convert(authorizationCodeGrantRequest);
        ResponseEntity<OAuth2AccessTokenResponse> response = getResponse(request);
        OAuth2AccessTokenResponse tokenResponse = response.getBody();
        if (CollectionUtils.isEmpty(tokenResponse.getAccessToken().getScopes())) {
            // As per spec, in Section 5.1 Successful Access Token Response
            // https://tools.ietf.org/html/rfc6749#section-5.1
            // If AccessTokenResponse.scope is empty, then default to the scope
            // originally requested by the client in the Token Request
            // @formatter:off
            tokenResponse = OAuth2AccessTokenResponse.withResponse(tokenResponse)
                    .scopes(authorizationCodeGrantRequest.getClientRegistration().getScopes())
                    .build();
            // @formatter:on
        }
        return tokenResponse;
    }

    private ResponseEntity<OAuth2AccessTokenResponse> getResponse(RequestEntity<?> request) {
        try {
            ResponseEntity<Map> exchange = restTemplate.exchange(request, Map.class);
            Map<String, Object> response = exchange.getBody();
            System.out.println(response);
            if (exchange.getStatusCode() != HttpStatus.OK) {
                throw new SysException(exchange.getStatusCode().name());
            }
            Integer code = (Integer) response.get("code");
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            if (code == null || code != 200 || data == null) {
                throw new SysException("AccessToken 获取失败：" + response.get("msg"));
            }

            OAuth2AccessTokenResponse tokenResponse = buildAuth2AccessTokenResponse(data);
            return ResponseEntity.of(Optional.of(tokenResponse));
        } catch (RestClientException ex) {
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_TOKEN_RESPONSE_ERROR_CODE,
                    "An error occurred while attempting to retrieve the OAuth 2.0 Access Token Response: "
                            + ex.getMessage(),
                    null);
            throw new OAuth2AuthorizationException(oauth2Error, ex);
        }
    }

    private OAuth2AccessTokenResponse buildAuth2AccessTokenResponse(Map<String, Object> data) {
        OAuth2AccessTokenResponse tokenResponse = OAuth2AccessTokenResponse.withToken(getString("access_token", data))
                .refreshToken(getString("refresh_token", data))
                .scopes(getScope(data))
                .expiresIn(getLong("expires_in", data))
                .tokenType(BEARER)
                .build();
        return tokenResponse;
    }

    private Set<String> getScope(Map<String, Object> data) {
        String scope = (String) data.get("scope");
        if (scope == null || scope.trim().length() == 0) {
            return Collections.emptySet();
        }
        String[] split = scope.split(",");
        return new HashSet<>(Arrays.asList(split));
    }

    private Long getLong(String key, Map<String, Object> data) {
        Object o = data.get(key);
        if (o == null) {
            return null;
        }
        return Long.parseLong(String.valueOf(o));
    }

    private String getString(String key, Map<String, Object> data) {
        return (String) data.get(key);
    }
}
