# Moo - JMS client
---------------------
100% pure JMS client support for JMS server. It is required as runtime 
dependency of a UI is [JMS server](../moo-server-jms/readme.md) is used.

## Moo Client

Since this is just a jar dependency, there is no client to start. Just 
make sure that UI has the following client enabled in its `pom.xml`:
```
<dependency>
  <groupId>pl.zimowski</groupId>
  <artifactId>moo-client-jms</artifactId>
  <scope>runtime</scope>
</dependency>
<dependency>
  <groupId>org.apache.activemq</groupId>
  <artifactId>activemq-core</artifactId>
  <scope>runtime</scope>
</dependency>
```
Above assumes that server is running on Apache ActiveMQ. If not using ActiveMQ 
substitute with different broker dependency. See server readme for more details.