FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Cache Maven dependencies first
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copy source
COPY src ./src
COPY testng*.xml ./

# Build without running tests
RUN mvn package -DskipTests -q

# ─── Runtime stage ────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Non-root user for security
RUN addgroup -S automation && adduser -S automation -G automation

# Copy built artifacts
COPY --from=builder /app /app
COPY --from=builder /root/.m2 /home/automation/.m2

# Directories for outputs
RUN mkdir -p /app/test-output/screenshots \
             /app/test-output/logs \
             /app/target/allure-results && \
    chown -R automation:automation /app

USER automation

ENV BROWSER=chrome
ENV HEADLESS=true
ENV REMOTE=false
ENV GRID_URL=http://selenium-hub:4444/wd/hub
ENV MAVEN_OPTS="-Xmx2048m"

ENTRYPOINT ["mvn", "test"]
CMD ["-Psmoce", "-Dbrowser=${BROWSER}", "-Dheadless=${HEADLESS}", "-Dremote=${REMOTE}", "-Dgrid.url=${GRID_URL}"]
