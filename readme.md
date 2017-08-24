# Moo - so you can chat from command
-----------
Mooo is a simple console based Java chat service.

Stack:

 * Java 8
 * Angular
 * REST
 * Spring Boot

Features:

 * Distributed Architecture (eg: console client can chat with web client)
 * Server JMX reporting
 * Console client
 * Web client (single page angular/rest/springboot)

## First things first
-----------
Make a local build:
```
cd moo/
mvn install
```

## Server
-----------
Starting server:
```
cd moo/moo-server/
mvn spring-boot:run
```
Server starts on port `8000`. Port can be changed via `application.properties`. 
Server reports client count stats to JMX console. Start the JMX console with a 
server PID and watch `ClientAnalytics` MBean.

## Client
-----------
Starting console client:
```
cd moo/moo-client/
mvn spring-boot:run
```
Client will attempt to connect to server at `localhost` on port `8000`. 
This can be re-configured via `application.properties`. Client aborts 
immediately if server connection cannot be established.

Starting web client:
```
cd moo/moo-ui-web1
mvn spring-boot:run
```
This client will also attempt to connect to server at `localhost` on port 
`8000`. This client right now does not expose configuration options.

## Chatting
-----------
Start yet another client and have fun! Moo...

## Future Vision
-----------
 * More clients! (various flavors of web, android, ios)
 * More servers! (maybe node? with gui?)
 * Alternate implementation of ChatService (different engine, same server)
 * More features! (direct/private user chats, channels, encryption, etc) 