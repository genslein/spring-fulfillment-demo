# Fulfillment Demo Application

This is the demo Fulfillment application associated with my blog posts about my experience with Java and Spring Boot as a Ruby lover.


### Auto-generated Getting Started Section

#### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.0/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.0/maven-plugin/reference/html/#build-image)
* [Testcontainers Postgres Module Reference Guide](https://www.testcontainers.org/modules/databases/postgres/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.4.0/reference/htmlsingle/#boot-features-security)
* [Testcontainers](https://www.testcontainers.org/)

#### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)

## Docker Debugging

### Prometheus and Grafana locally
Because Prometheus and Grafana are not required, they are optional to run.
If there is a need/desire for validating metrics collection, you can add optionally.
To run Prometheus + Grafana with your Spring Boot local environment use the following from
the base directory of this repository:
```bash
docker-compose -f docker-compose.yml -f docker-prometheus/docker-compose.yml up -d
docker-compose -f docker-compose.yml -f docker-prometheus/docker-compose.yml ps
```
Locations as follows:
- [Prometheus port 9090](http://localhost:9090)
- [Grafana 3000](http://localhost:3000)
    - user:pw is admin:admin
    - You will need to configure the dashboard and datasource manually for now
- [JVM dashboard Template](https://grafana.com/grafana/dashboards/4701)
    - [Postgresql Dashboard](https://grafana.com/grafana/dashboards/9628)
    - List of [Other dashboard templates](https://grafana.com/grafana/dashboards)

### Documentation
You can have Fulfillment demo generate its own OpenAPI 3.0 JSON specification using the local docker-compose setup.

```bash
mvn clean install -Dskip-tests
docker-compose up -d

curl -X GET http://localhost:8080/v3/api-docs
```
Or you can navigate in a web browser to 
[OpenAPI localhost Swagger URL](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config) for the Swagger UI.

### Debugging QueryDSL

A couple of things to try if Q-classes don't generate.

* Check the plugin for JPA annotation processing [querydsl#3431](https://github.com/querydsl/querydsl/issues/3431)
* Make sure `jakarta` is the persistence driver [querydsl#3436](https://github.com/querydsl/querydsl/issues/3436)
* Run `mvn clean install -DskipTests` to run builds and check the `target/classes/.../models` folder