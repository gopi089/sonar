/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2013 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.sonar.server.db.migrations;

import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.MessageException;
import org.sonar.core.persistence.Database;
import org.sonar.core.persistence.dialect.MySql;

import java.sql.*;

/**
 * Update a table by iterating a sub-set of rows. For each row a SQL UPDATE request
 * is executed.
 */
public class MassUpdater {

  private static final Logger LOGGER = LoggerFactory.getLogger(MassUpdater.class);
  private static final String FAILURE_MESSAGE = "Fail to migrate data";
  private static final int GROUP_SIZE = 1000;
  private final Database db;

  public MassUpdater(Database db) {
    this.db = db;
  }

  public static interface InputLoader<S> {
    String selectSql();

    S load(ResultSet rs) throws SQLException;
  }

  public static interface InputConverter<S> {
    String updateSql();

    void convert(S input, PreparedStatement updateStatement) throws SQLException;
  }

  public <S> void execute(InputLoader<S> inputLoader, InputConverter<S> converter) {
    long count = 0;
    try {
      Connection readConnection = db.getDataSource().getConnection();
      Statement stmt = null;
      ResultSet rs = null;
      Connection writeConnection = db.getDataSource().getConnection();
      PreparedStatement writeStatement = null;
      try {
        readConnection.setAutoCommit(false);
        writeConnection.setAutoCommit(false);
        writeStatement = writeConnection.prepareStatement(converter.updateSql());
        stmt = readConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(GROUP_SIZE);
        if (db.getDialect().getId().equals(MySql.ID)) {
          stmt.setFetchSize(Integer.MIN_VALUE);
        } else {
          stmt.setFetchSize(GROUP_SIZE);
        }
        rs = stmt.executeQuery(inputLoader.selectSql());

        int cursor = 0;
        while (rs.next()) {
          converter.convert(inputLoader.load(rs), writeStatement);
          writeStatement.addBatch();

          cursor++;
          count++;
          if (cursor == GROUP_SIZE) {
            writeStatement.executeBatch();
            writeConnection.commit();
            cursor = 0;
          }
        }
        if (cursor > 0) {
          writeStatement.executeBatch();
          writeConnection.commit();
        }
      } finally {
        DbUtils.closeQuietly(writeStatement);
        DbUtils.closeQuietly(writeConnection);
        DbUtils.closeQuietly(readConnection, stmt, rs);

        LOGGER.info("{} rows have been updated", count);
      }
    } catch (SQLException e) {
      LOGGER.error(FAILURE_MESSAGE, e);
      SqlUtil.log(LOGGER, e);
      throw MessageException.of(FAILURE_MESSAGE);

    } catch (Exception e) {
      LOGGER.error(FAILURE_MESSAGE, e);
      throw MessageException.of(FAILURE_MESSAGE);
    }
  }

}
