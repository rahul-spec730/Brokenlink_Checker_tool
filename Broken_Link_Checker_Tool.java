package testng;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Broken_Link_Checker_Tool {
	
	ChromeDriver driver;

	@BeforeTest
	public void setup() {
		driver = new ChromeDriver();
	}
	@Parameters("url")
	@BeforeMethod
	public void browserUrl(String url) {
		driver.get(url);
		driver.manage().window().maximize();
	}
	@Parameters("url")
	@Test
	public void urlScan(String url) throws Exception  {
		List <WebElement> lis = driver.findElements(By.tagName("a"));
		int brokencount=0;
		for(WebElement linktext:lis) {
			String links =linktext.getAttribute("href");
			if (links == null || links.isEmpty() || links.startsWith("javascript") ) {
                continue;
            }
			try {
			URI link = new URI(links);
			HttpURLConnection http=(HttpURLConnection)link.toURL().openConnection();
			int response = http.getResponseCode();
			if(response>=200 && response<=299) {
				System.out.println("Successful URL:"+links+" Responsecode:"+response);
			}
			if(response>=400) {
				System.out.println("Broken URL:"+links+" Responsecode:"+response);
				brokencount++;
			}
			}catch (Exception e) {
                System.out.println("Exception: " + links + " | " + e.getMessage());
            }
		}System.out.println("BrokenlinksCount: "+brokencount);
	}
	@AfterTest
	public void tearDown() {
		driver.close();
	}

}
