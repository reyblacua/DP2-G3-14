package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU04 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

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
		.pause(10)
	}

	object LoginOwner7 {
		val loginOwner7 = exec(http("LoginOwner7")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(22)
		.exec(http("LoggedOwner7")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner7")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}

	object AnnouncementsList {
		val announcementsList = exec(http("AnnouncementsList")
			.get("/announcements")
			.headers(headers_0))
		.pause(12)
	}

	object ShowAnnouncement {
		val showAnnouncement = exec(http("ShowAnnouncement")
			.get("/announcements/1")
			.headers(headers_0))
		.pause(43)
	}

	object NewAnswer {
		val newAnswer = exec(http("AnswerForm")
			.get("/announcements/1/answer/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(25)
		exec(http("AnswerCreated")
			.post("/announcements/1/answer/new")
			.headers(headers_3)
			.formParam("date", "2020/05/06")
			.formParam("description", "Me gustaria adoptar a esta mascota!!")
			.formParam("answerId", "")
			.formParam("_csrf", "${stoken}"))
		.pause(24)
	}

	object LoginOwner6 {
		val loginOwner6 = exec(http("LoginOwner6")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(24)
		.exec(http("LoggedOwner6")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner6")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}"))
		.pause(9)
	}

	object ErrorVacunas {
		val errorVacunas = exec(http("ErrorVacunas")
			.get("/announcements/1/answer/new")
			.headers(headers_0))
		.pause(12)
	}

	val vacinnatedScn = scenario("HU04_Vacinnated").exec(Home.home,
														  LoginOwner7.loginOwner7,
														  AnnouncementsList.announcementsList,
														  ShowAnnouncement.showAnnouncement,
														  NewAnswer.newAnswer)
	val nonVacinnatedScn = scenario("HU04_NonVacinnatedError").exec(Home.home,
														  LoginOwner6.loginOwner6,
														  AnnouncementsList.announcementsList,
														  ShowAnnouncement.showAnnouncement,
														  ErrorVacunas.errorVacunas)
		

	setUp(vacinnatedScn.inject(rampUsers(2000) during (100 seconds)),
            nonVacinnatedScn.inject(rampUsers(2000) during (100 seconds))
            ).protocols(httpProtocol)
			 .assertions(
                 global.responseTime.max.lt(6000),
                 global.responseTime.mean.lt(1000),
                 global.successfulRequests.percent.gt(95)
             )
}