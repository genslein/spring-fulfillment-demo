FROM adoptopenjdk/openjdk14:latest

WORKDIR /app

EXPOSE 8080

ADD ./target/fulfillment-*.jar fulfillment.jar
ENTRYPOINT ["java"]
CMD ["-jar", "fulfillment.jar"]