package pers.db;

public class ColumnInfo {
    String columnName;
    String columnType;
    boolean nullable;
    boolean autoIncrement;
    
    // boolean isPrimaryKey;
    // boolean unique;
    // Object defaultVal;
    // ?? checkPredicate;


    public ColumnInfo(String name, String type, boolean nullable, boolean autoIncrement){
        this.columnName = name;
        this.columnType = type;
        this.nullable = nullable;
        this.autoIncrement = autoIncrement;
    }

    public String getColumnName(){
        return this.columnName;
    }
    
    public String getColumnType(){
        return this.columnType;
    }

    public boolean isNullable(){
        return this.nullable;
    }

    public boolean isAutoIncrement(){
        return this.autoIncrement;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder(this.columnName);
        builder.append(" ").append(columnType);
        if (!this.nullable)
            builder.append(" NOT NULL");
        if (this.autoIncrement)
            builder.append(" AUTOMINCRRMENT");
        return builder.toString();
    }

    @Override
    public int hashCode(){
        return this.columnName.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null || !(obj instanceof TableInfo)){
            return false;
        }else{
            return this.hashCode() == obj.hashCode();
        }
    }
}
