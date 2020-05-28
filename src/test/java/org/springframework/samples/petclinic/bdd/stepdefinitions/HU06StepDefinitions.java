
package org.springframework.samples.petclinic.bdd.stepdefinitions;


import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class HU06StepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;


	@When("I add a new pet with name {string} and petType {string} with the dangerous&exotic permission")
	public void iAddANewPetWithExoticPermission(final String name, final String type) {
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li/a")).click();
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		this.getDriver().findElement(By.xpath("//a[contains(text(),'Peter McTavish')]")).click();
	    Assert.assertEquals("Yes", this.getDriver().findElement(By.xpath("//tr[5]/td")).getText());
	    this.getDriver().findElement(By.linkText("Add New Pet")).click();
	    this.getDriver().findElement(By.id("name")).click();
	    this.getDriver().findElement(By.id("name")).clear();
	    this.getDriver().findElement(By.id("name")).sendKeys(name);
	    this.getDriver().findElement(By.id("birthDate")).clear();
	    this.getDriver().findElement(By.id("birthDate")).sendKeys("2020/01/06");
	    new Select(this.getDriver().findElement(By.id("type"))).selectByVisibleText(type);
	    this.getDriver().findElement(By.xpath("//option[@value='cat']")).click();
	    new Select(this.getDriver().findElement(By.id("dangerous"))).selectByVisibleText("Yes");
	    this.getDriver().findElement(By.id("dangerous")).click();
	    this.getDriver().findElement(By.xpath("//option[@value='True']")).click();
	    this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Then("the pet with name {string} and petType {string} appears in my owners profile with the dangerous&exotic permission")
	public void theNewPetAppearsWithExoticPermission(final String name, final String type) {
		Assert.assertEquals(name, this.getDriver().findElement(By.xpath("//table[@id='pets']/tbody/tr[2]/td/dl/dd")).getText());
		Assert.assertEquals(type, this.getDriver().findElement(By.xpath("//table[@id='pets']/tbody/tr[2]/td/dl/dd[3]")).getText());
		this.stopDriver();
	}
	
	@When("I add a new pet with name {string} and petType {string} without the dangerous&exotic permission")
	public void iAddANewPetWithoutExoticPermission(final String name, final String type) {
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li/a/span[2]")).click();
		this.getDriver().findElement(By.id("search-owner-form")).click();
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		this.getDriver().findElement(By.xpath("//a[contains(text(),'George Franklin')]")).click();
		this.getDriver().findElement(By.xpath("//a[contains(text(),'Add New Pet')]")).click();
		this.getDriver().findElement(By.id("name")).click();
		this.getDriver().findElement(By.id("name")).clear();
		this.getDriver().findElement(By.id("name")).sendKeys(name);
		this.getDriver().findElement(By.id("birthDate")).clear();
		this.getDriver().findElement(By.id("birthDate")).sendKeys("2020/01/06");
	    new Select(this.getDriver().findElement(By.id("type"))).selectByVisibleText(type);
	    this.getDriver().findElement(By.xpath("//option[@value='lizard']")).click();
	    this.getDriver().findElement(By.id("dangerous")).click();
	    new Select(this.getDriver().findElement(By.id("dangerous"))).selectByVisibleText("Yes");
	    this.getDriver().findElement(By.xpath("//option[@value='True']")).click();
	    this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Then("the pet is not added in my owners profile without the dangerous&exotic permission")
	public void theNewPetIsNotAdded() {
		Assert.assertEquals("You can't add a new dangerous pet if you don't have the dangerous animals license", this.getDriver().findElement(By.xpath("//body/div/div/p[2]")).getText());
		this.stopDriver();
	}
	
}
