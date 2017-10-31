# Moo - JMS chat server
---------------------
100% pure JMS server for Moo chat service. Requires runtime dependency of a 
JMS compatible message broker. Brokers verified to work with this server are:

* [Apache ActiveMQ](http://activemq.apache.org/)

In general, native JMS brokers should be easy to configure and should be 
supported with only changes to `jndi.properties`. For example, [HornetQ](http://hornetq.jboss.org/) 
from JBoss should also work, though it wasn't tested. On the other hand, a broker like [RabbitMQ](https://www.rabbitmq.com/) 
is not natively JMS compliant. Rabit provides a plugin which requires additional 
configuration (binding file) which will likely not work against this implementation 
of a server out of a box.

## ActiveMQ
---------------------
Download ActiveMQ and install it per official instructions. `Moo` works with 
default ActiveMQ setup so no configuration of any kind is necessary. Assuming 
ActiveMQ was installed in `~/apache-activemq-5.15.1/`, the following would 
start the broker:

Starting the broker:
```
cd ~/apache-activemq-5.15.1/ # ActiveMQ installation directory
./bin/activemq start
```
By default, ActiveMQ starts on port `5672`. To check that broker was started 
successfully do:
```
./bin/activemq status
```
If it turns out that broker start failed, you can check the log:
```
less data/activemq.log
```
which will reveal cause of the problem (most likely port already taken). Once 
broker is running you can use browser GUI to administer it, though it is not 
necessary because `moo` on the first use will automatically configure queues 
and topics. It is still useful to log into the admin console to view the 
messages that `moo` generates. The console is available at `http://localhost:8161/admin/`. 
By default, ActiveMQ requires basic authentication. The credentials are `admin/admin`.

## Moo Server
---------------------
Once the broker is running we can start the server. Server with:
```
cd moo/moo-server-jms/
mvn spring-boot:run
```
Server attempts to immediately establish connection with the broker on port 
`5672`. If broker is not running server will not start.

## Screenshots
---------------------
Moo automatically creates all necessary queues and topics. Here are some views 
of ActiveMQ admin console after running `moo` with a few clients and chat 
events.

Queues:
![Moo ActiveMQ](../docs/images/moo-activemq-queues.png?raw=true "ActiveMQ Moo Queues")

Topics:
![Moo ActiveMQ](../docs/images/moo-activemq-topics.png?raw=true "ActiveMQ Moo Topics")

Connections:
![Moo ActiveMQ](../docs/images/moo-activemq-connections.png?raw=true "ActiveMQ Moo Connections")