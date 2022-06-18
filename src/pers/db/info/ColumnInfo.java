package pers.db.info;

// import java.sql.Types;

public class ColumnInfo {
    String columnName;
    int dataType;
    String columnType;
    int nullable;
    
    // boolean autoIncrement;
    // boolean isPrimaryKey;
    // boolean unique;
    // Object defaultVal;
    // ?? checkPredicate;


    public ColumnInfo(String name, int dataType, String type, int nullable/*, boolean autoIncrement*/){
        this.columnName = name;
        this.dataType = dataType;
        this.columnType = type;
        this.nullable = nullable;
        // this.autoIncrement = autoIncrement;
    }

    public String getColumnName(){
        return this.columnName;
    }
    
    public String getColumnType(){
        return this.columnType;
    }

    public int getDataType(){
        return this.dataType;
    }

    public boolean isNullable(){
        return this.nullable == 1;
    }

    public boolean isNonNullable(){
        return this.nullable == 0;
    }

    // public boolean isAutoIncrement(){
    //     return this.autoIncrement;
    // }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder(this.columnName);
        builder.append(" ").append(columnType);
        if (this.isNonNullable())
            builder.append(" NOT NULL");
        // if (this.autoIncrement)
        //     builder.append(" AUTOMINCRRMENT");
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
