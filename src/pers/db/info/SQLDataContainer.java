package pers.db.info;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import pers.db.SQLStatementInterface;

public class SQLDataContainer implements SQLStatementInterface {
    TableInfo tableInfo;
    ResultSet data;

    public SQLDataContainer(TableInfo tableInfo, ResultSet data) {
        this.tableInfo = tableInfo;
        this.data = data;
    }

    public TableInfo getTableInfo() {
        return this.tableInfo;
    }

    public ResultSet getData() {
        return this.data;
    }

    public void close() {
        try {
            Statement stmt = this.data.getStatement();
            this.data.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toSQLCreationStatement() {
        StringBuilder builder = new StringBuilder("INSERT OR IGNORE INTO ");
        builder.append("\"").append(this.tableInfo.getTableName()).append("\"");
        if (tableInfo.getColumnInfos().length > 0) {
            builder.append(" VALUES(");
            boolean isFirstKey = true;
            for (ColumnInfo _col : this.tableInfo.getColumnInfos()) {
                if (!isFirstKey)
                    builder.append(',');
                else
                    isFirstKey = false;
                builder.append('?');
            }
            builder.append(");");
            return builder.toString();
        } else
            return null;
    }

    @Override
    public String toSQLDeletionStatement() {
        return null;
    }
}
