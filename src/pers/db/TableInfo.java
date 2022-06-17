package pers.db;

import java.util.ArrayList;
import java.util.HashMap;

public class TableInfo implements SQLStatementInterface {
    String tableName;
    ArrayList<ColumnInfo> columns = new ArrayList<ColumnInfo>();
    ArrayList<IndexInfo> indexes = new ArrayList<IndexInfo>();
    ArrayList<String> primaryKey = new ArrayList<String>();

    ArrayList<ForeignKeyPair> foreignKeyPairs = new ArrayList<ForeignKeyPair>();
    HashMap<TableInfo, Boolean> exportedTables = new HashMap<TableInfo, Boolean>();
    HashMap<TableInfo, Boolean> importedTables = new HashMap<TableInfo, Boolean>(); 
    // imported table inited -> true; 
    // exported table (target) inited -> true;

    // boolean strict; // strict type
    boolean isCreated = false;

    public TableInfo(String tableName) {
        this.tableName = tableName;
    }

    public void addCloumn(ColumnInfo column) {

    }

    public void addIndex(IndexInfo index) {

    }

    public void addPrimaryKey(String key) {

    }

    public void addForeignKey(String key, TableInfo foreignTable, String foreignColumn) {
        // ForeignKeyPair keyPair
    }

    public ColumnInfo[] getColumnInfos(){
        return (ColumnInfo[])this.columns.toArray();
    }

    public IndexInfo[] getIndexInfos(){
        return (IndexInfo[])this.indexes.toArray();
    }

    public String[] getPrimaryKeys(){
        return (String[])this.primaryKey.toArray();
    }

    public ForeignKeyPair[] getForeignKeyPairs(){
        return (ForeignKeyPair[])this.foreignKeyPairs.toArray();
    }

    public void setCreated(){
        if(!this.isCreated){
            for(TableInfo table : this.exportedTables.keySet()){
                table.markCreatedTable(this);
                this.exportedTables.put(table, Boolean.TRUE);
            }
            this.isCreated = true;
        }
    }

    public boolean created(){
        return this.isCreated;
    }

    public boolean canBeCreated() {
        if (this.isCreated)
            return false;
        boolean can = true;
        for (Boolean flag : this.importedTables.values())
            can = can && flag;
        return can;
    }

    public void markCreatedTable(TableInfo tableInfo){
        this.importedTables.put(tableInfo, Boolean.TRUE);
    }

    @Override
    public String toSQLStatement() {
        StringBuilder builder = new StringBuilder("CREATE TABLE ");
        builder.append(this.tableName)
                .append("(\n");

        boolean isFirstRow = true;
        if (this.columns.size() > 0) {
            for (int i = 0; i < this.columns.size(); i++) {
                if (!isFirstRow) {
                    builder.append(",\n");
                    isFirstRow = false;
                }
                builder.append(this.columns.get(i).toString());
            }
        } else
            throw new NullPointerException();

        if (this.primaryKey.size() > 0) {
            if (!isFirstRow) {
                builder.append(",\n");
                isFirstRow = false;
            }
            boolean isFirstKey = true;
            builder.append("PRIMARY KEY (");
            for (String key : this.primaryKey) {
                if (!isFirstKey) {
                    builder.append(",");
                    isFirstKey = false;
                }
                builder.append(key);
            }
            builder.append(")");
        } else
            throw new NullPointerException();

        if (this.foreignKeyPairs.size() > 0) {
            for (ForeignKeyPair keyPair : this.foreignKeyPairs) {
                if (!isFirstRow) {
                    builder.append(",\n");
                    isFirstRow = false;
                }
                builder.append(keyPair.toString());
            }
        } else
            throw new NullPointerException();

        builder.append(");");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        return this.tableName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TableInfo)) {
            return false;
        } else {
            return this.hashCode() == obj.hashCode();
        }
    }
}
