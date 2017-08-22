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
UI server side does not track users. It probably should, using HttpSession. As 
a result, re-loading page aborts established chat link with the server without 
notifying the server. Server continues to track an orphaned user link (reports 
incorrect stats). This can be resolved by UI server tracking user session with 
timeouts and informing server when UI user is no longer active. In addition, 
the moo server should probably do something similar at the connection level, 
so if connection had no activity in some time period, it should be ejected. UI 
client would then have to introduce some sort of a heartbeat to prevent its 
global connection being ejected by the server if there was no chat activity.