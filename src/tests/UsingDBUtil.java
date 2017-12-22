package tests;

import static org.testng.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utilities.DBUtility;
import utilities.DBUtility.DBType;

public class UsingDBUtil {

	@BeforeTest
	public void setUp() throws SQLException {
		DBUtility.establishConnection(DBType.MYSQL);
	}

	@Test
	public void test() {

		String sql = "SELECT * FROM LOCATIONS WHERE COUNTRY_ID = 'US'";

		List<String[]> queryStrResult = DBUtility.runSQLQuery(sql);

		DBUtility.printStrList(queryStrResult);

		DBUtility.closeConnections();
	}

	@Test
	public void testSum() {

		String sql = "Select min_salary, max_salary, ((min_salary)+ (max_salary)) as sum_salary\n From jobs;";

		List<Integer[]> results = DBUtility.runSQLIntQuery(sql);

		for (Integer[] intResult : results) {
			int actSum = intResult[2];
			int expSum = intResult[0] + intResult[1];
			assertEquals(actSum, expSum);
			System.out.println("exp sum: " + expSum + " -------- act sum: " + actSum);
			System.out.println();
		}

		// DBUtility.printIntList(results);

	}
}
