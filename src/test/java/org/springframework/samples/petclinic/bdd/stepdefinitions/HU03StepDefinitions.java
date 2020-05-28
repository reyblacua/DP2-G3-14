
package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class HU03StepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;


	@When("I add an answer to announcement {string} with answer description {string}")
	public void iAddANewAnswer(final String announcement, final String description) {
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.getDriver().findElement(By.linkText(announcement)).click();
		this.getDriver().findElement(By.linkText("Answer to the announcement")).click();
		this.getDriver().findElement(By.id("date")).click();
		this.getDriver().findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a/span")).click();
		this.getDriver().findElement(By.linkText("15")).click();
		this.getDriver().findElement(By.id("description")).click();
		this.getDriver().findElement(By.id("description")).clear();
		this.getDriver().findElement(By.id("description")).sendKeys(description);
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Then("I am redirected to the announcement {string}")
	public void iAmRedirectedToAnnouncement(final String announcement) {
		Assert.assertEquals(announcement, this.getDriver().findElement(By.xpath("//td")).getText());
		this.stopDriver();
	}

	@When("I try to add an answer to announcement {string}")
	public void iTryToAddNewAnswer(final String announcement) {
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.getDriver().findElement(By.linkText(announcement)).click();
	}

	@Then("I can not create an answer")
	public void iCanNotCreateAnswer() {
		Assert.assertFalse(this.getDriver().findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*link=Answer to the announcement[\\s\\S]*$"));
		this.stopDriver();
	}

	@When("I try to add answer to announcement {string} again")
	public void iTryToAddNewAnswerAgain(final String announcement) {
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.getDriver().findElement(By.linkText(announcement)).click();
		this.getDriver().findElement(By.linkText("Answer to the announcement")).click();
	}

	@Then("I am redirected to the error page")
	public void iAmRedirectedToErrorPage() {
		Assert.assertEquals("You can't send more than one answer to the same announcement", this.getDriver().findElement(By.xpath("//body/div/div/p[2]")).getText());
		this.stopDriver();
	}

	@When("I try to add answer to my own announcement {string}")
	public void iTryToAddNewAnswerToMyOwnAnnouncement(final String announcement) {
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.getDriver().findElement(By.linkText(announcement)).click();
	}

	@Then("I can not create an answer to my own announcement")
	public void iCanNotCreateAnswerToMyOwnAnnouncement() {
		Assert.assertFalse(this.getDriver().findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*link=Answer to the announcement[\\s\\S]*$"));
		this.stopDriver();
	}
}
