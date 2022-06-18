package pers.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import pers.main.Util;
import pers.main.Config;

public class BaseDatabaseAccessor {
    /**
     * Set to true to enable debug messages
     */
    boolean debug = Config.DEBUG;

    /**
     * Name of database driver
     *
     * @see Class#forName( )
     */
    private static final String JDBC_DRIVER = "org.sqlite.JDBC";

    /**
     * URI prefix for database location
     *
     * @see java.sql.DriverManager#getConnection( )
     */
    private static final String DATABASE_LOCATION = "jdbc:sqlite:";

    /**
     * {@link java.sql.Connection} for the database
     *
     * @see #getConnection( )
     */
    protected Connection con = null;

    /**
     * Filesystem path to database
     *
     * @see #DbBasic(String)
     */
    public String dbName = null;

    /**
     * Constructor
     * <p>
     * Records a copy of the database name and
     * opens the database for use
     *
     * @param _dbName String holding the name of the database,
     *                for example, C:/directory/subdir/mydb.db
     */
    public BaseDatabaseAccessor(String _dbName, boolean checkExist) {
        dbName = _dbName;

        if (debug)
            System.out.println(
                    "Db.constructor ["
                            + dbName
                            + "]");

        open(checkExist);
    }

    /**
     * Opens database
     * <p>
     * Confirms database file exists and if so,
     * loads JDBC driver and establishes JDBC connection to database
     */
    private void open(boolean checkExist) {
        File dbf = new File(dbName);

        if (checkExist && dbf.exists() == false) {
            System.out.println(
                    "SQLite database file ["
                            + dbName
                            + "] does not exist");
            System.exit(0);
        }

        try {
            Class.forName(JDBC_DRIVER);
            getConnection();
        } catch (ClassNotFoundException cnfe) {
            Util.printExec("Db.Open", cnfe);
        }

        if (debug)
            System.out.println("Db.Open : leaving");
    }

    /**
     * Close database
     * <p>
     * Commits any remaining updates to database and
     * closes connection
     */
    public void close() {
        try {
            con.commit(); // Commit any updates
            con.close();
        } catch (Exception e) {
            Util.printExec("Db.close", e);
        }
        ;
    }

    /**
     * Establish JDBC connection with database
     * <p>
     * Autocommit is turned off delaying updates
     * until commit( ) is called
     */
    private void getConnection() {
        try {
            con = DriverManager.getConnection(
                    DATABASE_LOCATION
                            + dbName);

            /*
             * Turn off AutoCommit:
             * delay updates until commit( ) called
             */
            con.setAutoCommit(false);
        } catch (SQLException sqle) {
            Util.printExec("Db.getConnection database location ["
                    + DATABASE_LOCATION
                    + "] db name["
                    + dbName
                    + "]", sqle);
            close();
        }
    }
}
