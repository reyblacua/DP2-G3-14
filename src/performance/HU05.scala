package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU05 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.png""", """.*.js""", """.*.ico""", """.*.css"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "*/*",
		"Content-Type" -> "application/ocsp-request")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_12 = Map("Accept" -> "image/webp,*/*")

    val uri1 = "http://ocsp.sectigo.com"


	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(7)
	}

	object LoggedO3 {
		val loggedO3 = exec(http("LoginO3")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(10)
		.exec(http("LoggedO3")
			.post(uri1 + "/")
			.headers(headers_2)
			.body(RawFileBody("dp2/hu05/0002_request.dat")))
		.pause(2)
		.exec(http("request_3")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner3")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}"))
		.pause(15)
	}

	object LoggedO10 {
		val loggedO10 = exec(http("LoginO10")
			.get("/login")
			.headers(headers_0)
	        .check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(10)
		.exec(http("LoggedO10")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner10")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}"))
		.pause(8)
	}

	object FindOwners {
		val findOwners = exec(http("FindOwners")
			.get("/owners/find")
			.headers(headers_0))
		.pause(11)
	}

	object OwnersList {
		val ownersList = exec(http("OwnersList")
			.get("/owners?lastName=")
			.headers(headers_0))
		.pause(9)
	}

	object OwnerShowO3 {
		val ownerShowO3 = exec(http("OwnerShowO3")
			.get("/owners/3")
			.headers(headers_0))
		.pause(10)
	}

	object OwnerShowO10 {
		val ownerShowO10 = exec(http("OwnerShowO10")
			.get("/owners/10")
			.headers(headers_0))
		.pause(10)
	}

	object PetOneCreated {
		val petOneCreated = exec(http("CreatePetOne")
			.get("/owners/3/pets/new")
			.headers(headers_0)
	        .check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(30)	
		.exec(http("PetOneCreated")
			.post("/owners/3/pets/new")
			.headers(headers_3)
			.formParam("id", "")
			.formParam("name", "Roko1")
			.formParam("birthDate", "2020/05/01")
			.formParam("type", "cat")
			.formParam("dangerous", "False")
			.formParam("isVaccinated", "Yes")
			.formParam("_csrf", "${stoken}"))
		.pause(21)
	}

	object PetTwoCreated {
		val petTwoCreated = exec(http("CreatePetTwo")
			.get("/owners/3/pets/new")
			.headers(headers_0)
	        .check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(11)
		.exec(http("PetTwoCreated")
			.post("/owners/3/pets/new")
			.headers(headers_3)
			.formParam("id", "")
			.formParam("name", "Roko2")
			.formParam("birthDate", "2020/05/02")
			.formParam("type", "cat")
			.formParam("dangerous", "False")
			.formParam("isVaccinated", "Yes")
			.formParam("_csrf", "${stoken}"))
		.pause(29)
	}

	object NoPetCreated {
		val noPetCreated = exec(http("TryCreatePet")
			.get("/owners/10/pets/new")
			.headers(headers_0)
	        .check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(30)
		.exec(http("NoPetCreated")
			.post("/owners/10/pets/new")
			.headers(headers_3)
			.formParam("id", "")
			.formParam("name", "Dialga")
			.formParam("birthDate", "2020/05/01")
			.formParam("type", "dog")
			.formParam("dangerous", "False")
			.formParam("isVaccinated", "Yes")
			.formParam("_csrf", "${stoken}"))
		.pause(31)
	}

	val scn1 = scenario("Owner3").exec(Home.home,
										LoggedO3.loggedO3,
										FindOwners.findOwners,
										OwnersList.ownersList,
										OwnerShowO3.ownerShowO3,
										PetOneCreated.petOneCreated,
										PetTwoCreated.petTwoCreated)
	
	val scn2 = scenario("Owner10").exec(Home.home,
										LoggedO10.loggedO10,
										FindOwners.findOwners,
										OwnersList.ownersList,
										OwnerShowO10.ownerShowO10,
										NoPetCreated.noPetCreated)
	

	setUp(
		scn1.inject(rampUsers(60000) during (10 seconds)),
		scn2.inject(rampUsers(60000) during (10 seconds))
		).protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(6000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
}