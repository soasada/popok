[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.popokis/popok/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.popokis/popok) [![Total alerts](https://img.shields.io/lgtm/alerts/g/soasada/popok.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/soasada/popok/alerts/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/soasada/popok.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/soasada/popok/context:java)

# popok
Popok provides useful classes to work with HTTP based APIs, execute SQL queries via raw JDBC and JSON asynchronous parsing.

_Java 13 maven artifact_

```xml
<dependency>
    <groupId>com.popokis</groupId>
    <artifactId>popok</artifactId>
    <version>1.3.14</version>
</dependency>
```

#### NOTE: Feel free to do a pull request or create an issue if you need any new features or find a bug.

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
Are you looking for a more advanced example? [Click here](https://github.com/soasada/undertow-vuejs)
## Bootstrapping a Server

[Server.java](/src/main/java/com/popokis/popok/http/Server.java) it's just an Undertow wrapper class to add some functionality such as: HTTPs/HTTP2 support and HTTP to HTTPs redirection. To instantiate a Server you have to provide an undertow `HttpHandler` as router and use the builder API that provides you the following methods:

```java
/**
* Sets a different name for the properties configuration file.
* If you don't use this method popok will look inside 'resources'
* folder, looking for 'app.properties' default file.
*/
propertiesFilename(String name)
```

```java
/**
* Enables HTTPs support. You have to specify the keystore path,
* popok will look inside 'resources' folder, looking for the 'keyStorePath'.
*/
enableHttps(String keyStorePath)
```

```java
/**
* Enables HTTP to HTTPs redirection. You have to specify which
* HTTP `3XX` code you want to use.
*/
redirectToHttps(int statusCode)
```

```java
/**
* Enables HTTP2 support.
*/
enableHttp2()
```

The mandatory fields, that popok needs for the properties configuration file, are:

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
