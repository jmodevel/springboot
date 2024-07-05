FROM openjdk:11-jre-slim
VOLUME /tmp
ARG JAR_FILE=target/zuul-ping-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} zuul-ping.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/zuul-ping.jar"]