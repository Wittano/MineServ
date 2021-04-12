FROM openjdk:11
WORKDIR /app
RUN mkdir -p /app/servers
ARG JAR_FILE=build/libs/*.jar
ARG CORS_DOMAIN
ENV CORS_DOMAIN=${CORS_DOMAIN}
COPY $JAR_FILE /app/api.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/app/api.jar", "--project.client.domain=${CORS_DOMAIN}"]