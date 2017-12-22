package tests;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utilities.DBUtility;
import utilities.DBUtility.DBType;

public class DDTUsingJDBC {

	WebDriver driver;

	@BeforeTest
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "/Users/nigarasawirdin/Documents/libraries/Drivers/chromedriver");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://editor.datatables.net/examples/simple/simple.html");
	}

	@Test
	public void test() throws SQLException, InterruptedException {

		String sql = " select first_name, last_name, job_id, city, phone_number, hire_date, salary\n"
				+ " from employees e join departments d\n" + " on e.department_id = d.department_id\n"
				+ " join locations l\n" + " on l.location_id = d.location_id";

		// Connect to database
		DBUtility.establishConnection(DBType.MYSQL);
		// Run SQL query and store into a list
		List<String[]> queryResultList = DBUtility.runSQLQuery(sql);
		// close all connection
		DBUtility.closeConnections();

		WebDriverWait wait = new WebDriverWait(driver, 10);

		for (int rowNum = 0; rowNum < queryResultList.size(); rowNum++) {

			wait.until(ExpectedConditions.elementToBeClickable(By.linkText("New")));
			driver.findElement(By.linkText("New")).click();
			String[] rowData = queryResultList.get(rowNum);
			String firstName = rowData[0];
			String LastName = rowData[1];
			String Position = rowData[2];
			String Office = rowData[3];
			String Extension = rowData[4].substring(8);
			String Startdate = rowData[5];
			String Salary = rowData[6];

			driver.findElement(By.id("DTE_Field_first_name")).sendKeys(firstName);
			driver.findElement(By.id("DTE_Field_last_name")).sendKeys(LastName);
			driver.findElement(By.id("DTE_Field_position")).sendKeys(Position);
			driver.findElement(By.id("DTE_Field_office")).sendKeys(Office);
			driver.findElement(By.id("DTE_Field_extn")).sendKeys(Extension);
			driver.findElement(By.id("DTE_Field_start_date")).sendKeys(Startdate);
			driver.findElement(By.id("DTE_Field_salary")).sendKeys(Salary);
			WebElement creat = driver.findElement(By.xpath("//button[@class='btn']"));
			creat.click();
			wait.until(ExpectedConditions.invisibilityOf(creat));
		}
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}

}
