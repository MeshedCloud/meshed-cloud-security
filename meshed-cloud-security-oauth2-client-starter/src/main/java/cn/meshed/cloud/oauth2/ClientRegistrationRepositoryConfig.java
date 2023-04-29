package cn.meshed.cloud.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
public class ClientRegistrationRepositoryConfig {

    private final OAuth2ClientProperties oAuth2ClientProperties;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(clientRegistrations());
    }

    /**
     * oauth 配置
     *
     * @return
     */
    private List<ClientRegistration> clientRegistrations() {
        if (oAuth2ClientProperties.getRegistration() != null && !oAuth2ClientProperties.getRegistration().isEmpty()) {
            return oAuth2ClientProperties.getRegistration().entrySet().stream()
                    .map(entry -> clientRegistration(entry.getKey(), entry.getValue(),
                            oAuth2ClientProperties.getProvider().get(entry.getKey()))).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private ClientRegistration clientRegistration(String key, OAuth2ClientProperties.Registration registration, OAuth2ClientProperties.Provider provider) {
        return ClientRegistration.withRegistrationId(key)
                .clientId(registration.getClientId())
                .clientSecret(registration.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope(registration.getScope())
                .authorizationUri(provider.getAuthorizationUri())
                .tokenUri(provider.getTokenUri())
                .userInfoUri(provider.getUserInfoUri())
                .redirectUri(registration.getRedirectUri())
                .userNameAttributeName(provider.getUserNameAttribute())
                .jwkSetUri(provider.getJwkSetUri())
                .clientName(registration.getClientName())
                .build();
    }


}
