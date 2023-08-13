FROM eclipse-temurin:17-jdk-alpine
VOLUME C:\dockerVol
ARG target\*.jar
COPY  target/*.jar product-orchestration.jar
ENTRYPOINT ["java","-jar","/product-orchestration.jar"]