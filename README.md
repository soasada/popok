[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.popokis/popok/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.popokis/popok) [![Total alerts](https://img.shields.io/lgtm/alerts/g/soasada/popok.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/soasada/popok/alerts/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/soasada/popok.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/soasada/popok/context:java)

# popok
Popok provides useful classes to work with HTTP based APIs, execute SQL queries via raw JDBC and JSON asynchronous parsing.

_Java 12 maven artifact_

```xml
<dependency>
    <groupId>com.popokis</groupId>
    <artifactId>popok</artifactId>
    <version>1.3.0</version>
</dependency>
```

## Simple HTTP API example

Popok use [undertow](https://github.com/undertow-io/undertow) as HTTP server, so creating a router is as simple as creating an undertow `HttpHandler`.

```java
// Router
HttpHandler router = Handlers.path()
  .addPrefixPath("/api/v1", Handlers.routing()
    .get("/hello", (exchange) -> {
      exchange.setStatusCode(StatusCodes.OK);
      exchange.getResponseSender().send("Hello World!");
    })
  );
// Create and start the server
Server.builder(router)
  .build()
  .start();
```

## Bootstrapping a Server

The [Server.java](/src/main/java/com/popokis/popok/http/Server.java) it's just a wrapper class over Undertow class to add some functionality such as: HTTPs/HTTP2 support and HTTP to HTTPs redirection. To instantiate a Server you have to provide an undertow `HttpHandler` as router and use the builder API that provides you the following methods:

```java
propertiesFilename(String name)
```
If you want to specify a different name for the properties configuration file (default: `app.properties`).

```java
enableHttps(String keyStorePath)
```
Enables HTTP support. You have to specify the keystore path inside the `resources` folder.

```java
redirectToHttps(int statusCode)
```
Enables HTTP to HTTPs redirection. You have to specify the HTTP `3XX` code that you need.

```java
enableHttp2()
```
Enables HTTP2 support.

The Server class uses a `.properties` file, by default `app.properties` inside the `resources` folder. The mandatory fields of the properties file are:

* `server.http.port` to specify the HTTP port (an integer).
* `server.address` to specify the address of the server (an IP or `localhost`).

Additionally, you can specify optional configuration fields in order to activate HTTPs:

* `server.https.port` to specify the HTTPs port (an integer).
* `security.key.store.password` to specify the keystore password.

An example of `app.properties` file could be:

```java
server.http.port=8080
server.address=localhost
server.https.port=8443
security.key.store.password=password
```
