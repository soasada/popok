package com.popokis.popok.http.make_request;

import com.popokis.popok.http.client.Client;

public final class PostRequest<S> implements MakeRequest<S> {

  private final Client<S> client;

  public PostRequest(Client<S> client) {
    this.client = client;
  }

  @Override
  public S to(String uri, String body) {
    return client.post(uri, body);
  }
}
