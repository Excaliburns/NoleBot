package util;

import javax.xml.transform.Result;
import java.sql.*;

public class DBUtils {
    static Connection connection;

    public static void initConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String db_name = PropLoader.getProp("db_name");
        String db_addr = PropLoader.getProp("db_addr");
        String db_user = PropLoader.getProp("db_user");
        String db_pass = PropLoader.getProp("db_pass");

        connection = DriverManager.getConnection("jdbc:mysql://" + db_addr + ":3306/" + db_name, db_user, db_pass);
    }

    public static Connection getConnection() {
        return connection;
    }

    public static PreparedStatement getPreparedAttendanceStatement() throws SQLException {
        String sql = "insert into attendance " + "(dateTime, userID, nickname, discordID) values (?, ?, ?, ?)";
        return getConnection().prepareStatement(sql);
    }

    public static ResultSet executeQuery(String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
        //will be used later
    }
}
