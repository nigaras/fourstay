package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBUtility {
	private static String URL = "jdbc:mysql://localhost:3306/hr";
	private static String DbUserName = "root";
	private static String DbPassword = "root";

	private static String oraURL = "jdbc:oracle:thin:@localhost:49161:xe";
	private static String oraDbUserName = "HR";
	private static String oraDbPassword = "HR";

	private static Connection connection;
	private static Statement statement;
	private static ResultSet resultSet;

	public enum DBType {
		MYSQL, ORACLE, SYBASE
	}

	public static Connection getConnection(DBType dbType) throws SQLException {

		switch (dbType) {
		case ORACLE:
			return DriverManager.getConnection(oraURL, oraDbUserName, oraDbPassword);

		case MYSQL:
			return DriverManager.getConnection(URL, DbUserName, DbPassword);

		default:
			return null;
		}
	}

	public static void establishConnection(DBType dbType) throws SQLException {

		switch (dbType) {
		case ORACLE:
			connection = DriverManager.getConnection(oraURL, oraDbUserName, oraDbPassword);
			break;
		case MYSQL:
			connection = DriverManager.getConnection(URL, DbUserName, DbPassword);
			break;
		default:
			throw new RuntimeException("Invalid DBType");
		}
	}

	/**
	 * ========================Finished Connection=================================
	 * ========================Start to run Query==================================
	 */

	public static List<String[]> runSQLQuery(String sql) {
		List<String[]> queryResult = new ArrayList<>();

		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sql);
			ResultSetMetaData rsMetada = resultSet.getMetaData();

			int columnsCount = rsMetada.getColumnCount();
			resultSet.last();
			int recordCount = resultSet.getRow();

			if (columnsCount == 0 || recordCount == 0) {
				return null;
			}

			resultSet.beforeFirst();

			while (resultSet.next()) {
				String[] cellData = new String[columnsCount];

				for (int cell = 1; cell <= columnsCount; cell++) {
					cellData[cell - 1] = resultSet.getString(cell);

				}
				queryResult.add(cellData);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return queryResult;

	}

	public static List<Integer[]> runSQLIntQuery(String sql) {
		List<Integer[]> queryResult = new ArrayList<>();

		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sql);
			ResultSetMetaData rsMetada = resultSet.getMetaData();

			int columnsCount = rsMetada.getColumnCount();
			resultSet.last();
			int recordCount = resultSet.getRow();

			if (columnsCount == 0 || recordCount == 0) {
				return null;
			}

			resultSet.beforeFirst();

			while (resultSet.next()) {
				Integer[] cellData = new Integer[columnsCount];

				for (int cell = 1; cell <= columnsCount; cell++) {
					cellData[cell - 1] = resultSet.getInt(cell);

				}
				queryResult.add(cellData);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return queryResult;

	}

	public static void printStrList(List<String[]> queryStrResult) {
		// for (String[] rd : queryStrResult) {
		// for (String cellData : rd) {
		// System.out.print(cellData + " ");
		// }
		// System.out.println();
		// }

		for (int i = 0; i < queryStrResult.size(); i++) {
			String[] strResult = queryStrResult.get(i);
			for (String str : strResult) {
				System.out.print(str + " ");
			}
			System.out.println();
		}
	}

	public static void printIntList(List<Integer[]> queryIntResult) {
		for (Integer[] rd : queryIntResult) {
			for (int cellData : rd) {
				System.out.print(cellData + " ");
			}
			System.out.println();
		}

	}

	/**
	 * =========================Finished Run and Print Query
	 * Result================================= ==============================Closing
	 * all Connections=========================================
	 */

	public static void closeConnections() {
		try {

			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			System.err.println("Something went wrong!");
			e.printStackTrace();
		}
	}

	/**
	 * ===========================All Set!=================================
	 * =================================================================================
	 * ===============Second Part if all you need is Print
	 * everything=================== ====One Method will start from Connect until
	 * Print and Close all Connections=====
	 */
	public static String printAll(DBType dbType, String sql) {
		try {
			Connection connection = DBUtility.getConnection(dbType);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);

			List<String[]> sqlResultsList = new ArrayList<>();
			ResultSetMetaData rsMetaData = resultSet.getMetaData();

			int columnsCount = rsMetaData.getColumnCount();
			String[] colNames = new String[columnsCount];

			for (int colIndex = 1; colIndex <= columnsCount; colIndex++) {
				colNames[colIndex - 1] = rsMetaData.getColumnName(colIndex);
			}
			sqlResultsList.add(colNames);

			while (resultSet.next()) {
				String[] rowData = new String[columnsCount];
				for (int cellNum = 1; cellNum <= columnsCount; cellNum++) {
					rowData[cellNum - 1] = resultSet.getString(cellNum);
				}
				sqlResultsList.add(rowData);
			}

			for (String[] rd : sqlResultsList) {
				for (String cellData : rd) {
					System.out.print(cellData + " ");
				}
				System.out.println();
			}

			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.err.println("Something went wrong!");
			e.printStackTrace();
		}
		return sql;
	}

}
