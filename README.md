[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.popokis/popok/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.popokis/popok)

# ATTENTION: popok is in its early stage, please don't use in production unless you are planning to extend it.

# popok
Super simple, fail-first, web services oriented set of interfaces and default classes on top of undertow for Java 10.

Popok helps you to write SQL queries without an ORM and implement web services with a straightforward web server and
router. One of the aims of popok is to iterate with the new version schedule of Java.

## Unsupported features (yet)

* **HTTPS web server**: popok only provides a simple HTTP web server. If you want to use HTTPS, you have to implement
your own web server with undertow API.

## To run the tests

`docker-compose up`