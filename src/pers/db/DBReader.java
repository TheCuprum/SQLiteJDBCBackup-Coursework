package pers.db;

import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import pers.main.Util;

public class DBReader extends BaseDatabaseAccessor{
    
    public DBReader(String _dbName){
        super(_dbName);
    }

   
}
