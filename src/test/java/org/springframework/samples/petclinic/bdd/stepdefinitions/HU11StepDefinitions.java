package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class HU11StepDefinitions extends AbstractStep{

	@LocalServerPort
	private int port;

	@And("I have an appointment for pet {string} on {string}")
	public void iHaveAnAppointmentForPetOnDay(final String pet, final String date) {
		String date2 = new String(date);
		date2 = date2.replace("/", "-");
		date2 = date2.split(" ")[0];
		this.getDriver().get("http://localhost:"+this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[4]/a/span[2]")).click();
		Assert.assertEquals(date2, this.getDriver().findElement(By.xpath("//table[@id='appointmentsTable']/tbody/tr/td[2]")).getText().split(" ")[0]);
		Assert.assertEquals(pet, this.getDriver().findElement(By.xpath("//table[@id='appointmentsTable']/tbody/tr/td[3]")).getText());
	}


	@When("I try to add a new appointment for pet {string} with date {string}")
	public void iTryToAddAppointmentForSamePetOnSameDay(final String pet, final String date) {
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

	@Then("I cannot create an appointment for pet {string} on same date {string}")
	public void iCanNotCreateAppointmentForSamePetOnSameDay(final String pet, final String date) {
		Assert.assertEquals("You cannot make more than one appointment per day for the same pet", this.getDriver().findElement(By.xpath("//body/div/div/p[2]")).getText());
		this.stopDriver();
	}
}
