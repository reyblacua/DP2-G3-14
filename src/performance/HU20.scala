package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU20 extends Simulation {

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
        .formParam("username", "owner10")
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
		val showCourseDangerous=exec(http("showCourse")
			.get("/courses/3")
			.headers(headers_0))
		.pause(11)

		val showCourseNotDangerous=exec(http("showCourse")
			.get("/courses/2")
			.headers(headers_0))
		.pause(11)
	}

	object ListInscriptions{
		val listInscriptions=exec(http("listInscriptions")
			.get("/inscriptions")
			.headers(headers_0))
		.pause(11)
	}


	object NewInscription{
		val newInscriptionDangerous=exec(http("newInscription")
			.get("/courses/3/inscription/new")
			.headers(headers_0))
		.pause(17)

		val newInscriptionNotDangerous=exec(http("newInscriptionPerros")
			.get("/courses/2/inscription/new")
			.headers(headers_0))
			.pause(20)
	}

	object NewInscriptionCreated{
		val newInscriptionCreated=exec(http("newInscriptionCreated")
			.post("/courses/3/inscription/new")
			.headers(headers_2)
			.formParam("date", "2020/05/18")
			.formParam("pet", "Lucky")
			.formParam("isPaid", "false")
			.formParam("_csrf", "6a5aa7bf-2b14-48ea-89a7-694225bd3d38"))
	}
	
	object CanNotCreateInscriptionIfPetIsDangerous{
		val canNotCreateInscriptionIfPetIsDangerous=exec(http("canNotCreateInscriptionIfPetIsDangerous")
			.post("/courses/2/inscription/new")
			.headers(headers_3)
			.formParam("date", "2020/05/19")
			.formParam("pet", "Lucky")
			.formParam("isPaid", "false")
			.formParam("_csrf", "6a5aa7bf-2b14-48ea-89a7-694225bd3d38"))
		.pause(17)
	}

	val scn1=scenario("scn1").exec(		Home.home,
										Login.login,
										ListCourses.listCourses,
										ShowCourse.showCourseDangerous,
										NewInscriptionCreated.newInscriptionCreated,
										ListInscriptions.listInscriptions
										)


	val scn2=scenario("scn2").exec(		Home.home,
										Login.login,
										ListCourses.listCourses,
										ShowCourse.showCourseDangerous,
										CanNotCreateInscriptionIfPetIsDangerous.canNotCreateInscriptionIfPetIsDangerous
										)

	setUp(scn1.inject(rampUsers(4500) during (100 seconds)),
			scn2.inject(rampUsers(4500) during (100 seconds))
			).protocols(httpProtocol)
			.assertions(
				 global.responseTime.max.lt(6000),
				 global.responseTime.mean.lt(1000),
				 global.successfulRequests.percent.gt(95)
			 )
}