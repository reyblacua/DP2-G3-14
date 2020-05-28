package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class HU12StepDefinitions extends AbstractStep{

	@LocalServerPort
	private int port;

	@When("I enter to the page of hairdresser {string}")
	public void iEnterPageOfHairdresser(final String hairdresser) {
		this.getDriver().get("http://localhost:"+this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a/span[2]")).click();
		this.getDriver().findElement(By.linkText(hairdresser)).click();
	}

	@Then("I cannot create an appointment for him")
	public void iCanNotCreateAppointmentForHim() {
		boolean buttonNewAppointment = this.getDriver().findElements(By.linkText("Add New Appointment")).size()!=0;
		Assert.assertEquals(buttonNewAppointment, false);
		this.stopDriver();
	}
}
