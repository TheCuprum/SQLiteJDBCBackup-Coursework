package pers.db.info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import pers.db.SQLStatementInterface;

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
    // boolean isCreated = false;

    public TableInfo(String tableName) {
        this.tableName = tableName;
    }

    public void addCloumn(ColumnInfo column) {
        this.columns.add(column);
    }

    public void addCloumnBatch(Collection<ColumnInfo> columnBatch) {
        this.columns.addAll(columnBatch);
    }

    public void addIndex(IndexInfo index) {
        this.indexes.add(index);
    }

    public void addIndexBatch(Collection<IndexInfo> indexBatch) {
        this.indexes.addAll(indexBatch);
    }

    public void addPrimaryKey(String key) {
        this.primaryKey.add(key);
    }

    public void addPrimaryKey(Collection<String> keyBatch) {
        this.primaryKey.addAll(keyBatch);
    }

    public void addForeignKey(String key, TableInfo foreignTable, String foreignColumn) {
        this.foreignKeyPairs.add(new ForeignKeyPair(key, foreignColumn, foreignTable.getTableName()));
        if (this.importedTables.put(foreignTable, Boolean.FALSE) != null)
            System.err.println("Key ".concat(foreignTable.getTableName())
                    .concat(" already exists when creating TableInfo ".concat(this.tableName)));
        foreignTable.addExportedKey(foreignColumn, this, key);
        // ForeignKeyPair keyPair
    }

    public void addExportedKey(String key, TableInfo foreignTable, String foreignColumn){
        this.exportedTables.put(foreignTable, Boolean.FALSE);
    }

    public String getTableName() {
        return this.tableName;
    }

    public ColumnInfo[] getColumnInfos() {
        ColumnInfo[] infos = new ColumnInfo[this.columns.size()];
        return this.columns.toArray(infos);
    }

    public IndexInfo[] getIndexInfos() {
        IndexInfo[] infos = new IndexInfo[this.indexes.size()];
        return this.indexes.toArray(infos);
    }

    public String[] getPrimaryKeys() {
        String[] strArr = new String[this.primaryKey.size()];
        return this.primaryKey.toArray(strArr);
    }

    public ForeignKeyPair[] getForeignKeyPairs() {
        ForeignKeyPair[] pairs = new ForeignKeyPair[this.foreignKeyPairs.size()];
        return this.foreignKeyPairs.toArray(pairs);
    }

    public void setCreated() {
        // if(!this.isCreated){
        for (TableInfo table : this.exportedTables.keySet()) {
            table.markCreatedTable(this);
            this.exportedTables.put(table, Boolean.TRUE);
        }
        // this.isCreated = true;
        // }
    }

    // public boolean created(){
    // return this.isCreated;
    // }

    public boolean canBeCreated() {
        // if (this.isCreated)
        // return false;
        boolean can = true;
        for (Boolean flag : this.importedTables.values())
            can = can && flag;
        return can;
    }

    public void markCreatedTable(TableInfo tableInfo) {
        this.importedTables.put(tableInfo, Boolean.TRUE);
    }

    @Override
    public String toSQLCreationStatement() {
        StringBuilder builder = new StringBuilder("CREATE TABLE \"");
        builder.append(this.tableName)
                .append("\" (\n");

        boolean isFirstRow = true;
        if (this.columns.size() > 0) {
            for (int i = 0; i < this.columns.size(); i++) {
                if (!isFirstRow)
                    builder.append(",\n");
                else
                    isFirstRow = false;
                builder.append(this.columns.get(i).toString());
            }
        } else
            throw new NullPointerException();

        if (this.primaryKey.size() > 0) {
            if (!isFirstRow)
                builder.append(",\n");
            else
                isFirstRow = false;

            boolean isFirstKey = true;
            builder.append("PRIMARY KEY (");
            for (String key : this.primaryKey) {
                if (!isFirstKey)
                    builder.append(",");
                else
                    isFirstKey = false;
                builder.append(key);
            }
            builder.append(")");
        }

        if (this.foreignKeyPairs.size() > 0) {
            for (ForeignKeyPair keyPair : this.foreignKeyPairs) {
                if (!isFirstRow)
                    builder.append(",\n");
                else
                    isFirstRow = false;
                builder.append(keyPair.toString());
            }
        }

        builder.append(");");
        return builder.toString();
    }

    @Override
    public String toSQLDeletionStatement() {
        StringBuilder builder = new StringBuilder("DROP TABLE IF EXISTS ");
        builder.append('\"').append(this.tableName).append('\"').append(';');
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
