FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/Api-Gateway-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8765
ENTRYPOINT ["java","-jar","app.jar"]
