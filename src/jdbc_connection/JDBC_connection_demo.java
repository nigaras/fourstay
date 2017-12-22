package jdbc_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC_connection_demo {

	private static String URL = "jdbc:mysql://localhost:3306/hr";
	private static String DbUserName = "root";
	private static String DbPassword = "root";

	public static void main(String[] args) {
		try {
			Connection connection = DriverManager.getConnection(URL, DbUserName, DbPassword);
			System.out.println("MySql database Connection successful");

			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.err.println("MySql database Connection failed");
			e.printStackTrace();
		}
	}
}
