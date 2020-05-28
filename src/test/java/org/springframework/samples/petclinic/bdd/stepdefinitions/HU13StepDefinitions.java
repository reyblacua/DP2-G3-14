package org.springframework.samples.petclinic.bdd.stepdefinitions;

import java.util.List;
import java.util.stream.IntStream;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class HU13StepDefinitions extends AbstractStep{

	@LocalServerPort
	private int port;

	@When("I enter to hairdressers list")
	public void iEnterHairdressersList() {
		this.getDriver().get("http://localhost:"+this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a/span[2]")).click();
	}

	@Then("I see hairdressers")
	public void iSeeHairdressers() {
		int hairdressersNumber = this.getDriver().findElements(By.xpath("//table[@id='hairdressersTable']/tbody/tr")).size();
		Assert.assertEquals(hairdressersNumber, 6);

		List<String> hairdressers = Lists.newArrayList("George Primero","George Segundo","George Tercero","George Cuarto","George Quinto","George Sexto");
		List<String> specialities = Lists.newArrayList("CATS","HAMSTERS","BIRDS","HEDGEHOJS","RABBITS","OTTERS");

		IntStream.range(0, 6).forEach((x)->{
			Assert.assertEquals(hairdressers.get(x), this.getDriver().findElement(By.linkText(hairdressers.get(x))).getText());
			Assert.assertEquals(specialities.get(x), this.getDriver().findElement(By.xpath("//table[@id='hairdressersTable']/tbody/tr["+(x+1)+"]/td[2]")).getText());
		});
		this.stopDriver();
	}
}
