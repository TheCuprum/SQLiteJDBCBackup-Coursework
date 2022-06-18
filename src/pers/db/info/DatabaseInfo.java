package pers.db.info;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseInfo {
    HashMap<String, TableInfo> tableMap = new HashMap<String, TableInfo>();
    HashMap<String, Boolean> created = new HashMap<String, Boolean>();

    ArrayList<TableInfo> creationOrder = new ArrayList<TableInfo>();

    public DatabaseInfo() {

    }

    public void addTable(TableInfo info) {
        this.addTable(info.getTableName(), info);
    }

    public void addTable(String tableName, TableInfo info) {
        this.tableMap.put(tableName, info);
        this.created.put(tableName, Boolean.FALSE);
    }

    public void setCreated(TableInfo info) {
        this.setCreated(info.getTableName());
    }

    public void setCreated(String table) {
        this.created.replace(table, Boolean.TRUE);
        this.tableMap.get(table).setCreated();
        this.creationOrder.add(this.tableMap.get(table));
    }

    public TableInfo[] getCreationOrder() {
        TableInfo[] infos = new TableInfo[this.creationOrder.size()];
        return this.creationOrder.toArray(infos);
    }

    public int getTableCount() {
        return this.tableMap.keySet().size();
    }

    public TableInfo getTableInfo(String tableName) {
        return this.tableMap.get(tableName);
    }

    public boolean isCreated(String tableName) {
        return this.created.get(tableName);
    }

    public TableInfo getNextTableToCreate() {
        for (String tableName : this.tableMap.keySet()) {
            if (!this.created.get(tableName) && this.tableMap.get(tableName).canBeCreated()) {
                this.setCreated(tableName);
                return this.getTableInfo(tableName);
            }
        }
        return null;
    }
}
