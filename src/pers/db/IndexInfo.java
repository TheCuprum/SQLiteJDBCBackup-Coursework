package pers.db;

import java.util.ArrayList;

public class IndexInfo implements SQLStatementInterface{
    String tableName;
    String indexName;
    boolean nonUnique;
    ArrayList<String> columnList;

    public IndexInfo(String table, String index, boolean nonUnique) {
        this(table, index, nonUnique, new String[] {});
    }

    public IndexInfo(String table, String index, boolean nonUnique, String[] columns) {
        this.tableName = table;
        this.indexName = index;
        this.nonUnique = nonUnique;
        this.columnList = new ArrayList<String>();
        for (int i = 0; i < columns.length; i++)
            this.columnList.add(columns[i]);
    }

    public void addColumn(String columnName, boolean checkExist){
        if (checkExist && this.columnList.contains(columnName))
            return;
        this.columnList.add(columnName);
    }

    public String getTableName(){
        return this.tableName;
    }

    public String getIndexName(){
        return indexName;
    }

    public boolean isUnique(){
        return !this.nonUnique;
    }

    public String[] getColumnList(){
        return (String[])this.columnList.toArray();
    }

    @Override
    public String toSQLStatement(){
        StringBuilder builder = new StringBuilder("CREATE INDEX ");
        if (!this.nonUnique) builder.append("UNIDQUE ");
        builder.append(this.indexName)
               .append("\n").append("ON ")
               .append(this.tableName);
        if (this.columnList.size() > 0){
            builder.append("(").append(this.columnList.get(0));
            for (int i = 1; i < this.columnList.size(); i++)
                builder.append(", ").append(this.columnList.get(i));
            builder.append(")");
        }
        builder.append(";");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        String hashString = this.tableName.concat("@").concat(this.indexName);
        return hashString.hashCode();
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
