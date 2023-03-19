########
# Dockerfile to build billing-service container image
#
########
FROM openjdk:17-slim

LABEL maintainer="Petrulin Alexander"

COPY target/billing-service-*.jar app.jar

EXPOSE 8050

ENTRYPOINT ["java","-jar","/app.jar"]
