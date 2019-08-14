[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.popokis/popok/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.popokis/popok)

# popok
Simple util classes for building HTTP based services. Popok provides useful classes to work with HTTP based APIs, SQL databases
and JSON.

## Simple HTTP API example

Popok use undertow as HTTP server so creating a router is as simple as creating an `HttpHandler` of undertow.

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
`propertiesFilename()` method of the builder. The mandatory values of the properties file are:

* `server.http.port` to specify the HTTP port (an integer).
* `server.address` to specify the address of the server (an IP or localhost).

Additionally, you can specify optional configuration parameters in order to activate HTTPs:

* `server.https.port` to specify the HTTPs port (an integer).
* `security.key.store.password` to specify the keystore password.

If you want to enable the HTTPs feature you must use the `enableHttps()` method of the builder and the `keyStorePath()` method
to specify the path within the `resources` folder.

An example of `app.properties` file could be:

```java
server.http.port=8080
server.address=localhost
server.https.port=8443
security.key.store.password=password
```