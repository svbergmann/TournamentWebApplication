package com.github.profschmergmann.tournamentwebapplication.database;

import java.sql.Types;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.type.StringType;

public class SqLiteDialect extends Dialect {

  public SqLiteDialect() {
    this.registerColumnType(Types.BIT, "integer");
    this.registerColumnType(Types.TINYINT, "tinyint");
    this.registerColumnType(Types.SMALLINT, "smallint");
    this.registerColumnType(Types.INTEGER, "integer");
    this.registerColumnType(Types.BIGINT, "bigint");
    this.registerColumnType(Types.FLOAT, "float");
    this.registerColumnType(Types.REAL, "real");
    this.registerColumnType(Types.DOUBLE, "double");
    this.registerColumnType(Types.NUMERIC, "numeric");
    this.registerColumnType(Types.DECIMAL, "decimal");
    this.registerColumnType(Types.CHAR, "char");
    this.registerColumnType(Types.VARCHAR, "varchar");
    this.registerColumnType(Types.LONGVARCHAR, "longvarchar");
    this.registerColumnType(Types.DATE, "date");
    this.registerColumnType(Types.TIME, "time");
    this.registerColumnType(Types.TIMESTAMP, "timestamp");
    this.registerColumnType(Types.BINARY, "blob");
    this.registerColumnType(Types.VARBINARY, "blob");
    this.registerColumnType(Types.LONGVARBINARY, "blob");
    // registerColumnType(Types.NULL, "null");
    this.registerColumnType(Types.BLOB, "blob");
    this.registerColumnType(Types.CLOB, "clob");
    this.registerColumnType(Types.BOOLEAN, "integer");

    this.registerFunction("concat", new VarArgsSQLFunction(StringType.INSTANCE, "", "||", ""));
    this.registerFunction("mod", new SQLFunctionTemplate(StringType.INSTANCE, "?1 % ?2"));
    this.registerFunction("substr", new StandardSQLFunction("substr", StringType.INSTANCE));
    this.registerFunction("substring", new StandardSQLFunction("substr", StringType.INSTANCE));
  }

  public boolean dropTemporaryTableAfterUse() {
    return false;
  }

  public String getCreateTemporaryTableString() {
    return "create temporary table if not exists";
  }

  public String getForUpdateString() {
    return "";
  }

  public boolean supportsOuterJoinForUpdate() {
    return false;
  }

  public boolean supportsCurrentTimestampSelection() {
    return true;
  }

  public boolean isCurrentTimestampSelectStringCallable() {
    return false;
  }

  public String getCurrentTimestampSelectString() {
    return "select current_timestamp";
  }

  public boolean supportsUnionAll() {
    return true;
  }

  public boolean hasAlterTable() {
    return false; // As specify in NHibernate dialect
  }

  public boolean dropConstraints() {
    return false;
  }

  public String getAddColumnString() {
    return "add column";
  }

  public String getDropForeignKeyString() {
    throw new UnsupportedOperationException(
        "No drop foreign key syntax supported by SQLiteDialect");
  }

  public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey,
      String referencedTable, String[] primaryKey, boolean referencesPrimaryKey) {
    throw new UnsupportedOperationException("No add foreign key syntax supported by SQLiteDialect");
  }

  public String getAddPrimaryKeyConstraintString(String constraintName) {
    throw new UnsupportedOperationException("No add primary key syntax supported by SQLiteDialect");
  }

  public boolean supportsIfExistsBeforeTableName() {
    return true;
  }

  public boolean supportsCascadeDelete() {
    return false;
  }

  public String getIdentityColumnString() {
    // return "integer primary key autoincrement";
    return "integer";
  }

  public String getIdentitySelectString() {
    return "select last_insert_rowid()";
  }

  public boolean hasDataTypeInIdentityColumn() {
    return false;
  }

  public boolean supportsIdentityColumns() {
    return true;
  }

  public boolean supportsTemporaryTables() {
    return true;
  }
}
