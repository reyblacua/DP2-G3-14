
package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import io.cucumber.java.en.Given;
import lombok.extern.java.Log;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginStepsDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;


	@Given("I am logged in the system as {string} with password {string}")
	public void iAmLoggedInTheSystem(final String user, final String password) {
		LoginStepsDefinitions.loginAs(user, password, this.port, this.getDriver());

	}

	@Given("I am not logged in the system")
	public void iAmNotLoggedInTheSystem() {
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.xpath("//a[contains(text(),'Login')]")).getText();
	}

	public static void loginAs(final String user, final String password, final int port, final WebDriver driver) {
		driver.get("http://localhost:" + port);
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(user);
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Assert.assertEquals(user.toUpperCase(), driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
	}
}
