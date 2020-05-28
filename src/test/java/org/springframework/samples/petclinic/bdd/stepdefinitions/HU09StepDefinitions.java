package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.web.server.LocalServerPort;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class HU09StepDefinitions extends AbstractStep{

	@LocalServerPort
	private int port;

	@When("I add a new appointment for pet {string} with date {string}")
	public void iAddANewAppointment(final String pet, final String date) {
		this.getDriver().get("http://localhost:"+this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a/span[2]")).click();
		this.getDriver().findElement(By.linkText("George Primero")).click();
		this.getDriver().findElement(By.linkText("Add New Appointment")).click();
		this.getDriver().findElement(By.id("name")).click();
		this.getDriver().findElement(By.id("name")).clear();
		this.getDriver().findElement(By.id("name")).sendKeys("CreateAppointmentUITest");
		this.getDriver().findElement(By.id("description")).click();
		this.getDriver().findElement(By.id("description")).clear();
		this.getDriver().findElement(By.id("description")).sendKeys("Description example");
		this.getDriver().findElement(By.id("date")).click();
		this.getDriver().findElement(By.id("date")).clear();
		this.getDriver().findElement(By.id("date")).sendKeys(date);
		new Select(this.getDriver().findElement(By.id("pet"))).selectByVisibleText(pet);
		this.getDriver().findElement(By.xpath("//option[@value='"+pet+"']")).click();
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Then("the appointment appears in my appointments")
	public void theNewAppointmentAppears() {
		Assert.assertEquals("Description example", this.getDriver().findElement(By.linkText("Description example")).getText());
		this.getDriver().findElement(By.linkText("Description example")).click();
		Assert.assertEquals("CreateAppointmentUITest", this.getDriver().findElement(By.xpath("//td")).getText());
		this.stopDriver();
	}
	@When("I have no pets")
	public void iHaveNoPets() {
		this.getDriver().get("http://localhost:"+this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li/a/span[2]")).click();
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		this.getDriver().findElement(By.linkText("Julia Martin")).click();
		int petsNumber = this.getDriver().findElements(By.xpath("//table[@id='pets']/tbody/tr")).size();
		Assert.assertEquals(petsNumber, 0);
	}

	@Then("I can not create an appointment")
	public void iCannotCreateAnAppointment() {
		this.getDriver().get("http://localhost:"+this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a/span[2]")).click();
		this.getDriver().findElement(By.linkText("George Primero")).click();
		this.getDriver().findElement(By.linkText("Add New Appointment")).click();
		Assert.assertEquals("You have no pets to make an appointment", this.getDriver().findElement(By.xpath("//body/div/div/p[2]")).getText());
		this.stopDriver();
	}

}
