# Moo - browser based UI client
=====================

Very simple single page web app based on Angular, websockets, REST and Spring 
Boot. Graphical client for moo-server.

## Running
-----------
Before starting the client, ensure that server is running:
```
cd moo/moo-server
mvn spring-boot:run
```
Once server is up and running, angular client can be started:
```
cd moo/moo-ui-angular
mvn spring-boot:run
```
The app immediately listens (and records) server activity, even if page was 
not loaded yet.

Point your favorite browser at:
```
localhost:8080
```

## Limitations
-----------
Re-loading page aborts established chat session with the server without 
notifying the server. As a result, server continues to track a user that is 
no longer in chat (reports incorrect stats). This can be resolved by notifying 
the server properly or by having the server track users activity timeout.