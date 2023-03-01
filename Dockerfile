FROM eclipse-temurin:11
ARG JAR_FILE=target/TelegramBotTimerForChef-1.0-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]