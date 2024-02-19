FROM eclipse-temurin:21-jdk

WORKDIR /app

EXPOSE 8080

ADD ./target/fulfillment-*.jar fulfillment.jar
ENTRYPOINT ["java"]
CMD ["-jar", "fulfillment.jar"]