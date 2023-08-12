FROM eclipse-temurin:17-jdk-alpine
VOLUME C:\dockerVol
ARG target\*.jar
COPY  target/*.jar attendence-listener.jar
ENTRYPOINT ["java","-jar","/attendence-listener.jar"]