
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
public class HU05StepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;


	@When("I add a new pet with name {string} and petType {string} exceeding the limit with special permission")
	public void iAddANewPetWithSpecialPermissionExceedingTheLimit(final String name, final String type) {
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li/a/span[2]")).click();
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		this.getDriver().findElement(By.xpath("//a[contains(text(),'Eduardo Rodriquez')]")).click();
		this.getDriver().findElement(By.xpath("//tr[6]/td")).click();
	    Assert.assertEquals("Yes", this.getDriver().findElement(By.xpath("//tr[6]/td")).getText());
	    this.getDriver().findElement(By.xpath("//a[contains(text(),'Add New Pet')]")).click();
	    this.getDriver().findElement(By.id("name")).click();
	    this.getDriver().findElement(By.id("name")).clear();
	    this.getDriver().findElement(By.id("name")).sendKeys("prueba1");
	    this.getDriver().findElement(By.id("birthDate")).click();
	    this.getDriver().findElement(By.id("birthDate")).clear();
	    this.getDriver().findElement(By.id("birthDate")).sendKeys("2012/08/06");
	    this.getDriver().findElement(By.xpath("//option[@value='dog']")).click();
	    this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	    this.getDriver().findElement(By.xpath("//a[contains(text(),'Add New Pet')]")).click();
	    this.getDriver().findElement(By.id("name")).click();
	    this.getDriver().findElement(By.id("name")).clear();
	    this.getDriver().findElement(By.id("name")).sendKeys(name);
	    this.getDriver().findElement(By.id("birthDate")).click();
	    this.getDriver().findElement(By.id("birthDate")).clear();
	    this.getDriver().findElement(By.id("birthDate")).sendKeys("2020/01/06");
	    this.getDriver().findElement(By.xpath("//option[@value='cat']")).click();
	    this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Then("the pet with name {string} and petType {string} appears in my owners profile exceeding the limit with special permission")
	public void theNewPetAppearsWithSpecialPermissionExceedingTheLimit(final String name, final String type) {
		Assert.assertEquals(name, this.getDriver().findElement(By.xpath("//table[@id='pets']/tbody/tr[2]/td/dl/dd")).getText());
	    Assert.assertEquals(type, this.getDriver().findElement(By.xpath("//table[@id='pets']/tbody/tr[2]/td/dl/dd[3]")).getText());
		this.stopDriver();
	}
	
	@When("I add a new pet with name {string} and petType {string}")
	public void iAddANewPet(final String name, final String type) {
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li/a/span[2]")).click();
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		this.getDriver().findElement(By.xpath("//a[contains(text(),'Harold Davis')]")).click();
		this.getDriver().findElement(By.xpath("//tr[6]/td")).click();
	    this.getDriver().findElement(By.xpath("//a[contains(text(),'Add New Pet')]")).click();
	    this.getDriver().findElement(By.id("name")).click();
	    this.getDriver().findElement(By.id("name")).clear();
	    this.getDriver().findElement(By.id("name")).sendKeys(name);
	    this.getDriver().findElement(By.id("birthDate")).click();
	    this.getDriver().findElement(By.id("birthDate")).clear();
	    this.getDriver().findElement(By.id("birthDate")).sendKeys("2020/01/06");
	    this.getDriver().findElement(By.xpath("//option[@value='dog']")).click();
	    this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@Then("the pet with name {string} and petType {string} appears in my owners profile")
	public void theNewPetAppears(final String name, final String type) {
		Assert.assertEquals(name, this.getDriver().findElement(By.xpath("//table[@id='pets']/tbody/tr[2]/td/dl/dd")).getText());
	    Assert.assertEquals(type, this.getDriver().findElement(By.xpath("//table[@id='pets']/tbody/tr[2]/td/dl/dd[3]")).getText());
		this.stopDriver();
	}

	@When("I add a new pet with name {string} and petType {string} exceeding the limit")
	public void iAddANewPetExceedingTheLimit(final String name, final String type) {
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li/a/span[2]")).click();
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		this.getDriver().findElement(By.xpath("//a[contains(text(),'Ruben Doblas')]")).click();
		this.getDriver().findElement(By.xpath("//tr[6]/td")).click();
	    this.getDriver().findElement(By.xpath("//a[contains(text(),'Add New Pet')]")).click();
	    this.getDriver().findElement(By.id("name")).click();
	    this.getDriver().findElement(By.id("name")).clear();
	    this.getDriver().findElement(By.id("name")).sendKeys(name);
	    this.getDriver().findElement(By.id("birthDate")).click();
	    this.getDriver().findElement(By.id("birthDate")).clear();
	    this.getDriver().findElement(By.id("birthDate")).sendKeys("2020/01/06");
	    this.getDriver().findElement(By.xpath("//option[@value='cat']")).click();
	    this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@Then("the pet with name {string} and petType {string} appears in my owners profile exceeding the limit")
	public void theNewPetAppearsExceedingTheLimit(final String name, final String type) {
		Assert.assertEquals(name, this.getDriver().findElement(By.xpath("//table[@id='pets']/tbody/tr[3]/td/dl/dd")).getText());
	    Assert.assertEquals(type, this.getDriver().findElement(By.xpath("//table[@id='pets']/tbody/tr[3]/td/dl/dd[3]")).getText());
		this.stopDriver();
	}

	
}
