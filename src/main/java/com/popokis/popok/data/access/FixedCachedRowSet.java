package com.popokis.popok.data.access;

import javax.sql.rowset.CachedRowSet;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class FixedCachedRowSet {

  private final CachedRowSet cachedRowSet;
  private final Map<String, Integer> columnLabelMap;

  public FixedCachedRowSet(CachedRowSet cachedRowSet) {
    this.cachedRowSet = cachedRowSet;

    try {
      ResultSetMetaData resultSetMetaData = cachedRowSet.getMetaData();

      if (Objects.nonNull(resultSetMetaData)) {
        int columnCount = resultSetMetaData.getColumnCount();
        this.columnLabelMap = new HashMap<>(columnCount);

        for (int i = 1; i <= columnCount; ++i) {
          String key = resultSetMetaData.getColumnLabel(i);
          if (!this.columnLabelMap.containsKey(key)) {
            this.columnLabelMap.put(key, i);
          }
        }
      } else {
        this.columnLabelMap = Collections.emptyMap();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public CachedRowSet getCachedRowSet() {
    return cachedRowSet;
  }

  public int findColumn(String columnLabel) {
    Integer columnIndex = this.columnLabelMap.get(columnLabel);

    if (Objects.nonNull(columnIndex)) {
      return columnIndex;
    } else {
      try {
        return this.cachedRowSet.findColumn(columnLabel);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public BigDecimal getBigDecimal(int columnIndex) {
    try {
      return this.cachedRowSet.getBigDecimal(columnIndex);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public BigDecimal getBigDecimal(String columnLabel) {
    return this.getBigDecimal(this.findColumn(columnLabel));
  }

  public boolean getBoolean(int columnIndex) {
    try {
      return this.cachedRowSet.getBoolean(columnIndex);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean getBoolean(String columnLabel) {
    return this.getBoolean(this.findColumn(columnLabel));
  }

  public byte getByte(int columnIndex) {
    try {
      return this.cachedRowSet.getByte(columnIndex);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public byte getByte(String columnLabel) {
    return this.getByte(this.findColumn(columnLabel));
  }

  public Date getDate(int columnIndex) {
    try {
      return this.cachedRowSet.getDate(columnIndex);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Date getDate(String columnLabel) {
    return this.getDate(this.findColumn(columnLabel));
  }

  public Date getDate(int columnIndex, Calendar cal) {
    try {
      return this.cachedRowSet.getDate(columnIndex, cal);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Date getDate(String columnLabel, Calendar cal) {
    return this.getDate(this.findColumn(columnLabel), cal);
  }

  public double getDouble(int columnIndex) {
    try {
      return this.cachedRowSet.getDouble(columnIndex);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public double getDouble(String columnLabel) {
    return this.getDouble(this.findColumn(columnLabel));
  }

  public float getFloat(int columnIndex) {
    try {
      return this.cachedRowSet.getFloat(columnIndex);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public float getFloat(String columnLabel) {
    return this.getFloat(this.findColumn(columnLabel));
  }

  public int getInt(int columnIndex) {
    try {
      return this.cachedRowSet.getInt(columnIndex);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public int getInt(String columnLabel) {
    return this.getInt(this.findColumn(columnLabel));
  }

  public long getLong(int columnIndex) {
    try {
      return this.cachedRowSet.getLong(columnIndex);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public long getLong(String columnLabel) {
    return this.getLong(this.findColumn(columnLabel));
  }

  public String getNString(int columnIndex) {
    try {
      return this.cachedRowSet.getNString(columnIndex);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public String getNString(String columnLabel) {
    return this.getNString(this.findColumn(columnLabel));
  }

  public Object getObject(int columnIndex) {
    try {
      return this.cachedRowSet.getObject(columnIndex);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Object getObject(String columnLabel) {
    return this.getObject(this.findColumn(columnLabel));
  }

  public Object getObject(int columnIndex, Map<String, Class<?>> map) {
    try {
      return this.cachedRowSet.getObject(columnIndex, map);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Object getObject(String columnLabel, Map<String, Class<?>> map) {
    return this.getObject(this.findColumn(columnLabel), map);
  }

  public <T> T getObject(int columnIndex, Class<T> type) {
    try {
      return this.cachedRowSet.getObject(columnIndex, type);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T getObject(String columnLabel, Class<T> type) {
    return this.getObject(this.findColumn(columnLabel), type);
  }

  public short getShort(int columnIndex) {
    try {
      return this.cachedRowSet.getShort(columnIndex);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public short getShort(String columnLabel) {
    return this.getShort(this.findColumn(columnLabel));
  }

  public String getString(int columnIndex) {
    try {
      return this.cachedRowSet.getString(columnIndex);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public String getString(String columnLabel) {
    return this.getString(this.findColumn(columnLabel));
  }

  public Time getTime(int columnIndex) {
    try {
      return this.cachedRowSet.getTime(columnIndex);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Time getTime(String columnLabel) {
    return this.getTime(this.findColumn(columnLabel));
  }

  public Time getTime(int columnIndex, Calendar cal) {
    try {
      return this.cachedRowSet.getTime(columnIndex, cal);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Time getTime(String columnLabel, Calendar cal) {
    return this.getTime(this.findColumn(columnLabel), cal);
  }

  public Timestamp getTimestamp(int columnIndex) {
    try {
      return this.cachedRowSet.getTimestamp(columnIndex);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Timestamp getTimestamp(String columnLabel) {
    return this.getTimestamp(this.findColumn(columnLabel));
  }

  public Timestamp getTimestamp(int columnIndex, Calendar cal) {
    try {
      return this.cachedRowSet.getTimestamp(columnIndex, cal);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Timestamp getTimestamp(String columnLabel, Calendar cal) {
    return this.getTimestamp(this.findColumn(columnLabel), cal);
  }

  public boolean absolute(int row) {
    try {
      return this.cachedRowSet.absolute(row);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void afterLast() {
    try {
      this.cachedRowSet.afterLast();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void beforeFirst() {
    try {
      this.cachedRowSet.beforeFirst();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean first() {
    try {
      return this.cachedRowSet.first();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public int getRow() {
    try {
      return this.cachedRowSet.getRow();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean isAfterLast() {
    try {
      return this.cachedRowSet.isAfterLast();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean isBeforeFirst() {
    try {
      return this.cachedRowSet.isBeforeFirst();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean isFirst() {
    try {
      return this.cachedRowSet.isFirst();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean isLast() {
    try {
      return this.cachedRowSet.isLast();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean last() {
    try {
      return this.cachedRowSet.last();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean next() {
    try {
      return this.cachedRowSet.next();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean previous() {
    try {
      return this.cachedRowSet.previous();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean relative(int rows) {
    try {
      return this.cachedRowSet.relative(rows);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean wasNull() {
    try {
      return this.cachedRowSet.wasNull();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
