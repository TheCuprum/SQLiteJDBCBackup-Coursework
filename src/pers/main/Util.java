package pers.main;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Util {
    /**
     * Outputs a stacktrace for debugging and exits
     * <p>
     * To be called following an {@link java.lang.Exception}
     *
     * @param message informational String to display
     * @param e       the Exception
     */
    public static void printExec(String message, Exception e) {
        System.out.println(message + " : " + e);
        e.printStackTrace();
        System.exit(0);
    }

    /**
     * Prints the content of a list
     * 
     * @param objList a list of objects
     */
    public static void printList(Object[] objList, boolean newline) {
        for (Object object : objList) {
            System.out.print(object.toString() + ", ");
        }
        if (newline)
            System.out.println();
    }

    public static void printResultSet(ResultSet rs, int padLength) {
        try {
            ResultSetMetaData rsMeta = rs.getMetaData();
            for (int index = 1; index <= rsMeta.getColumnCount(); index++) {
                System.out.print(Util.padString(rsMeta.getColumnName(index), padLength) + '|');
            }
            System.out.println();
            while (rs.next()) {
                for (int index = 1; index <= rsMeta.getColumnCount(); index++) {
                    System.out.print(Util.padString(rs.getString(index), padLength) + '|');
                }
                System.out.println();
            }
        } catch (SQLException e) {
            printExec("Execrption while printing ResultSet", e);
        }
    }

    public static String padString(String str, int length) {
        byte[] org_bytes = (str != null) ? str.getBytes() : "null".getBytes();
        byte[] new_bytes = new byte[length];
        int bound = (str != null) ? str.length() : 4;
        boolean isCutted = false;

        if (bound > length) {
            bound = length;
            if (length > 2)
                isCutted = true;
        }
        for (int i = 0; i < bound; i++)
            new_bytes[i] = org_bytes[i];

        for (int i = bound; i < length; i++)
            new_bytes[i] = ' ';

        if (isCutted) {
            new_bytes[length - 1] = '.';
            new_bytes[length - 2] = '.';
        }

        return new String(new_bytes);
    }

    public static String hex2String(byte[] bytes) {
        StringBuilder builder = new StringBuilder("0x");
        for (byte hex : bytes) {
            byte higher = (byte) (hex >> 4 & 0x0f);
            byte lower = (byte) (hex & 0x0f);
            char h = (char) ((higher > 9) ? (higher + 'A' - 10) : higher + '0');
            char l = (char) ((lower > 9) ? (lower + 'A' - 10) : lower + '0');
            builder.append(h).append(l);
        }
        return builder.toString();
    }

    public static String convertToString(Object o) {
        if (o == null)
            return "NULL";
        else if (o instanceof Long || o instanceof Integer)
            return o.toString();
        else if (o instanceof Double)
            return o.toString();
        else if (o instanceof byte[])
            return hex2String((byte[]) o);
        else if (o instanceof String) {
            String str = ((String) o).replace("\"", "\"\"").replace("\'", "\'\'");
            return "\"".concat(str).concat("\"");
        }
        return null;
    }
}
