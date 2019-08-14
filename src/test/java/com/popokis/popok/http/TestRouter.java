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
import io.undertow.server.HttpHandler;
import io.undertow.util.StatusCodes;

public final class TestRouter {

  private TestRouter() {}

  public static HttpHandler of() {
    return Handlers.path()
        .addPrefixPath("/api/v1", Handlers.routing()
            .get("/hello", (exchange) -> {
              exchange.setStatusCode(StatusCodes.OK);
              exchange.getResponseSender().send("Hello World!");
            })
        );
  }
}
