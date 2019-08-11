package com.popokis.popok.http;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;

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
import java.util.Objects;
import java.util.Properties;

public final class Server {

  private final Undertow server;

  private char[] keyStorePassword;

  private Server(Builder builder) {
    try (InputStream fi = Server.class.getResourceAsStream(File.separator + builder.propertiesFilename)) {
      Properties appProps = new Properties();
      appProps.load(fi);

      String httpPort = appProps.getProperty("server.http.port");
      if (Objects.isNull(httpPort)) throw new RuntimeException("server.http.port property not found.");
      String address = appProps.getProperty("server.address");
      if (Objects.isNull(address)) throw new RuntimeException("server.address property not found.");

      if (builder.isHttps) {
        String httpsPort = appProps.getProperty("server.https.port");
        if (Objects.isNull(httpsPort)) throw new RuntimeException("server.https.port property not found.");
        String keyStorePassword = appProps.getProperty("security.key.store.password");
        if (Objects.isNull(keyStorePassword)) throw new RuntimeException("security.key.store.password property not found.");
        this.keyStorePassword = keyStorePassword.toCharArray();

        this.server = Undertow.builder()
            .setServerOption(UndertowOptions.ENABLE_HTTP2, true)
            .addHttpListener(Integer.parseInt(httpPort), address)
            .addHttpsListener(Integer.parseInt(httpsPort), address, createSSLContext(loadKeyStore(builder.keyStorePath)))
            .setHandler(builder.router)
            .build();
      } else {
        this.server = Undertow.builder()
            .addHttpListener(Integer.parseInt(httpPort), address)
            .setHandler(builder.router)
            .build();
      }
    } catch (IOException e) {
      throw new RuntimeException(File.separator + "app.properties not found, please create it inside resources folder.");
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

  public static class Builder {
    private final HttpHandler router;

    private String propertiesFilename = "app.properties";
    private boolean isHttps = false;
    private String keyStorePath = "";

    public Builder(HttpHandler router) {
      this.router = router;
    }

    public Builder propertiesFilename(String name) {
      this.propertiesFilename = name;
      return this;
    }

    public Builder enableHttps() {
      this.isHttps = true;
      return this;
    }

    public Builder keyStorePath(String keyStorePath) {
      this.keyStorePath = keyStorePath;
      return this;
    }

    public Server build() {
      return new Server(this);
    }
  }
}
