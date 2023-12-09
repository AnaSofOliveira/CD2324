FROM openjdk:11

RUN mkdir /usr/markapp

WORKDIR /usr/markapp

COPY MarkApp.jar .

ENTRYPOINT ["java", "-jar", "MarkApp.jar"]

VOLUME /usr/datafiles
