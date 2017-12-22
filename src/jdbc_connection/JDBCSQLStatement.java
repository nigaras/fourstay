package jdbc_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCSQLStatement {

	private static String URL = "jdbc:mysql://localhost:3306/hr";
	private static String DbUserName = "root";
	private static String DbPassword = "root";

	public static void main(String[] args) throws SQLException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = DriverManager.getConnection(URL, DbUserName, DbPassword);
			System.out.println("MySql database Connection successful");

			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from countries");
			// resultSet.next();
			while (resultSet.next()) {
				System.out
						.println(resultSet.getString("country_name") + "'s ID is " + resultSet.getString("country_id"));
			}
			System.out.println("-------------Query 2 on the way!-------------");
			System.out.println();
			resultSet.close();

			resultSet = statement.executeQuery("select last_name, department_name "
					+ " from employees e join departments d" + " on e.department_id = d.department_id");
			while (resultSet.next()) {
				System.out.println(
						resultSet.getString("last_name") + " works for " + resultSet.getString("department_name"));
			}
		} catch (SQLException e) {
			System.err.println("MySql database Connection failed");
			e.printStackTrace();

		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
}
