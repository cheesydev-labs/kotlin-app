#
# builder
#
FROM openjdk:8-alpine AS builder

RUN addgroup -S appbuilder && adduser -S -G appbuilder appbuilder
USER appbuilder
WORKDIR /home/appbuilder

COPY gradle/ gradle/
COPY gradlew .
COPY build.gradle.kts settings.gradle.kts ./
COPY src src
RUN ./gradlew --no-daemon bootJar

#
# dist
#
FROM openjdk:8-jre-alpine AS dist

RUN addgroup -S appuser && adduser -S -G appuser appuser
USER appuser
WORKDIR /home/appuser

COPY --from=builder --chown=appuser /home/appbuilder/build/libs/demo-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]
