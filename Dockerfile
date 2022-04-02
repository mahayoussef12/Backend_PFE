FROM openjdk:8
COPY ./target/user-0.0.1-SNAPSHOT.jar user-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","user-0.0.1-SNAPSHOT.jar"]