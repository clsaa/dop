FROM openjdk:8-jdk-alpine

VOLUME /tmp

ADD **/*.jar app.jar

RUN sh -c 'touch /app.jar'

ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"

CMD exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=production -jar /app.jar