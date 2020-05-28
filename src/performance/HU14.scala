package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU14 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptEncodingHeader("gzip, deflate")
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
		val home =exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(6)
	}

	 object Login {
    val login1 = exec(
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

	val login2 = exec(
      http("Login")
        .get("/login")
        .headers(headers_0)
        .check(css("input[name=_csrf]", "value").saveAs("stoken"))
    ).pause(20)
    .exec(
      http("Logged")
        .post("/login")
        .headers(headers_3)
        .formParam("username", "owner2")
        .formParam("password", "0wn3r")        
        .formParam("_csrf", "${stoken}")
    ).pause(142)
  }

	object ListAppointements {
		val listAppointements=exec(http("listAppointements")
			.get("/appointments")
			.headers(headers_0))
		.pause(21)
	}

	object ShowAppointment {
		val showAppointment1=exec(http("showAppointment")
			.get("/appointments/1")
			.headers(headers_0))
		.pause(15)

		val showAppointment2=exec(http("showAppointment")
			.get("/appointments/2")
			.headers(headers_0))
		.pause(15)
	}

	object CanNotDeleteTheAppointmentInTheSameDay { 
		val canNotDeleteInTheAppointmentSameDay= exec(http("canNotDeleteTheAppointmentInTheSameDay")
			.get("/appointments/delete/1")
			.headers(headers_0))
		.pause(21)
	}

	val scn1=scenario("scn1").exec(		Home.home,
										Login.login1,
										ListAppointements.listAppointements,
										ShowAppointment.showAppointment1,
										CanNotDeleteTheAppointmentInTheSameDay.canNotDeleteInTheAppointmentSameDay)


	val scn2=scenario("scn2").exec(		Home.home,
										Login.login2,
										ListAppointements.listAppointements,
										ShowAppointment.showAppointment2
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