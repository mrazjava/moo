FROM adoptopenjdk/openjdk11:latest

RUN mkdir /opt/moo
COPY target/moo-server-socket-*.jar /opt/moo/moo-server-socket.jar

CMD ["java", "-jar", "/opt/moo/moo-server-socket.jar"]