FROM openjdk:11-jre-slim
VOLUME /tmp
ARG JAR_FILE=target/zuul-pong-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} zuul-pong.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/zuul-pong.jar"]