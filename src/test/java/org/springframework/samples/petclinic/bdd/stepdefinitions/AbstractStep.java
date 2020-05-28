package org.springframework.samples.petclinic.bdd.stepdefinitions;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class AbstractStep {
	private static WebDriver driver;
	private static StringBuffer verificationErrors = new StringBuffer();


	public WebDriver getDriver() {
		if(AbstractStep.driver==null) {
			String pathToGeckoDriver=".\\src\\main\\resources";
			System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "\\geckodriver.exe");
			AbstractStep.driver = new FirefoxDriver();
			AbstractStep.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		}
		return AbstractStep.driver;
	}

	public void stopDriver() {
		AbstractStep.driver.quit();
		String verificationErrorString = AbstractStep.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
		AbstractStep.driver=null;
	}
}