FROM openjdk:17-jdk-oracle
ARG JAR_FILE=*.jar
COPY build/libs/${JAR_FILE} airbank-be.jar
ENTRYPOINT ["java","-jar","/airbank-be.jar"]