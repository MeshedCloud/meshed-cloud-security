package cn.meshed.cloud.oauth2;

import cn.meshed.cloud.oauth2.ConsumerOAuth2AccessTokenResponseClient;
import cn.meshed.cloud.oauth2.ConsumerOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
public class Oauth2ClientSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    @Autowired
    private OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;


    private final ConsumerOAuth2UserService consumerOAuth2UserService;
    private final ConsumerOAuth2AccessTokenResponseClient consumerOAuth2AccessTokenResponseClient;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/actuator", "/actuator/**", "/login", "/logout", "/login/**", "/error").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()

                .tokenEndpoint().accessTokenResponseClient(consumerOAuth2AccessTokenResponseClient)
                .and()
                .userInfoEndpoint().userService(consumerOAuth2UserService)
                .and().and()
                .oauth2Client()
                .authorizedClientService(authorizedClientService)
                .authorizedClientRepository(oAuth2AuthorizedClientRepository)
                .authorizationCodeGrant()
                .accessTokenResponseClient(consumerOAuth2AccessTokenResponseClient)
//                .authorizationRequestResolver()
                .authorizationRequestRepository(this.cookieAuthorizationRequestRepository());
////                .loginPage("/login")

//                .userInfoEndpoint().userService(customOauth2UserService)
//                .and()
//                .successHandler(oauth2LoginSuccessHandler)
//                .failureHandler(oauth2LoginFailedHandler)
//                .and()
//                .logout()
//                .logoutUrl("/auth/logout")
//                .logoutSuccessHandler(oauth2LoginOutSuccessHandler)
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .deleteCookies("JSESSIONID");

    }

    private AuthorizationRequestRepository<OAuth2AuthorizationRequest> cookieAuthorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }


}
