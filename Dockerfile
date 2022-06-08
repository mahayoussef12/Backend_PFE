FROM adoptopenjdk/openjdk11:alpine-jre
ADD target/user-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","app.jar"]
