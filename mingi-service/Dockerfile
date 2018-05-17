FROM openjdk:8-jdk-alpine
WORKDIR /opt/mingi/service
COPY ./mingi-service/target/mingi-service-*.jar mingi-service.jar
COPY ./res/*.* res/
COPY ./qas/*.* qas/
COPY ./res/bin/tree-tagger res/bin/tree-tagger
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,address=2002,server=y,suspend=n","-Djava.security.egd=file:/dev/./urandom","-jar","mingi-service.jar"]
