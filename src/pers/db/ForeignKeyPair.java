package pers.db;

public class ForeignKeyPair {
    String localColumn;
    String foreignColumn;
    String foreignTable;

    public ForeignKeyPair(String localColumn, String foreignColumn, String foreignTable) {
        if (localColumn == null || foreignColumn == null || foreignTable == null)
            throw new NullPointerException();
        this.localColumn = localColumn;
        this.foreignColumn = foreignColumn;
        this.foreignTable = foreignTable;
    }

    public String getLocalColmun() {
        return this.localColumn;
    }

    public String getForeignColumn() {
        return this.foreignColumn;
    }

    public String getForeignTable() {
        return this.foreignTable;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("FOREIGN KEY");
        builder.append("(").append(this.localColumn).append(")")
                .append(" REFERENCES ")
                .append(this.foreignTable)
                .append("(").append(this.foreignColumn).append(")");
        return builder.toString();
    }
}
