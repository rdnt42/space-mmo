FROM openjdk:17-oracle
WORKDIR /opt
ENV PORT 8084
EXPOSE 8084
COPY target/account-module.jar /opt/player-module.jar
ENTRYPOINT exec java $JAVA_OPTS -jar account-module.jar