# Moo - so you can chat from command
-----------
Mooo is a simple console based Java chat service.

Stack:

 * Java 8
 * Spring Boot

Features:

 * Server support for multiple clients
 * Server JMX reporting

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

## Client
-----------
Starting client:
```
cd moo/moo-client/
mvn spring-boot:run
```
Client will attempt to connect to server at `localhost` on port `8000`. 
This can be re-configured via `application.properties`. Client aborts 
immediately if server connection cannot be established.

## Chatting
-----------
Start another client and have fun! Moo...

## Future Vision
-----------
 * More clients! (gui?)
 * More servers! (something different than socket)
 * More features! (support for channels, end to end encryption, etc) 