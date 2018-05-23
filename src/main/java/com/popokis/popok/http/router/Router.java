package com.popokis.popok.http.router;

import java.util.List;

public interface Router {
  List<Route> routes();
  String version();
}
