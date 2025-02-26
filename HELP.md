# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.2/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.2/maven-plugin/build-image.html)
* [Spring Integration JDBC Module Reference Guide](https://docs.spring.io/spring-integration/reference/jdbc.html)
* [Spring Integration JPA Module Reference Guide](https://docs.spring.io/spring-integration/reference/jpa.html)
* [Spring Integration Test Module Reference Guide](https://docs.spring.io/spring-integration/reference/testing.html)
* [Spring Integration Apache Kafka Module Reference Guide](https://docs.spring.io/spring-integration/reference/kafka.html)
* [Spring Integration Mail Module Reference Guide](https://docs.spring.io/spring-integration/reference/mail.html)
* [Spring Integration Security Module Reference Guide](https://docs.spring.io/spring-integration/reference/security.html)
* [Spring Integration HTTP Module Reference Guide](https://docs.spring.io/spring-integration/reference/http.html)
* [Spring Integration WebFlux Module Reference Guide](https://docs.spring.io/spring-integration/reference/webflux.html)
* [Spring Integration STOMP Module Reference Guide](https://docs.spring.io/spring-integration/reference/stomp.html)
* [Spring Integration WebSocket Module Reference Guide](https://docs.spring.io/spring-integration/reference/web-sockets.html)
* [Spring Integration Web Services Module Reference Guide](https://docs.spring.io/spring-integration/reference/ws.html)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/3.4.2/reference/actuator/index.html)
* [Config Client](https://docs.spring.io/spring-cloud-config/reference/client.html)
* [Config Server](https://docs.spring.io/spring-cloud-config/reference/server.html)
* [Eureka Discovery Client](https://docs.spring.io/spring-cloud-netflix/reference/spring-cloud-netflix.html#_service_discovery_eureka_clients)
* [Eureka Server](https://docs.spring.io/spring-cloud-netflix/reference/spring-cloud-netflix.html#spring-cloud-eureka-server)
* [Gateway](https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway-server-mvc.html)
* [Reactive Gateway](https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway.html)
* [Spring Data JDBC](https://docs.spring.io/spring-boot/3.4.2/reference/data/sql.html#data.sql.jdbc)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.4.2/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Datadog](https://docs.spring.io/spring-boot/3.4.2/reference/actuator/metrics.html#actuator.metrics.export.datadog)
* [Docker Compose Support](https://docs.spring.io/spring-boot/3.4.2/reference/features/dev-services.html#features.dev-services.docker-compose)
* [Flyway Migration](https://docs.spring.io/spring-boot/3.4.2/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)
* [Spring for GraphQL](https://docs.spring.io/spring-boot/3.4.2/reference/web/spring-graphql.html)
* [Spring Integration](https://docs.spring.io/spring-boot/3.4.2/reference/messaging/spring-integration.html)
* [Jersey](https://docs.spring.io/spring-boot/3.4.2/reference/web/servlet.html#web.servlet.jersey)
* [Spring for Apache Kafka](https://docs.spring.io/spring-boot/3.4.2/reference/messaging/kafka.html)
* [Java Mail Sender](https://docs.spring.io/spring-boot/3.4.2/reference/io/email.html)
* [OAuth2 Authorization Server](https://docs.spring.io/spring-boot/3.4.2/reference/web/spring-security.html#web.security.oauth2.authorization-server)
* [OAuth2 Client](https://docs.spring.io/spring-boot/3.4.2/reference/web/spring-security.html#web.security.oauth2.client)
* [OAuth2 Resource Server](https://docs.spring.io/spring-boot/3.4.2/reference/web/spring-security.html#web.security.oauth2.server)
* [Spring Security](https://docs.spring.io/spring-boot/3.4.2/reference/web/spring-security.html)
* [Spring Session](https://docs.spring.io/spring-session/reference/)
* [Validation](https://docs.spring.io/spring-boot/3.4.2/reference/io/validation.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.2/reference/web/servlet.html)
* [Spring Web Services](https://docs.spring.io/spring-boot/3.4.2/reference/io/webservices.html)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/3.4.2/reference/web/reactive.html)
* [WebSocket](https://docs.spring.io/spring-boot/3.4.2/reference/messaging/websockets.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Centralized Configuration](https://spring.io/guides/gs/centralized-configuration/)
* [Service Registration and Discovery with Eureka and Spring Cloud](https://spring.io/guides/gs/service-registration-and-discovery/)
* [Service Registration and Discovery with Eureka and Spring Cloud](https://spring.io/guides/gs/service-registration-and-discovery/)
* [Using Spring Cloud Gateway](https://github.com/spring-cloud-samples/spring-cloud-gateway-sample)
* [Using Spring Data JDBC](https://github.com/spring-projects/spring-data-examples/tree/master/jdbc/basics)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a GraphQL service](https://spring.io/guides/gs/graphql-server/)
* [Integrating Data](https://spring.io/guides/gs/integration/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Producing a SOAP web service](https://spring.io/guides/gs/producing-web-service/)
* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)
* [Using WebSocket to build an interactive web application](https://spring.io/guides/gs/messaging-stomp-websocket/)

## GraphQL code generation with DGS

This project has been configured to use the Netflix DGS Codegen plugin.
This plugin can be used to generate client files for accessing remote GraphQL services.
The default setup assumes that the GraphQL schema file for the remote service is added to the
`src/main/resources/graphql-client/` location.

You can learn more about the [plugin configuration options](https://github.com/deweyjose/graphqlcodegen) and
[how to use the generated types](https://netflix.github.io/dgs/generating-code-from-schema/) to adapt the default setup.

### Docker Compose support

This project contains a Docker Compose file named `compose.yaml`.
In this file, the following services have been defined:

* postgres: [`postgres:latest`](https://hub.docker.com/_/postgres)

Please review the tags of the used images and set them to the same as you're running in production.

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the
parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.
