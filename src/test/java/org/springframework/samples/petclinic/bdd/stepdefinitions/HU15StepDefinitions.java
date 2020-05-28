package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class HU15StepDefinitions extends AbstractStep{

	@LocalServerPort
	private int port;

	@Then("I cannot create appointment because theres another one")
	public void iCannotCreatAppointmentBecauseTheresAnotherOne() {
		Assert.assertEquals("Hairdresser already has an appointment at that time", this.getDriver().findElement(By.xpath("//body/div/div/p[2]")).getText());
		this.stopDriver();
	}


}
