
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class testCase {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  private String[] username=new String[97];
  private Map<String, String> map=new HashMap<String, String>();
  
  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://www.katalon.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    FileInputStream fis = new FileInputStream(new File("C:/Users/DELL/Desktop/input.xlsx"));
    XSSFWorkbook workbook = new XSSFWorkbook(fis);
    XSSFSheet sheet = workbook.getSheet("sheet1");
    for(int i=0;i<97;i++){
    	XSSFRow row = sheet.getRow(i);
        XSSFCell cell = row.getCell(0);
        String cellValue="";
        if(cell.getRawValue().length()!=10){
        	cellValue = cell.getStringCellValue();
        }
        else{
        	DecimalFormat df = new DecimalFormat("#");
        	cellValue = df.format(cell.getNumericCellValue());
        }
        username[i]=cellValue;
    }
    for(int i=0;i<97;i++){
    	XSSFRow row = sheet.getRow(i);
        XSSFCell cell = row.getCell(1);
        String cellValue="";
        cellValue = cell.getStringCellValue();
        if(i==80){
        	map.put(username[i],cellValue.substring(0, cellValue.length()-5));
            continue;
        }
        if(cellValue.startsWith(" ")){
        	map.put(username[i],cellValue.substring(1, cellValue.length()));
        }
        else if((cellValue.endsWith("/")&&i!=93)||cellValue.endsWith(" ")||i==84){
        	map.put(username[i],cellValue.substring(0, cellValue.length()-1));
        }
        else{
        	map.put(username[i],cellValue);
        }
    }
  }

  @Test
  public void testUntitledTestCase() throws Exception {
	for(int i=0;i<97;i++){
		if(i==73){
			continue;
		}
		driver.get("https://psych.liebes.top/st");
	    driver.findElement(By.id("username")).click();
	    driver.findElement(By.id("username")).clear();
	    driver.findElement(By.id("username")).sendKeys(username[i]);
	    driver.findElement(By.id("password")).click();
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys(username[i].substring(4, 10));
	    driver.findElement(By.id("submitButton")).click();
		String url=driver.findElement(By.xpath("//p")).getText();
		assertEquals(url,map.get(username[i]));		
	}
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
