package pers.db;

import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import pers.main.Util;

public class DBReader extends BaseDatabaseAccessor {

    private DatabaseMetaData dbMetaData;

    public DBReader(String _dbName) {
        super(_dbName);
        this.refreshInstance();
    }

    public void refreshInstance() {
        try {
            this.dbMetaData = super.con.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getTableList() {
        try {
            ResultSet tableSet = this.dbMetaData.getTables(null, "%", "", new String[] { "TABLE" });
            ArrayList<String> tableNameList = new ArrayList<String>();
            while (tableSet.next()) {
                // tableNameList.add(tableSet.getString(3));
                tableNameList.add(tableSet.getString("TABLE_NAME"));
            }
            return tableNameList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<IndexInfo> getIndexInfoList(String tableName) {
        try {
            ResultSet tableSet = this.dbMetaData.getIndexInfo(null, "%", tableName, false, false);
            ArrayList<String> tableNameList = new ArrayList<String>();
            while (tableSet.next()) {
                tableNameList.add(tableSet.getString(3));
            }
            return tableNameList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DatabaseInfo getDatabaseInfo(){
        return null;
    }

    public TableInfo getTableInfo(){
        return null;
    }

    public 

}
