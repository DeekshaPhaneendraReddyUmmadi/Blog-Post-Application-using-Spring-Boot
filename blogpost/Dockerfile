# FROM openjdk:17
# RUN mkdir /app
# COPY target/dockerudemy.jar app
# WORKDIR /app

# # CMD java DemoApplication

# ENTRYPOINT [ "java" ,"-jar" ,"dockerudemy.jar" ]




# Use an OpenJDK base image
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR file into the container
COPY target/dockerudemy.jar /app/dockerudemy.jar

# Expose the port that your Spring Boot application listens on
# EXPOSE 8080

# Command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "dockerudemy.jar"]
