package pers.main;

import pers.db.DBReader;
import pers.db.DBMetaAccessor;

public class Main{
    public static void main(String[] argv){
        // ExtendedDBAccessor accessor = new ExtendedDBAccessor("data/LSH.db");
        DBMetaAccessor accessor = new DBMetaAccessor("data/Chinook.db");
        accessor.getTableNames();
        // accessor.showMeta(accessor.listAllTables());
        accessor.showMeta(accessor.listIndexes("Album"));
        // accessor.showMeta(accessor.listPrimaryKeys("department"));
        // accessor.showMeta(accessor.listColumns("staff"));
        // accessor.showMeta(accessor.listRef("department", "staff"));

        // accessor.showMeta(accessor.listImportedKeys("work_on"));
    }
}
//query.exec("PRAGMA foreign_keys = ON"); //外键