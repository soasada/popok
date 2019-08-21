/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.popokis.popok.http;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.attribute.ExchangeAttributes;
import io.undertow.server.HttpHandler;
import io.undertow.util.Headers;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Optional;
import java.util.Properties;

import static io.undertow.predicate.Predicates.secure;

public final class Server {

  private final Undertow server;

  private char[] keyStorePassword;

  private Server(Builder builder) {
    try (InputStream fi = Server.class.getResourceAsStream(File.separator + builder.propertiesFilename)) {
      Properties appProps = new Properties();
      appProps.load(fi);

      String httpPort = appProps.getProperty("server.http.port");
      if (builder.nullOrEmpty(httpPort)) throw new RuntimeException("server.http.port property not found.");
      String address = appProps.getProperty("server.address");
      if (builder.nullOrEmpty(address)) throw new RuntimeException("server.address property not found.");

      if (builder.isHttps) {
        String httpsPort = appProps.getProperty("server.https.port");
        if (builder.nullOrEmpty(httpsPort)) throw new RuntimeException("server.https.port property not found.");
        String keyStorePassword = appProps.getProperty("security.key.store.password");
        if (builder.nullOrEmpty(keyStorePassword)) throw new RuntimeException("security.key.store.password property not found.");
        this.keyStorePassword = keyStorePassword.toCharArray();

        this.server = Undertow.builder()
            .setServerOption(UndertowOptions.ENABLE_HTTP2, builder.enableHttp2)
            .setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false)
            .addHttpListener(Integer.parseInt(httpPort), address)
            .addHttpsListener(Integer.parseInt(httpsPort), address, createSSLContext(loadKeyStore(builder.keyStorePath)))
            .setHandler(builder.redirectToHttps ? withHttpsRedirect(builder.router, httpsPort, builder.statusCode) : builder.router)
            .build();
      } else {
        this.server = Undertow.builder()
            .setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false)
            .addHttpListener(Integer.parseInt(httpPort), address)
            .setHandler(builder.router)
            .build();
      }
    } catch (IOException e) {
      throw new RuntimeException(File.separator + builder.propertiesFilename + " not found, please create it inside resources folder.");
    }
  }

  public static Builder builder(HttpHandler router) {
    return new Builder(router);
  }

  public void start() {
    server.start();
  }

  public void stop() {
    server.stop();
  }

  private KeyStore loadKeyStore(String name) {
    try (InputStream is = Server.class.getResourceAsStream(File.separator + name)) {
      KeyStore loadedKeystore = KeyStore.getInstance("JKS");
      loadedKeystore.load(is, keyStorePassword);
      return loadedKeystore;
    } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
      throw new RuntimeException(e);
    }
  }

  private SSLContext createSSLContext(final KeyStore keyStore) {
    try {
      KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      keyManagerFactory.init(keyStore, keyStorePassword);

      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init(keyStore);

      SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
      sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

      return sslContext;
    } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
      throw new RuntimeException(e);
    }
  }

  private HttpHandler withHttpsRedirect(HttpHandler router, String httpsPort, int statusCode) {
    return Handlers.header(
        Handlers.predicate(
            secure(),
            router,
            exchange -> {
              String httpsUrl = "https://" + exchange.getHostName() + ":" + httpsPort + exchange.getRelativePath();
              exchange.getResponseHeaders().add(Headers.LOCATION, httpsUrl);
              exchange.setStatusCode(statusCode);
            }
        ), "x-undertow-transport", ExchangeAttributes.transportProtocol()
    );
  }

  public static class Builder {
    private final HttpHandler router;

    private String propertiesFilename = "app.properties";
    private boolean isHttps = false;
    private boolean redirectToHttps = false;
    private boolean enableHttp2 = false;
    private String keyStorePath = "";
    private int statusCode = 0;

    public Builder(HttpHandler router) {
      this.router = router;
    }

    public Builder propertiesFilename(String name) {
      if (nullOrEmpty(name))
        throw new RuntimeException("properties file cannot be null or empty.");

      this.propertiesFilename = name;
      return this;
    }

    public Builder enableHttps(String keyStorePath) {
      if (nullOrEmpty(keyStorePath))
        throw new RuntimeException("keyStorePath cannot be null or empty.");

      this.isHttps = true;
      this.keyStorePath = keyStorePath;
      return this;
    }

    public Builder redirectToHttps(int statusCode) {
      if (String.valueOf(statusCode).charAt(0) != '3')
        throw new RuntimeException("Status code must be 3XX.");

      this.redirectToHttps = true;
      this.statusCode = statusCode;
      return this;
    }

    public Builder enableHttp2() {
      this.enableHttp2 = true;
      return this;
    }

    public Server build() {
      return new Server(this);
    }

    private boolean nullOrEmpty(String value) {
      return Optional.ofNullable(value).orElse("").trim().isEmpty();
    }
  }
}
