[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.popokis/popok/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.popokis/popok) [![Total alerts](https://img.shields.io/lgtm/alerts/g/soasada/popok.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/soasada/popok/alerts/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/soasada/popok.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/soasada/popok/context:java)

# popok
Popok provides useful classes to work with HTTP based APIs, execute SQL queries via raw JDBC and JSON asynchronous parsing.

_Java 12 maven artifact_

```xml
<dependency>
    <groupId>com.popokis</groupId>
    <artifactId>popok</artifactId>
    <version>1.2.3</version>
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
The [Server.java](/src/main/java/com/popokis/popok/http/Server.java) class uses a `.properties` file, it will try to find
an `app.properties` file within the `resources` folder by default. If you want to specify another name you can use the
`propertiesFilename()` builder method. The mandatory fields of the properties file are:

* `server.http.port` to specify the HTTP port (an integer).
* `server.address` to specify the address of the server (an IP or `localhost`).

Additionally, you can specify optional configuration fields in order to activate HTTPs:

* `server.https.port` to specify the HTTPs port (an integer).
* `security.key.store.password` to specify the keystore password.

If you want to enable the HTTPs feature you must use the `enableHttps()` builder method and the `keyStorePath()` method
to specify the path of the keystore within the `resources` folder.

An example of `app.properties` file could be:

```java
server.http.port=8080
server.address=localhost
server.https.port=8443
security.key.store.password=password
```

The Server class also has two additional methods:
* `redirectToHttps()` to redirect every request from HTTP to HTTPs.
* `enableHttp2()` to enable HTTP2.
