package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU06 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.png""", """.*.js""", """.*.ico""", """.*.css"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_9 = Map("Accept" -> "image/webp,*/*")

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(7)
	}

	object LoggedO1 {
		val loggedO1 = exec(http("LoginO1")
			.get("/login")
			.headers(headers_0)
        	.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(10)
		.exec(http("LoggedO1")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "owner1")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}"))
		.pause(7)
	}

	object LoggedO2 {
		val loggedO2 = exec(http("LoginO2")
			.get("/login")
			.headers(headers_0)
        	.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(10)
		.exec(http("LoggedO2")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "owner2")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}"))
		.pause(16)
	}

	object FindOwners {
		val findOwners = exec(http("FindOwners")
			.get("/owners/find")
			.headers(headers_0))
		.pause(10)
	}

	object OwnersList {
		val ownersList = exec(http("OwnersList")
			.get("/owners?lastName=")
			.headers(headers_0))
		.pause(14)
	}

	object OwnerShowO1 {
		val ownerShowO1 = exec(http("OwnerShowO1")
			.get("/owners/1")
			.headers(headers_0))
		.pause(17)
	}

	object OwnerShowO2 {
		val ownerShowO2 = exec(http("OwnerShowO2")
			.get("/owners/2")
			.headers(headers_0))
		.pause(15)
	}


	object PetCreated {
		val petCreated = exec(http("CreatePet")
			.get("/owners/2/pets/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(90) 
		.exec(http("PetCreated")
			.post("/owners/2/pets/new")
			.headers(headers_2)
			.formParam("id", "")
			.formParam("name", "Fimbulvetr")
			.formParam("birthDate", "2020/05/01")
			.formParam("type", "cat")
			.formParam("dangerous", "True")
			.formParam("isVaccinated", "Yes")
			.formParam("_csrf", "${stoken}"))
		.pause(14)
	}


	object NoPetCreated {
		val noPetCreated = exec(http("TryCreatePet")
			.get("/owners/1/pets/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(25)
		.exec(http("NoPetCreated")
			.post("/owners/1/pets/new")
			.headers(headers_2)
			.formParam("id", "")
			.formParam("name", "Elfire")
			.formParam("birthDate", "2020/05/02")
			.formParam("type", "dog")
			.formParam("dangerous", "True")
			.formParam("isVaccinated", "Yes")
			.formParam("_csrf", "${stoken}"))
		.pause(7)
	}

	val scn1 = scenario("Owner2").exec(Home.home,
										LoggedO2.loggedO2,
										FindOwners.findOwners,
										OwnersList.ownersList,
										OwnerShowO2.ownerShowO2,
										PetCreated.petCreated)
	
	val scn2 = scenario("Owner1").exec(Home.home,
										LoggedO1.loggedO1,
										FindOwners.findOwners,
										OwnersList.ownersList,
										OwnerShowO1.ownerShowO1,
										NoPetCreated.noPetCreated)

	setUp(
		scn1.inject(rampUsers(95000) during (10 seconds)),
		scn2.inject(rampUsers(95000) during (10 seconds)),
		).protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(6000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}