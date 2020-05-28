
package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class HU04StepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;


	@When("I try to add a new answer to announcement {string}")
	public void iTryToAddANewAnswer(final String announcement) {
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.getDriver().findElement(By.linkText(announcement)).click();
		this.getDriver().findElement(By.linkText("Answer to the announcement")).click();
	}

	@Then("I cannot create an answer without vaccinating my pets")
	public void iCanNotCreateAnswerWithoutVaccinatedPets() {
		Assert.assertEquals("You can't send an answer if any of your pets aren't vaccinated", this.getDriver().findElement(By.xpath("//body/div/div/p[2]")).getText());
		this.stopDriver();
	}
}
