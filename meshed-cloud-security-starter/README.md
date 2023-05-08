# meshed-cloud-security-starter

**微服务安全**

## 推送制品库

```shell
mvn clean install org.apache.maven.plugins:maven-deploy-plugin:2.8:deploy -DskipTests
```

## 使用

```xml
<dependency>
    <groupId>cn.meshed.cloud</groupId>
    <artifactId>meshed-cloud-security-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
## 配置

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

