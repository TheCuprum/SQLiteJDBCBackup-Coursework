package pers.db;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Function;

import pers.main.Util;

/**
 * Contains code blocks for testing.
 */
public class DBMetaAccessor extends BaseDatabaseAccessor {

    public DBMetaAccessor(String _dbName) {
        super(_dbName);
    }

    public void showMeta(Function<DatabaseMetaData, ResultSet> f) {
        try {
            DatabaseMetaData meta = this.con.getMetaData();
            ResultSet rs = f.apply(meta);
            Util.printResultSet(rs, 15);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Function<DatabaseMetaData, ResultSet> listTables() {
        return meta -> {
            try {
                return meta.getTables(null, null, "", new String[] { "TABLE" });
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    public Function<DatabaseMetaData, ResultSet> listIndexes(String tableName) {
        return meta -> {
            try {
                return meta.getIndexInfo(null, null, tableName, false, false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    public Function<DatabaseMetaData, ResultSet> listAllTables() {
        return meta -> {
            try {
                return meta.getTables(null, null, "", null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    public Function<DatabaseMetaData, ResultSet> listPrimaryKeys(String table) {
        return meta -> {
            try {
                return meta.getPrimaryKeys(null, null, table);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    public Function<DatabaseMetaData, ResultSet> listColumns(String tableName) {
        return meta -> {
            try {
                return meta.getColumns(null, "%", tableName, "%");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    public Function<DatabaseMetaData, ResultSet> listRef(String parentTableName, String foreignTableName) {
        return meta -> {
            try {
                return meta.getCrossReference(null, "%", parentTableName, null, "%", foreignTableName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    @Deprecated // has implement issue in sqlite-jdbc-3.7.2
                // (https://github.com/xerial/sqlite-jdbc/issues/278).
    public Function<DatabaseMetaData, ResultSet> listExportedKeys(String tableName) {
        return meta -> {
            try {
                return meta.getExportedKeys(null, "%", tableName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    public Function<DatabaseMetaData, ResultSet> listImportedKeys(String tableName) {
        return meta -> {
            try {
                return meta.getImportedKeys(null, "%", tableName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    public void getTableNames() {
        try {
            DatabaseMetaData meta = this.con.getMetaData();
            ResultSet tableSet = meta.getTables(null, "%", "", new String[] { "TABLE" });

            // ResultSetMetaData tableMeta = tableSet.getMetaData();
            // tableMeta.getColumnCount();
            ArrayList<String> tableNameList = new ArrayList<String>();
            while (tableSet.next()) {
                tableNameList.add(tableSet.getString(3));
            }
            Util.printList(tableNameList.toArray(), true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // public void testPrepStmt() {
    //     PreparedStatement pstmt = null;
    //     try {
    //         pstmt = this.con.prepareStatement("SELECT * FROM staff WHERE d_id= ?");
    //         pstmt.setString(1, "ASFD");
    //         System.out.println(pstmt.toString());
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     } finally {
    //         try {
    //             pstmt.close();
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }
}
