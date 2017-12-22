package sql_to_excel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utilities.ExcelUtils;

public class SQLtoExcel {
	private static String URL = "jdbc:mysql://localhost:3306/hr";
	private static String DbUserName = "root";
	private static String DbPassword = "root";

	Connection connection = null;
	Statement statement = null;
	ResultSet resultSet = null;
	String sql = "Select * from countries";
	ResultSetMetaData rsMetaData = null;

	@BeforeTest
	public void connectToDb() throws SQLException {
		connection = DriverManager.getConnection(URL, DbUserName, DbPassword);
		System.out.println("MySql database Connection successful");
		statement = connection.createStatement();
		resultSet = statement.executeQuery(sql);
		ExcelUtils.openExcelFile("./countries.xlsx", "Sheet1");
		rsMetaData = resultSet.getMetaData();
	}

	@Test
	public void loadDataToExcel() throws SQLException {

		for (int colNum = 1; colNum <= rsMetaData.getColumnCount(); colNum++) {
			String colName = rsMetaData.getCatalogName(colNum);
			ExcelUtils.setCellData(colName, 0, colNum);
		}

		int currentRow = 1;
		while (resultSet.next()) {
			String countryId = resultSet.getString("country_id");
			String countryName = resultSet.getString("country_name");
			ExcelUtils.setCellData(countryId, currentRow, 0);
			ExcelUtils.setCellData(countryName, currentRow, 1);
			currentRow++;
		}

		// resultSet.last();
		// int rowNum = resultSet.getRow();
		// for (int i = 0; i <= rowNum; i++) {
		// resultSet.absolute(i);
		// String countryId = resultSet.getString("country_id");
		// String countryName = resultSet.getString("country_name");
		// ExcelUtils.setCellData(countryId, i, 0);
		// ExcelUtils.setCellData(countryName, i, 1);
		// }
	}
}
