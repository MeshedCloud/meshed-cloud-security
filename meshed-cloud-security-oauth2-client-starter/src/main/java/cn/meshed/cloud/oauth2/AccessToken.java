package cn.meshed.cloud.oauth2;

import lombok.Data;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Data
public class AccessToken {

    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private Long refreshExpiresIn;
    private String clientId;
    private String scope;
    private String openid;

}
