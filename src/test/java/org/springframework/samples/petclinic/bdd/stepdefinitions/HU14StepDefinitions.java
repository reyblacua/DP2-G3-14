package org.springframework.samples.petclinic.bdd.stepdefinitions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class HU14StepDefinitions extends AbstractStep{

	@LocalServerPort
	private int port;

	@When("I create an appointment for today for pet {string}")
	public void iCreateAppointmentForToday(final String pet) {
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
		this.getDriver().findElement(By.id("date")).sendKeys(LocalDateTime.now().plus(30, ChronoUnit.MINUTES).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));
		new Select(this.getDriver().findElement(By.id("pet"))).selectByVisibleText(pet);
		this.getDriver().findElement(By.xpath("//option[@value='"+pet+"']")).click();
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Then("I cannot delete today appointment")
	public void iCannotDeleteTodayAppointment() {
		this.getDriver().get("http://localhost:"+this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[4]/a/span[2]")).click();
		this.getDriver().findElement(By.linkText("Description example")).click();
		this.getDriver().findElement(By.linkText("Delete Appointment")).click();
		Assert.assertEquals("Error: You cannot delete an appointment whose date is today", this.getDriver().findElement(By.xpath("//body/div/div/p[2]")).getText());
		this.stopDriver();
	}

	@Then("I can delete appointment which is not today")
	public void iCanDeleteAppointmentWhichIsNotToday() {
		this.getDriver().get("http://localhost:"+this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[4]/a/span[2]")).click();
		this.getDriver().findElement(By.linkText("Description example")).click();
		this.getDriver().findElement(By.linkText("Delete Appointment")).click();
		Assert.assertEquals("There are not appointments yet.", this.getDriver().findElement(By.xpath("//body/div/div/p")).getText());
		this.stopDriver();

	}

}
