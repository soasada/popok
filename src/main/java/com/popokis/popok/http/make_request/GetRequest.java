package com.popokis.popok.http.make_request;

import com.popokis.popok.http.client.Client;

public final class GetRequest<S> implements MakeRequest<S> {

  private final Client<S> client;

  public GetRequest(Client<S> client) {
    this.client = client;
  }

  @Override
  public S to(String uri, String queryString) {
    return client.get(uri + "?" + queryString);
  }
}
