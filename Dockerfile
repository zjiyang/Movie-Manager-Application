# One image serves both halves. The browser then talks to the origin it was
# loaded from, which is why there is no CORS configuration anywhere and no API
# address compiled into the bundle.

# 1. Build the browser bundle.
FROM node:22-alpine AS frontend
WORKDIR /build
# Dependencies first: this layer is reused whenever only source files change.
COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci
COPY frontend/ ./
RUN npm run build

# 2. Build the executable jar, with the bundle inside it.
FROM maven:3.9-eclipse-temurin-21 AS backend
WORKDIR /build
COPY backend/pom.xml ./pom.xml
RUN mvn -B -q dependency:go-offline
COPY backend/src ./src
# Spring Boot serves whatever sits in static/. Routing lives in the URL hash, so
# a deep link never reaches the server and no rewrite rules are needed.
COPY --from=frontend /build/dist ./src/main/resources/static
RUN mvn -B -q -DskipTests package

# 3. Run it. A JRE, not a JDK, and not as root.
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S app && adduser -S -G app app
WORKDIR /app
COPY --from=backend /build/target/*.jar app.jar
USER app
EXPOSE 8080
# Containers publish a port, so loopback would make the service unreachable.
ENV SERVER_ADDRESS=0.0.0.0
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
