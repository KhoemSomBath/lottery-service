FROM adoptopenjdk/openjdk14

ARG JAR_FILE=artifacts/system-service.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
