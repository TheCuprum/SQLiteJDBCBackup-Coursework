package pers.db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;

import pers.db.info.DatabaseInfo;
import pers.db.info.IndexInfo;
import pers.db.info.TableInfo;
import pers.main.Config;

public class DBBackup {
    DBReader reader;
    DBWriter writer;
    BufferedWriter sqlWriter;

    private DatabaseInfo dbInfo = null;

    public DBBackup(String sourceName) throws FileNotFoundException, UnsupportedEncodingException {
        String backupName = sourceName.substring(0, sourceName.lastIndexOf("."))
                .concat(Config.BACKUP_SUFFIX);
        String backupDB = backupName.concat(Config.DB_EXTENSION);
        String backupSQL = backupName.concat(Config.SQL_EXTENSION);

        this.reader = new DBReader(sourceName);
        this.writer = new DBWriter(backupDB);
        this.sqlWriter = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(
                                new File(backupSQL)),
                        Config.IO_DECODE_CHARSET));

        this.writer.appendWriter(this.sqlWriter);
    }

    public void close() {
        try {
            this.reader.close();
            this.writer.close();
            this.sqlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runBackup() {
        this.backupDBMeta();
        this.backupDBContent();
    }

    protected void backupDBMeta() {
        this.dbInfo = this.reader.getDatabaseInfo();
        // create tables
        TableInfo tableInfo;
        while ((tableInfo = this.dbInfo.getNextTableToCreate()) != null) {
            this.writer.createTable(tableInfo);
            if (Config.DEBUG)
                System.out.println("Creating table: " + tableInfo.getTableName());
        }

        // create indexes
        TableInfo[] tables = this.dbInfo.getCreationOrder();
        for (TableInfo table : tables) {
            IndexInfo[] indexes = table.getIndexInfos();
            for (IndexInfo index : indexes) {
                this.writer.createIndex(index);
                if (Config.DEBUG)
                    System.out.println("Creating: " + index.toSQLCreationStatement());
            }
        }
    }

    protected void backupDBContent() {
        this.writer.executeDirectly("PRAGMA foreign_keys = OFF");
        TableInfo[] tables = this.dbInfo.getCreationOrder();
        for (TableInfo table : tables) {
            ResultSet rs = this.reader.getTableContent(table.getTableName());
            SQLDataContainer container = new SQLDataContainer(table, rs);
            this.writer.fillData(container);
            container.close();
            if (Config.DEBUG)
                System.out.println("Adding table data: " + table.getTableName());
        }
        this.writer.executeDirectly("PRAGMA foreign_keys = ON");
    }
}
