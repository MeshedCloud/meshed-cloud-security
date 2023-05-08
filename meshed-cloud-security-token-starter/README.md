# meshed-cloud-security-token-starter

**内网令牌**

## 推送制品库
```shell
mvn clean install org.apache.maven.plugins:maven-deploy-plugin:2.8:deploy -DskipTests
```

## 使用

主要网关和security引用，生成内网安全令牌
```xml
<dependency>
    <groupId>cn.meshed.cloud</groupId>
    <artifactId>meshed-cloud-security-token-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## 配置

密钥配置，微服务一般不用独立配置，归属到通用配置
```yaml
service:
  security:
    secret: cAu9eYo3N4U8HnJeeSxoWjp3Wd1O6XmSNH2RBwbbUkrHJz4Cs0fnJyxD65wlFAuTfAnTJljs3TRa2LOy51gS9Gczt84oivybxajYvN9BpiF2TSbfXLmleSn6SAGX8efW
```
