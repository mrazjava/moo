[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Build Status](https://travis-ci.org/mrazjava/moo.svg?branch=develop)](https://travis-ci.org/mrazjava/moo?branch=develop) [![Coverage Status](https://coveralls.io/repos/github/mrazjava/moo/badge.svg?branch=develop)](https://coveralls.io/github/mrazjava/moo?branch=develop)

# A simple, modular, extensible chat platform
<p align="center"><img src="moo.png" width=256 alt="Moo Logo"/> mooooooooooo</p>

---------------------
Moo is a Java based chat service written as test bed for various concepts. It features 
a modular design, which separates the concepts of a UI, client and a server. As a 
result, a UI can be developed based on a common chat API, without compile time dependency 
on neither client or a server. Depending on which server is running, a UI only needs 
a compatible runtime client dependency. 

### Stack

 * Java 8
 * Web Sockets
 * Spring Boot

### Features

 * Distributed Architecture
 * Server JMX reporting
 * Console UI

## Screenshot
---------------------
![Moo Shell UI in Action](/docs/images/moo-ui-shell-tmux.png?raw=true "Shell UI Screenshot")

Here we have a 4 way tmux session. In the left upper corner we're running moo server. In 
the right upper we have a moo reader which displays chat activity. In both bottom corners, 
we're running two instances of a writer, simulating user chat experience. In a typical use 
case, end user would run one instance of a reader and writer only. 

## First things first
---------------------
Make a local build:
```
cd moo/
mvn install
```

## Server
---------------------
Starting server:
```
cd moo/moo-server-socket/
mvn spring-boot:run
```
Server starts on port `8000`. Port can be changed via `application.properties`. 
Server reports client count stats to JMX console. Start the JMX console with a 
server PID and watch `ClientAnalytics` MBean.

## Client
---------------------
Socket server, requires compatible socket client. Make sure that the UI of your 
choice, has enabled relevant maven dependency. For example, command line UI 
would need to have commented out the following in `moo-ui-shell/pom.xml`:
```
<!--
only one moo-client implementation can be enabled otherwise spring 
IOC will not resolve injection points due to multiple implementations
-->
<dependency>
  <groupId>pl.zimowski</groupId>
  <artifactId>moo-client-socket</artifactId>
  <scope>runtime</scope>
</dependency>
```

## UI
---------------------
Starting console UI requires two terminal sessions. It's probably easiest to 
split terminal session into two using something like `screen` or `tmux`.

First, start UI reader which will allow to view public chats:
```
cd moo/moo-ui-shell/moo-ui-shell-reader
mvn spring-boot:run
```
Reader will attempt to connect to server at `localhost` on port `8000`. 
This can be re-configured via `application.properties` of `moo-client-*` that 
is used at runtime, or more conveniently, by overriding these spring managed 
props as command line args. Client aborts immediately if server connection cannot 
be established.

To be able to actually send chat messages, it is necessary to have a running 
instance of a writer:
```
cd moo/moo-ui-shell/moo-ui-shell-writer
mvn spring-boot:run
```
Because writer uses the same client as reader, same customization strategy 
applies. If server is based on websockets, then websocket client should be 
used. If server is based on JMS, then JMS client should be used, etc.

## Code Coverage
---------------------
Playing around with [Coveralls](https://coveralls.io/github/mrazjava/moo) Github plugin, 
found it interesting but imperfect. The readme badge is really nice, but it is not 
reliably updated when coverage changes and there are a lot of complaints about it posted 
on the web. For me, sometimes it updates instantly, other times takes 24 hours. Don't 
trust the badge. The sure way to verify code coverage is to invoke suite locally:
```
cd moo/
mvn clean verify
```
Then check `jacoco` reports:
```
cd moo-reports/target/site/jacoco-aggregate/
```
and open `index.html` in your favorite browser.

## Care to help or join?
---------------------
This could be more fun with alternate UIs. For example, web based Angular UI would 
be nice. Or Eclipse RCP ui. Or JavaFX. You get the point. If you feel like 
hacking please issue a PR against `develop` branch and I will happily merge your 
improvements. Other areas of improvemnts are obviously server/api/feature related.

*Enjoy!*