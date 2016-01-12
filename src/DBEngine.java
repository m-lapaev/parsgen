import ru.itmo.model.Field;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Администратор on 14.10.15.
 */
public class DBEngine {
    private static DBEngine dbUtil;
    private static Connection connection;

    private DBEngine() {

    }

    private static void connectToDB(String connectionString, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(connectionString, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DBEngine getInstance(String connectionString, String user, String password) {
        if (dbUtil == null) {
            dbUtil = new DBEngine();
            connectToDB(connectionString, user, password);
        }
        return dbUtil;
    }

    public synchronized boolean createTable(String tableName, ArrayList<Field> fields) {
        String query = "CREATE TABLE ";
        query += tableName;
        query += "(id SERIAL";
        query += (fields.size() > 0 ? "," : "");
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            query += (field.getName() + " ");
            String dataType = "";
            switch (field.getDataType()) {
                case INTEGER:
                    dataType = "BIGINT";
                    break;
                case STRING:
                    dataType = "VARCHAR" + (field.getLength().equals("DEFAULT SIZE") ? "" : ("(" + field.getLength() + ")"));
                    break;
                case FLOAT:
                    dataType = "REAL";
                    break;
                case BOOLEAN:
                    dataType = "BOOLEAN";
                    break;
                default:
                    return false;
            }
            query += dataType;
            query += (i == (fields.size() - 1) ? "" : ",");
        }
        query += ")";
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(query);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public synchronized boolean processQuery(String query) {
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            if (query.toUpperCase().startsWith("INSERT")) {
                pst.executeUpdate();
            } else {
                pst.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
