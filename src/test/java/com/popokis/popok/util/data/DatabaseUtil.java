package com.popokis.popok.util.data;

import com.popokis.popok.data.Database;
import com.popokis.popok.util.query.schema.CreateCompanyTableQuery;
import com.popokis.popok.util.query.schema.CreateEmployeeTableQuery;
import com.popokis.popok.util.query.schema.CreateTestTableQuery;
import com.popokis.popok.util.query.schema.DropCompanyTableQuery;
import com.popokis.popok.util.query.schema.DropEmployeeTableQuery;
import com.popokis.popok.util.query.schema.DropTestTableQuery;

public final class DatabaseUtil {

  private DatabaseUtil() {}

  public static void createTestSchema() {
    Database.getInstance().executeDML(new CreateTestTableQuery());
    Database.getInstance().executeDML(new CreateCompanyTableQuery());
    Database.getInstance().executeDML(new CreateEmployeeTableQuery());
  }

  public static void dropTestSchema() {
    Database.getInstance().executeDML(new DropTestTableQuery());
    Database.getInstance().executeDML(new DropEmployeeTableQuery());
    Database.getInstance().executeDML(new DropCompanyTableQuery());
  }
}
