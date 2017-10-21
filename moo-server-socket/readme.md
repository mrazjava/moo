# Moo - socket chat server
---------------------
Simple web sockets server for Moo chat service.

Starting server:
```
cd moo/moo-server-socket/
mvn spring-boot:run
```
Server starts on port `8000`. Port can be changed via `application.properties`. 
Server reports client count stats to JMX console. Start the JMX console with a 
server PID and watch `ClientAnalytics` MBean.

![Moo JMX Screenshot](../moo/docs/images/moo-jmx-console.png?raw=true "Moo via JMX")
