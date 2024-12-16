#  Use an existing image (openjdk Java runtime) as the base image for accounts image
FROM openjdk:21-jdk-slim

# Set maintainer details
MAINTAINER jmo.devel

# Add the application's jar to the container
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "/accounts-0.0.1-SNAPSHOT.jar"]