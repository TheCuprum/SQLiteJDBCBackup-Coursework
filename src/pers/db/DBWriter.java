package pers.db;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// import java.sql.Types;

import pers.db.info.ColumnInfo;
import pers.db.info.IndexInfo;
import pers.db.info.TableInfo;
import pers.main.Util;

public class DBWriter extends BaseDatabaseAccessor {
    BufferedWriter sqlWriter = null;

    PreparedStatement prepStmt = null;
    String[] prepSql = null;

    public DBWriter(String _dbName) {
        super(_dbName, false);
    }

    @Override
    public void close() {
        try {
            if (this.prepStmt != null)
                this.prepStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.close();
    }

    public void appendWriter(BufferedWriter writer) {
        if (writer != null)
            this.sqlWriter = writer;
    }

    public void resetWriter() {
        this.sqlWriter = null;
    }

    public void executeDirectly(String sql) {
        Statement stmt = null;
        try {
            stmt = this.con.createStatement();
            if (this.sqlWriter != null) {
                try {
                    this.sqlWriter.write(sql);
                    this.sqlWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null)
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    public void createTable(TableInfo tableInfo) {
        Statement stmt = null;
        try {
            stmt = this.con.createStatement();
            String deletionSQL = tableInfo.toSQLDeletionStatement();
            String creationSQL = tableInfo.toSQLCreationStatement();

            if (this.sqlWriter != null) {
                try {
                    this.sqlWriter.write(deletionSQL);
                    this.sqlWriter.newLine();
                    this.sqlWriter.write(creationSQL);
                    this.sqlWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            stmt.execute(deletionSQL);
            stmt.execute(creationSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null)
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    public void createIndex(IndexInfo indexInfo) {
        Statement stmt = null;
        try {
            stmt = this.con.createStatement();
            String creationSQL = indexInfo.toSQLCreationStatement();
            if (this.sqlWriter != null) {
                try {
                    this.sqlWriter.write(creationSQL);
                    this.sqlWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            stmt.execute(creationSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null)
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    public void createPrepStmt(String sql) {
        try {
            this.prepStmt = this.con.prepareStatement(sql);
            this.prepSql = sql.split("\\?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertData(Object[] data, String[] typeNames, int[] typeIDs) {
        try {
            if ((this.prepSql.length - 1) != typeNames.length)
                throw new IndexOutOfBoundsException("Length of prepStmt parameters and types must match.");
            StringBuilder builder = new StringBuilder(this.prepSql[0]);
            for (int i = 0; i < typeNames.length; i++) {
                builder.append(Util.convertToString(data[i]));
                builder.append(this.prepSql[i + 1]);
                this.prepStmt.setObject(i + 1, data[i]);
            }

            if (this.sqlWriter != null) {
                try {
                    this.sqlWriter.write(builder.toString());
                    this.sqlWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (this.prepStmt.execute())
                System.err.println("Unhandled result while executing: " + builder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fillData(SQLDataContainer container) {
        this.createPrepStmt(container.toSQLCreationStatement());
        ColumnInfo[] columns = container.getTableInfo().getColumnInfos();
        String[] typeNames = new String[columns.length];
        int[] typeIDs = new int[columns.length];
        for (int i = 0; i < columns.length; i++) {
            typeNames[i] = columns[i].getColumnType();
            typeIDs[i] = columns[i].getDataType();
        }
        ResultSet rs = container.getData();
        try {
            // rs.beforeFirst();
            int count = rs.getMetaData().getColumnCount();
            Object[] dataArr = new Object[count];
            while (rs.next()) {
                for (int i = 0; i < count; i++)
                    dataArr[i] = rs.getObject(i + 1);
                this.insertData(dataArr, typeNames, typeIDs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // No need to implement because org.sqlite.RS#getObject() already handles this.
    // public static void transferData(ResultSet data, PreparedStatement prepStmt,
    // StringBuilder builder, int columnNumber, String type){
    // // if(type == Types.)
    // //...
    // }
}
