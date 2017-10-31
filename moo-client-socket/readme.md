# Moo - socket client
---------------------
Websocket based client. If using socket server implementation, this runtime is 
placed as dependency of a UI:
```
<dependency>
  <groupId>pl.zimowski</groupId>
  <artifactId>moo-client-socket</artifactId> 
  <scope>runtime</scope>
</dependency>
```
By default client attempts connection at `localhost` on port `8000`. This can 
be reconfigured via `application.properties` using `server.host` and 
`server.port` properties.