package com.popokis.popok.http.server;

import com.popokis.popok.http.router.Router;
import com.popokis.popok.http.router.RouterUtils;
import io.undertow.Undertow;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.Objects;

public final class SecureServer implements PopokHttpServer {

  private static final char[] STORE_PASSWORD = "password".toCharArray();

  private final int port;
  private final String host;
  private final Router router;
  private final Undertow server;

  public SecureServer(int port, String host, Router router, String keyStorePath, String trustStorePath) {
    this.port = port;
    this.host = host;
    this.router = router;

    SSLContext sslContext;
    try {
      sslContext = createSSLContext(loadKeyStore(keyStorePath), loadKeyStore(trustStorePath));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    server = Undertow.builder()
        .addHttpsListener(port, host, sslContext, RouterUtils.loadRoutes(router))
        .build();
  }

  private KeyStore loadKeyStore(String name) throws Exception {
    String storeLoc = System.getProperty(name);
    final InputStream stream;

    if (Objects.isNull(storeLoc)) {
      stream = SecureServer.class.getResourceAsStream(name);
    } else {
      stream = Files.newInputStream(Paths.get(storeLoc));
    }

    if (Objects.isNull(stream)) {
      throw new RuntimeException("Could not load keystore");
    }

    try (InputStream is = stream) {
      KeyStore loadedKeystore = KeyStore.getInstance("JKS");
      loadedKeystore.load(is, password(name));
      return loadedKeystore;
    }
  }

  private char[] password(String name) {
    String pw = System.getProperty(name + ".password");
    return Objects.nonNull(pw) ? pw.toCharArray() : STORE_PASSWORD;
  }

  private SSLContext createSSLContext(final KeyStore keyStore, final KeyStore trustStore) throws Exception {
    KeyManager[] keyManagers;
    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(keyStore, password("key"));
    keyManagers = keyManagerFactory.getKeyManagers();

    TrustManager[] trustManagers;
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(trustStore);
    trustManagers = trustManagerFactory.getTrustManagers();

    SSLContext sslContext;
    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagers, trustManagers, null);

    return sslContext;
  }

  @Override
  public void start() {
    server.start();
  }

  @Override
  public void stop() {
    server.stop();
  }

  @Override
  public String url() {
    return "https://" + host + ":" + port + router.version();
  }
}
