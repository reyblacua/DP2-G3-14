
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
public class HU02StepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;

	
	@When("I enter on announcements section")
	public void iListAnnouncements() {
		this.getDriver().get("http://localhost:" + this.port);
	    this.getDriver().findElement(By.xpath("//a[contains(@href, '/announcements')]")).click();
	}

	@Then("the announcements are listed")
	public void theAnnouncementsAreListed() {
		Assert.assertEquals("Anuncio1", this.getDriver().findElement(By.xpath("//a[contains(text(),'Anuncio1')]")).getText());
		Assert.assertEquals("Anuncio2", this.getDriver().findElement(By.xpath("//a[contains(text(),'Anuncio2')]")).getText());
	    Assert.assertEquals("Anuncio3", this.getDriver().findElement(By.xpath("//a[contains(text(),'Anuncio3')]")).getText());		
	    this.stopDriver();
	}

	
}
