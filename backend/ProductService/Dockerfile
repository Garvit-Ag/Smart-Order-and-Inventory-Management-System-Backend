FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/ProductService-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9095
ENTRYPOINT ["java","-jar","app.jar"]
