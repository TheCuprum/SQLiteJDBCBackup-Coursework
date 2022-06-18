package pers.main;


import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import pers.db.DBBackup;

public class Main{
    public static String hadleArguments(String[] argv){
        if (argv.length == 1)
            return argv[0];
        else if (argv.length > 1)
            System.err.println("Too many arguments.");
        else
            System.err.println("Not enough arguments.");
        System.out.println("Usage: java <CLASS_NAME> <DATABASE_NAME>");
        System.exit(0);
        return null;
    }

    public static void main(String[] argv){
        String dbName = hadleArguments(argv);

        DBBackup backup = null;
        try {
            backup = new DBBackup(dbName);
            backup.runBackup();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }finally{
            if (backup != null)
                backup.close();
        }
        
       
    }
}