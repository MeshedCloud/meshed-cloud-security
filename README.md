# meshed-cloud-security

安全starter管理

## 介绍
微服务快速开发流平台 [meshed-cloud](cloud.meshed.cn) 

**安全依赖管理工程**

推送制品库
```shell
mvn clean install org.apache.maven.plugins:maven-deploy-plugin:2.8:deploy -DskipTests
```

## 导航

### 微服务安全

```xml
<dependency>
    <groupId>cn.meshed.cloud</groupId>
    <artifactId>meshed-cloud-security-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
配置，提供开发是模拟用户生成，产线环境下mock无效
```yaml
service:
  security:
    exclude-uris:
      - /actuator
      - /actuator/**
      - /doc.html
      - /v3/**
    mock:
      enable: true
      user-id: 1
      username: name
      access: 6
      roles: RD:ADMIN
```

### 内网令牌

主要网关和security引用，生成内网安全令牌
```xml
<dependency>
    <groupId>cn.meshed.cloud</groupId>
    <artifactId>meshed-cloud-security-token-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

密钥配置，微服务一般不用独立配置，归属到通用配置
```yaml
service:
  security:
    secret: cAu9eYo3N4U8HnJeeSxoWjp3Wd1O6XmSNH2RBwbbUkrHJz4Cs0fnJyxD65wlFAuTfAnTJljs3TRa2LOy51gS9Gczt84oivybxajYvN9BpiF2TSbfXLmleSn6SAGX8efW
```

### OAUTH2客户端

```xml
<dependency>
    <groupId>cn.meshed.cloud</groupId>
    <artifactId>meshed-cloud-security-oauth2-client-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

配置

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





