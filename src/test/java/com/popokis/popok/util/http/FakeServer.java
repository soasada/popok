package com.popokis.popok.util.http;

import com.popokis.popok.http.extractor.GetExtractor;
import com.popokis.popok.http.extractor.PostExtractor;
import com.popokis.popok.http.router.Route;
import com.popokis.popok.http.server.SimpleServer;
import io.undertow.util.Methods;

import java.util.List;

public final class FakeServer {

  private static final String API_VERSION = "/api/v1";

  private static final String FAKE_GET = API_VERSION + "/fake/get";
  private static final String FAKE_POST = API_VERSION + "/fake/post";

  private static final String COMPANY = API_VERSION + "/company";
  private static final String EMPLOYEE = API_VERSION + "/employee";

  private static final String CREATE = "/create";
  private static final String UPDATE = "/update";
  private static final String DELETE = "/delete";
  private static final String ALL = "/all";

  private final SimpleServer server;

  public FakeServer() {
    List<Route> routes = List.of(
        Route.of(Methods.GET, FAKE_GET, new FakeTextHandler(new GetExtractor())),
        Route.of(Methods.POST, FAKE_POST, new FakeTextHandler(new PostExtractor())),

        Route.of(Methods.POST, COMPANY + CREATE, CompanyHandlerFactory.crudCompany().create()),
        Route.of(Methods.POST, COMPANY + UPDATE, CompanyHandlerFactory.crudCompany().update()),
        Route.of(Methods.GET, COMPANY + "/{id}" + DELETE, CompanyHandlerFactory.crudCompany().remove()),
        Route.of(Methods.GET, COMPANY + "/{id}", CompanyHandlerFactory.crudCompany().search()),
        Route.of(Methods.GET, COMPANY + ALL, CompanyHandlerFactory.crudCompany().all()),

        Route.of(Methods.POST, EMPLOYEE + CREATE, CompanyHandlerFactory.crudEmployee().create()),
        Route.of(Methods.POST, EMPLOYEE + UPDATE, CompanyHandlerFactory.crudEmployee().update()),
        Route.of(Methods.GET, EMPLOYEE + "/{id}" + DELETE, CompanyHandlerFactory.crudEmployee().remove()),
        Route.of(Methods.GET, EMPLOYEE + "/{id}", CompanyHandlerFactory.crudEmployee().search()),
        Route.of(Methods.GET, EMPLOYEE + ALL, CompanyHandlerFactory.crudEmployee().all())
    );

    server = new SimpleServer(8080, "0.0.0.0", routes);
  }

  public void stop() {
    server.stop();
  }

  public String url() {
    return server.url() + API_VERSION;
  }
}
