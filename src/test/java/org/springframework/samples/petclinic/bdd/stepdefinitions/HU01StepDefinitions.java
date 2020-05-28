
package org.springframework.samples.petclinic.bdd.stepdefinitions;


import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class HU01StepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;


	@When("I add a new announcement for pet {string} with announcement {string}, description {string} and petType {string}")
	public void iAddANewAnnouncement(final String pet, final String name, final String description, final String type) {
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.cssSelector("li:nth-child(3) span:nth-child(2)")).click();
		this.getDriver().findElement(By.linkText("Add New Announcement")).click();
		this.getDriver().findElement(By.id("name")).click();
		this.getDriver().findElement(By.id("name")).sendKeys(name);
		this.getDriver().findElement(By.id("petName")).click();
		this.getDriver().findElement(By.id("petName")).sendKeys(pet);
		this.getDriver().findElement(By.id("description")).click();
		this.getDriver().findElement(By.id("description")).sendKeys(description);
		{
			WebElement dropdown = this.getDriver().findElement(By.id("type"));
			dropdown.findElement(By.xpath("//option[. = '" + type + "']")).click();
		}
		this.getDriver().findElement(By.cssSelector("option:nth-child(3)")).click();
		this.getDriver().findElement(By.cssSelector(".btn-default")).click();
	}

	@Then("the announcement appears in my announcements")
	public void theNewAnnouncementAppears() {
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.cssSelector("li:nth-child(3) span:nth-child(2)")).click();
		int announcementsNumber = this.getDriver().findElements(By.xpath("//table[@id='announcementsTable']/tbody/tr")).size();
		Assert.assertEquals(announcementsNumber, 4);
		this.stopDriver();
	}
	
	@When("I list announcements")
	public void iListAnnouncements() {
		this.getDriver().get("http://localhost:" + this.port);
	    this.getDriver().findElement(By.xpath("//a[contains(@href, '/announcements')]")).click();
	}

	@Then("the create announcements button does not appear")
	public void theButtonNotAppears() {
		 Assert.assertFalse(this.getDriver().findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*link=Add New Announcement[\\s\\S]*$"));
		this.stopDriver();
	}

	
}
