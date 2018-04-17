package com.popokis.popok.data;

import java.sql.PreparedStatement;

public interface Query {
  String query();
  void parameters(PreparedStatement stm);
}
