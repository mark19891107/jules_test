# Use a specific OpenJDK version for reproducibility
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml to download dependencies
COPY ./mvnw .
COPY ./.mvn ./.mvn
COPY ./pom.xml .

# Download dependencies. This layer is cached Docker unless pom.xml changes.
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the application source code
COPY ./src ./src

# Compile the application and package it into a JAR file
RUN ./mvnw package -DskipTests

# Expose port 8080
EXPOSE 8080

# Command to run the application
# Replace 'your-app-name.jar' with the actual name of your JAR file if different
# Usually, it's in the format <artifactId>-<version>.jar
# You can find this in your pom.xml or by inspecting the target directory after building
CMD ["java", "-jar", "target/todo-backend-0.0.1-SNAPSHOT.jar"]
