package pers.db.info;

import java.util.ArrayList;

import pers.db.SQLStatementInterface;

public class IndexInfo implements SQLStatementInterface {
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

    public void addColumn(String columnName, boolean checkExist) {
        if (checkExist && this.columnList.contains(columnName))
            return;
        this.columnList.add(columnName);
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getIndexName() {
        return indexName;
    }

    public boolean isUnique() {
        return !this.nonUnique;
    }

    public String[] getColumnList() {
        String[] strArr = new String[this.columnList.size()];
        return this.columnList.toArray(strArr);
    }

    @Override
    public String toSQLCreationStatement() {
        StringBuilder builder = new StringBuilder("CREATE ");
        if (!this.nonUnique)
            builder.append("UNIQUE ");
        builder.append("INDEX ");
        builder.append(this.indexName)
                .append("\n").append("ON ")
                .append(this.tableName);
        if (this.columnList.size() > 0) {
            builder.append("(").append(this.columnList.get(0));
            for (int i = 1; i < this.columnList.size(); i++)
                builder.append(", ").append(this.columnList.get(i));
            builder.append(")");
        }
        builder.append(";");
        return builder.toString();
    }

    @Override
    public String toSQLDeletionStatement() {
        return null;
    }

    @Override
    public int hashCode() {
        String hashString = this.indexName.concat("@").concat(this.tableName);
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
