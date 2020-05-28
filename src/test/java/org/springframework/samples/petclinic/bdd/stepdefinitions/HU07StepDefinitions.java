
package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class HU07StepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;


	@When("I try to add a new answer to announcement {string} with negative history")
	public void iTryToAddANewAnswer(final String announcement) {
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.getDriver().findElement(By.linkText(announcement)).click();

	}

	@Then("I cannot create an answer with a negative history")
	public void ICanNotCreateAnswerWithNegativeHistory() {
		Assert.assertEquals("You can't answer an announcement if you don't have a possitive history", this.getDriver().findElement(By.xpath("//h")).getText());
		this.stopDriver();
	}

}
