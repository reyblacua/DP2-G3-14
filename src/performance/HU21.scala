package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU21 extends Simulation {

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
        .formParam("username", "owner11")
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
        .formParam("username", "owner1")
        .formParam("password", "0wn3r")        
        .formParam("_csrf", "${stoken}")
    ).pause(142)
  }

	object ListCourses{
		val listCourses=
			exec(http("listCourses")
			.get("/courses")
			.headers(headers_0))
		.pause(11)
	}


	object ShowCourse{
		val showCourse1=exec(http("showCourse")
			.get("/courses/5")
			.headers(headers_0))
		.pause(11)

		val showCourse2=exec(http("showCourse")
			.get("/courses/1")
			.headers(headers_0))
		.pause(11)
	}

	object ListInscriptions{
		val listInscriptions=exec(http("listInscriptions")
			.get("/inscriptions")
			.headers(headers_0))
		.pause(11)
	}

	object NewInscriptionCreated{
		val newInscriptionCreated=exec(http("newInscription")
			.get("/courses/1/inscription/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			)
		.pause(17)
		.exec(http("newInscriptionCreated")
			.post("/courses/1/inscription/new")
			.headers(headers_2)
			.formParam("date", "2020/05/18")
			.formParam("pet", "Poppy")
			.formParam("isPaid", "false")
			.formParam("_csrf", "${stoken}"))
	}
	
	object CanNotCreateInscriptionIfCourseIsFull{
		val canNotCreateInscriptionIfCourseIsFull=exec(http("newInscriptionPerros")
			.get("/courses/5/inscription/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			)
			.pause(20)
			.exec(http("canNotCreateInscriptionIfCourseIsFull")
			.post("/courses/5/inscription/new")
			.headers(headers_3)
			.formParam("date", "2020/05/19")
			.formParam("pet", "Lucky")
			.formParam("isPaid", "false")
			.formParam("_csrf", "${stoken}"))
		.pause(17)
	}

	val scn1=scenario("scn1").exec(		Home.home,
										Login.login1,
										ListCourses.listCourses,
										ShowCourse.showCourse1,
										NewInscriptionCreated.newInscriptionCreated,
										ListInscriptions.listInscriptions
										)


	val scn2=scenario("scn2").exec(		Home.home,
										Login.login2,
										ListCourses.listCourses,
										ShowCourse.showCourse2,
										CanNotCreateInscriptionIfCourseIsFull.canNotCreateInscriptionIfCourseIsFull
										)

	setUp(scn1.inject(rampUsers(1750) during (100 seconds)),
			scn2.inject(rampUsers(1750) during (100 seconds))
			).protocols(httpProtocol)
			.assertions(
				 global.responseTime.max.lt(6000),
				 global.responseTime.mean.lt(1000),
				 global.successfulRequests.percent.gt(95)
			 ) 
}