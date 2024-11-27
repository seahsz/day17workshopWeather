FROM eclipse-temurin:23.0.1_11-jdk

LABEL maintainer="seahsz"

WORKDIR /myApp

COPY mvnw .
COPY pom.xml .
COPY src src
COPY .mvn .mvn

# Build the application
RUN chmod a+x ./mvnw && ./mvnw package -Dmaven.test.skip=true

# For Railway
ENV PORT=8080

# Need to set these to the Redis (Railway Variables) to run Local Docker Image (but left empty for security reason)
# can set up using -e in terminal
ENV SPRING_DATA_REDIS_HOST=localhost 
ENV SPRING_DATA_REDIS_PORT=6379
ENV SPRING_DATA_REDIS_USERNAME= 
ENV SPRING_DATA_REDIS_PASSWORD=

# Need to set this to get API from ...weather.org to run Local Docker Image (but left empty for security reason)
# can set up using -e in terminal
ENV SPRING_DATA_WEATHER_APIKEY=

# What port does the application need
EXPOSE ${PORT}

SHELL [ "/bin/sh", "-c" ]

ENTRYPOINT SERVER_PORT=${PORT} java -jar target/day17workshopWeather-0.0.1-SNAPSHOT.jar



