# Moo - flexible chat
-----------
Moo is a Java based chat service written as test bed for various concepts. Moo features 
a modular design, which separates the concepts of a UI, client and a server. As a 
result, a UI can be developed based on a common chat API, without compile time dependency 
on neither client or a server. Depending on which server is running, a UI only needs 
a compatible runtime client dependency. 

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) &nbsp; master: &nbsp; [![Build Status](https://travis-ci.org/mrazjava/moo.svg?branch=master)](https://travis-ci.org/mrazjava/moo?branch=master) [![Coverage Status](https://coveralls.io/repos/github/mrazjava/moo/badge.svg?branch=master)](https://coveralls.io/github/mrazjava/moo?branch=master) &nbsp; develop: &nbsp; [![Build Status](https://travis-ci.org/mrazjava/moo.svg?branch=develop)](https://travis-ci.org/mrazjava/moo?branch=develop) [![Coverage Status](https://coveralls.io/repos/github/mrazjava/moo/badge.svg?branch=develop)](https://coveralls.io/github/mrazjava/moo?branch=develop)

### Stack

 * Java 8
 * Web Sockets
 * Spring Boot

### Features

 * Distributed Architecture
 * Server JMX reporting
 * Console UI

![Moo Shell UI in Action](/docs/images/moo-ui-shell-tmux.png?raw=true "Shell UI Screenshot" =800x417)

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

## Chatting
-----------
Start yet another client and have fun! Moo...