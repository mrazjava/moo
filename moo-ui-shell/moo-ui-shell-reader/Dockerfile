FROM adoptopenjdk/openjdk11:latest

RUN mkdir /opt/moo
COPY target/moo-ui-shell-reader-*.jar /opt/moo/moo-ui-shell-reader.jar

CMD ["java", "-jar", "/opt/moo/moo-ui-shell-reader.jar"]