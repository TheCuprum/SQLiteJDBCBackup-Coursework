package pers.db;

// import java.sql.Types;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import pers.db.info.ColumnInfo;
import pers.db.info.DatabaseInfo;
import pers.db.info.ForeignKeyPair;
import pers.db.info.IndexInfo;
import pers.db.info.TableInfo;
import pers.main.Config;

public class DBReader extends BaseDatabaseAccessor {

    private DatabaseMetaData dbMetaData;

    public DBReader(String _dbName) {
        super(_dbName, true);
        this.refreshInstance();
    }

    public void refreshInstance() {
        try {
            this.dbMetaData = super.con.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DatabaseInfo getDatabaseInfo() {
        ArrayList<String> tableList = this.getTableList();
        DatabaseInfo databaseInfo = new DatabaseInfo();
        for (String tableName : tableList) {
            databaseInfo.addTable(this.getBasicTableInfo(tableName));
            if (Config.DEBUG)
                System.out.println("Scanned: " + tableName);
        }
        for (String tableName : tableList) {
            TableInfo table = databaseInfo.getTableInfo(tableName);
            ArrayList<ForeignKeyPair> fkList = this.getForeignKeys(tableName);
            for (ForeignKeyPair foreignKey : fkList) {
                table.addForeignKey(
                        foreignKey.getLocalColmun(),
                        databaseInfo.getTableInfo(foreignKey.getForeignTable()),
                        foreignKey.getForeignColumn());
                if (Config.DEBUG) {
                    System.out.println("Found foreign key pair: " + foreignKey.toString());
                    System.out.println(
                            "Bind table: " + databaseInfo.getTableInfo(foreignKey.getForeignTable()).getTableName());
                }
            }
        }
        return databaseInfo;
    }

    public TableInfo getBasicTableInfo(String tableName) {
        TableInfo tableInfo = new TableInfo(tableName);
        tableInfo.addCloumnBatch(this.getColumns(tableName));
        tableInfo.addIndexBatch(this.getIndexInfo(tableName));
        tableInfo.addPrimaryKey(this.getPrimaryKeys(tableName));
        return tableInfo;
    }

    public ArrayList<String> getTableList() {
        ResultSet tableSet = null;
        try {
            tableSet = this.dbMetaData.getTables(null, "%", "%", new String[] { "TABLE" });
            ArrayList<String> tableNameList = new ArrayList<String>();
            while (tableSet.next()) {
                // tableNameList.add(tableSet.getString(3));
                tableNameList.add(tableSet.getString("TABLE_NAME"));
            }
            return tableNameList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (tableSet != null)
                try {
                    tableSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    // public ArrayList<IndexInfo> getIndexInfoList(String[] tables) {
    // ArrayList<IndexInfo> indexList = new ArrayList<IndexInfo>();
    // for (String t : tables) {
    // indexList.addAll(this.getIndexInfo(t));
    // }
    // return indexList;
    // }

    public ArrayList<IndexInfo> getIndexInfo(String tableName) {
        ResultSet indexSet = null;
        try {
            indexSet = this.dbMetaData.getIndexInfo(null, "%", tableName, false, false);
            HashMap<String, IndexInfo> indexMap = new HashMap<String, IndexInfo>();
            while (indexSet.next()) {
                String indexName = indexSet.getString("INDEX_NAME");
                if (!indexName.startsWith("sqlite_autoindex_")) {
                    if (!indexMap.containsKey(indexName))
                        indexMap.put(indexName, new IndexInfo(
                            indexSet.getString("TABLE_NAME"),
                            indexSet.getString("INDEX_NAME"),
                            indexSet.getBoolean("NON_UNIQUE")));
                    indexMap.get(indexName).addColumn(indexSet.getString("COLUMN_NAME"), true);
                }
            }
            ArrayList<IndexInfo> indexList = new ArrayList<IndexInfo>();
            indexList.addAll(indexMap.values());
            return indexList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (indexSet != null)
                try {
                    indexSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    public ArrayList<ColumnInfo> getColumns(String tableName) {
        ArrayList<ColumnInfo> columnList = null;
        ResultSet columnSet = null;
        try {
            columnList = new ArrayList<ColumnInfo>();
            columnSet = this.dbMetaData.getColumns(null, "%", tableName, "%");
            while (columnSet.next()) {
                columnList.add(
                        new ColumnInfo(columnSet.getString("COLUMN_NAME"),
                                columnSet.getInt("DATA_TYPE"),
                                columnSet.getString("TYPE_NAME"),
                                columnSet.getInt("NULLABLE")));
                // columnSet.getBoolean("IS_AUTOINCREMENT")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (columnSet != null)
                try {
                    columnSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return columnList;
    }

    public ArrayList<String> getPrimaryKeys(String tableName) {
        ArrayList<String> primaryKeys = null;
        ResultSet keySet = null;
        try {
            primaryKeys = new ArrayList<String>();
            keySet = this.dbMetaData.getPrimaryKeys(null, "%", tableName);
            while (keySet.next()) {
                primaryKeys.add(keySet.getString("COLUMN_NAME"));
                // primaryKeys.add(keySet.getString("PK_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (keySet != null)
                try {
                    keySet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return primaryKeys;
    }

    public ArrayList<ForeignKeyPair> getForeignKeys(String tableName) {
        ArrayList<ForeignKeyPair> foreignKeys = null;
        ResultSet keySet = null;
        try {
            foreignKeys = new ArrayList<ForeignKeyPair>();
            keySet = this.dbMetaData.getImportedKeys(null, "%", tableName);
            while (keySet.next()) {
                foreignKeys.add(
                        new ForeignKeyPair(
                                keySet.getString("FKCOLUMN_NAME"),
                                keySet.getString("PKCOLUMN_NAME"),
                                keySet.getString("PKTABLE_NAME")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (keySet != null)
                try {
                    keySet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return foreignKeys;
    }

    public ResultSet getTableContent(String tableName) {
        Statement stmt = null;
        ResultSet contentSet = null;
        try {
            stmt = this.con.createStatement();
            contentSet = stmt.executeQuery("SELECT * FROM \"".concat(tableName).concat("\";"));
            // stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return contentSet;
    }

}
