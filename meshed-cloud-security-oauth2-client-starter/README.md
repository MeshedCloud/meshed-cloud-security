# meshed-cloud-security-oauth2-client-starter

推送制品库
```shell
mvn clean install org.apache.maven.plugins:maven-deploy-plugin:2.8:deploy -DskipTests
```

# 使用

## 依赖
```xml
<dependency>
    <groupId>cn.meshed.cloud</groupId>
    <artifactId>meshed-cloud-security-oauth2-client-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## 配置

```yaml
oauth2:
  host: http://localhost:7989/iam
spring:
  security:
    oauth2:
      client:
        registration:
          default:
            clientId: 6c6a53c614944917bebff88b439eb85f
            clientSecret: 914dbcdf96a748c98b1259b8c24b0913
            authorization-grant-type: code
            client-authentication-method: client_secret_post
            redirect-uri: http://localhost:9200/monitor/login/oauth2/code/default
            client-name: 统一身份中心
            scope:
              - userinfo
        provider:
          default:
            authorization-uri: ${oauth2.host}/oauth2/authorize
            token-uri: ${oauth2.host}/oauth2/token
            user-info-uri: ${oauth2.host}/current/userinfo
            user-name-attribute: name
```