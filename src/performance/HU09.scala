package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU09 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(11)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(9)
		.exec(http("Login")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner1")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}")
		).pause(28)
	}

	object ShowHairdressers {
		val showHairdressers = exec(http("Show hairdressers")
			.get("/hairdressers")
			.headers(headers_0)
		).pause(2)
	}

	object ShowHairdresser {
		val showHairdresser = exec(http("Show hairdresser")
			.get("/hairdressers/1")
			.headers(headers_0)
		).pause(20)
	}

	object AddNewAppointment {
		val addNewAppointment = exec(http("Add new appointment")
			.get("/hairdressers/1/appointments/new")
			.headers(headers_0)
		  	.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(40)
		.exec(http("Appointment created")
			.post("/hairdressers/1/appointments/new")
			.headers(headers_3)
			.formParam("name", "new appointment")
			.formParam("description", "a brand new appointment")
			.formParam("date", "2020/08/04 20:00")
			.formParam("pet", "Leo")
			.formParam("isPaid", "false")
		 	.formParam("_csrf", "${stoken}") 
		).pause(30)
	}

	object Login2 {
		val login2 = exec(http("Login2")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(15)
		.exec(http("Login2")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner12")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}")
		).pause(16)
	}

	object CannotAddAppointment {
		val cannotAddAppointment = exec(http("Cannot add appointment")
			.get("/hairdressers/1/appointments/new")
			.headers(headers_0)
		).pause(10)
	}

	val scn1 = scenario("Create appointment").exec(
		Home.home,
		Login.login,
		ShowHairdressers.showHairdressers,
		ShowHairdresser.showHairdresser,
		AddNewAppointment.addNewAppointment,
	)

	val scn2 = scenario("Cannot create appointment").exec(
		Home.home,
		Login2.login2,
		ShowHairdressers.showHairdressers,
		ShowHairdresser.showHairdresser,
		CannotAddAppointment.cannotAddAppointment
	)
		
	setUp(scn1.inject(rampUsers(5500) during (100 seconds)),
			scn2.inject(rampUsers(5500) during (100 seconds))
			).protocols(httpProtocol)
			.assertions(
				 global.responseTime.max.lt(6000),
				 global.responseTime.mean.lt(1000),
				 global.successfulRequests.percent.gt(95)
			 )
}