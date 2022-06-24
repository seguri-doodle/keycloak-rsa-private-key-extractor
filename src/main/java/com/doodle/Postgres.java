package com.doodle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Postgres {
  private static final String USERNAME = "keycloak_iam";
  private static final String PASSWORD = "changeme";
  private static final String TABLE = "keycloak_iam";
  private static final String QUERY =
      "SELECT value "
          + "FROM component_config cfg "
          + "JOIN component c ON cfg.component_id = c.id "
          + "WHERE c.realm_id = 'doodle' "
          + "AND c.provider_id = 'rsa-generated' "
          + "AND cfg.name = 'privateKey'";

  public static String fetchSecret() throws ClassNotFoundException, SQLException {
    return fetchFirstSingleResult(QUERY);
  }

  private static String fetchFirstSingleResult(String query)
      throws ClassNotFoundException, SQLException {
    Class.forName("org.postgresql.Driver");
    var conn = getConnection();
    try (var stmt = conn.createStatement()) {
      var rs = stmt.executeQuery(query);
      return rs.next() ? rs.getString(1) : null;
    }
  }

  private static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/" + TABLE, USERNAME, PASSWORD);
  }
}
