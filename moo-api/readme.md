# Moo - Chat API
---------------------
Common API shared by UI, client and server. Because UI is bound to an 
API, and client powers UI at runtime, it is easy to build any new UI 
client by simply implementing this API.

A UI application essentially must inject implementation of `ClientHandling` 
(which is provided by client runtime), and register `ClientListener` which 
will process chat events.