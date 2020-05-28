package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU15 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36")

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
		.pause(12)
	}

	object Login {
		val login = exec(
		http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(20)
		.exec(
		http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner1")
			.formParam("password", "0wn3r")        
			.formParam("_csrf", "${stoken}")
		).pause(142)
	}

	object ListHairdressers{
		val listHairdressers=exec(http("listHairdressers")
			.get("/hairdressers")
			.headers(headers_0))
		.pause(15)
	}

	object ShowHairdresser{
		val showHairdresser=exec(http("showHairdresser")
			.get("/hairdressers/1")
			.headers(headers_0))
		.pause(13)
	}

	object CantCreateAnAppointmentOnAnotherAppointment{
		val cantCreateAnAppointmentOnAnotherAppointment=
			exec(http("newAppointment")
			.get("/hairdressers/1/appointments/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
			).pause(44)
			.exec(http("cantCreateAnAppointmentOnAnotherAppointmentDate")
			.post("/hairdressers/1/appointments/new")
			.headers(headers_3)
			.formParam("name", "Cita Leo")
			.formParam("description", "This is a text")
			.formParam("date", "2020/07/03 18:35")
			.formParam("pet", "Leo")
			.formParam("isPaid", "false")
			.formParam("_csrf", "${stoken}"))
		.pause(49)
		
	}

	object NewAppointmentCreated{
		val newAppointmentCreated=exec(http("newAppointment")
			.get("/hairdressers/1/appointments/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
			).pause(44)
			.exec(http("newAppointmentCreated")
			.post("/hairdressers/1/appointments/new")
			.headers(headers_3)
			.formParam("name", "Cita Leo")
			.formParam("description", "This is a text")
			.formParam("date", "2020/09/03 18:35")
			.formParam("pet", "Leo")
			.formParam("isPaid", "false")
			.formParam("_csrf", "${stoken}"))
		.pause(49)
		
	}


	val scn1=scenario("scn1").exec(	Home.home,
									Login.login,
									ListHairdressers.listHairdressers,
									ShowHairdresser.showHairdresser,
									CantCreateAnAppointmentOnAnotherAppointment.cantCreateAnAppointmentOnAnotherAppointment
									)
						
	val scn2=scenario("scn2").exec(	Home.home,
									Login.login,
									ListHairdressers.listHairdressers,
									ShowHairdresser.showHairdresser,
									NewAppointmentCreated.newAppointmentCreated
									)


	setUp(scn1.inject(rampUsers(3050) during (100 seconds)),
			scn2.inject(rampUsers(3050) during (100 seconds))
			).protocols(httpProtocol)
			.assertions(
				 global.responseTime.max.lt(6000),
				 global.responseTime.mean.lt(1000),
				 global.successfulRequests.percent.gt(95)
			 )
}