
package org.springframework.samples.petclinic.integration;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.boot.web.server.LocalServerPort;

import groovy.util.logging.Log;
import io.restassured.RestAssured;

@Log
public class CatFactAPITest {

	@LocalServerPort
	private int port;


	@Test
	public void CatFact() {
		RestAssured.when().get("https://arcane-ocean-65006.herokuapp.com/")//
		.then().statusCode(200)//
		.assertThat()//
		.body("data", Matchers.notNullValue());
	}
}
