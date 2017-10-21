# Moo - Chat API
---------------------
Common API for implementing moo components: UI, client or a server. Because of   
this API, UI and client/server pairs are completely decoupled.

UI is simply deployed against a client runtime dependency. It essentially must 
inject instance of `ClientHandling` (which is provided by client runtime), and 
register its own implementation of  `ClientReporting` which is reports 
server generated chat events.

A client/server pair obviously must be compatible (web socket client expects 
a running web socket server).