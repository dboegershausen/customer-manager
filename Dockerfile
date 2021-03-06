FROM openjdk:11-jre-slim

ENV TZ America/Sao_Paulo

COPY ./target/customermanager.jar /app/

ENTRYPOINT exec java $JAVA_OPTIONS -jar /app/customermanager.jar

EXPOSE 9000
