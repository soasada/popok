package com.popokis.popok.service.http;

import com.popokis.popok.http.make_request.MakeRequest;
import com.popokis.popok.serialization.Serializator;
import com.popokis.popok.service.Service;

public final class HttpService<R, S> implements Service<R, S> {

  private final MakeRequest<S> makeRequest;
  private final Serializator<String, R> requestSerializator;
  private final String endpoint;

  public HttpService(MakeRequest<S> makeRequest, Serializator<String, R> requestSerializator, String endpoint) {
    this.makeRequest = makeRequest;
    this.requestSerializator = requestSerializator;
    this.endpoint = endpoint;
  }

  @Override
  public S call(R request) {
    String serializedRequest = requestSerializator.serialize(request);
    return makeRequest.to(endpoint, serializedRequest);
  }
}
