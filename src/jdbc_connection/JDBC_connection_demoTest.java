package jdbc_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class JDBC_connection_demoTest {
	private static String URL = "jdbc:mysql://localhost:3306/hr";
	private static String DbUserName = "root";
	private static String DbPassword = "root";

	Connection connection = null;
	Statement statement = null;
	ResultSet resultSet = null;

	@BeforeTest
	public void connectToDb() throws SQLException {
		connection = DriverManager.getConnection(URL, DbUserName, DbPassword);
		System.out.println("MySql database Connection successful");
	}

	@Test
	public void getRowsCount() throws SQLException {
		statement = connection.createStatement();
		resultSet = statement.executeQuery("select * from employees where job_id = 'IT_PROG' order by salary");
		resultSet.last();
		int rowsCount = resultSet.getRow();
		System.out.println("Number of rows: " + rowsCount);
		System.out.println("--------next test--------");

	}

	@Test(dependsOnMethods = { "getRowsCount" })
	private void goReverse() throws SQLException {
		resultSet.afterLast();
		while (resultSet.previous()) {
			System.out.println(resultSet.getString("Last_name") + " earns " + resultSet.getDouble("salary"));
		}
	}

	@Test(dependsOnMethods = { "goReverse" })
	private void goToSpecificRow() throws SQLException {
		resultSet.absolute(2);
		System.out.println("2nd row:");
		System.out.println(resultSet.getString("last_name") + " earns " + resultSet.getDouble("salary"));
	}

	@Test(dependsOnMethods = { "goToSpecificRow" })
	private void printAll() throws SQLException {
		statement = connection.createStatement();
		resultSet.close();
		resultSet = statement.executeQuery("select department_id, count(*) from employees group by department_id");
		resultSet.beforeFirst();
		while (resultSet.next()) {
			System.out.println("Dep_id: " + resultSet.getString(1) + "; Num of count: " + resultSet.getString(2));
		}
	}
	// second option:
	// while (resultSet.next()) {
	// int currentRow = resultSet.getRow();
	// int currentDepID = resultSet.getInt("department_id");
	// int currentCount = resultSet.getInt("count(*)");
	//
	// System.out.println(currentRow + "\t" + currentDepID + "\t" + currentCount);
	// }

	@AfterTest
	private void colseDown() throws Exception {
		if (resultSet != null) {
			resultSet.close();
		}
		if (statement != null) {
			statement.close();
		}
		if (connection != null) {
			connection.close();
		} else {
			throw new Exception();
		}

	}
}
