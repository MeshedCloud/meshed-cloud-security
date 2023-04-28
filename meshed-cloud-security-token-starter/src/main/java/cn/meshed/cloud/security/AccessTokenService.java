package cn.meshed.cloud.security;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface AccessTokenService {

    /**
     * 生成密钥
     * @param payloadStr 加密内容
     * @return token
     */
    String generateToken(String payloadStr);

    /**
     * 校验并解析
     *
     * @param token  token
     * @return info string
     */
    String verifyToken(String token);
}
