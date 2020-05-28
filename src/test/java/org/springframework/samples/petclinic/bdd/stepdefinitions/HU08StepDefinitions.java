
package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class HU08StepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;


	@When("I try to add a new answer to announcement {string} with pet not ready for adopting")
	public void iTryToAddANewAnswerWithPetNotReadyAdopting(final String announcement) {
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.getDriver().findElement(By.linkText(announcement)).click();
	}

	@Then("I cannot create an answer with pet not ready for adopting")
	public void iCanNotCreateAnswerWithPetNotReady() {
		Assert.assertFalse(this.getDriver().findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*link=Answer to the announcement[\\s\\S]*$"));
		this.stopDriver();
	}

}
