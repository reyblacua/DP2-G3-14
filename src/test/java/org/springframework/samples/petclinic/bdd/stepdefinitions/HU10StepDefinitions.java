package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class HU10StepDefinitions extends AbstractStep{

	@LocalServerPort
	private int port;

	@Then("I cannot create an appointment without paying previous ones")
	public void iCannotCreateAnAppointmentWithoutPayingPreviousOnes() {
		this.getDriver().get("http://localhost:"+this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a/span[2]")).click();
		this.getDriver().findElement(By.linkText("George Primero")).click();
		this.getDriver().findElement(By.linkText("Add New Appointment")).click();
		Assert.assertEquals("You have to pay previous appointments", this.getDriver().findElement(By.xpath("//body/div/div/p[2]")).getText());
		this.stopDriver();
	}

}
