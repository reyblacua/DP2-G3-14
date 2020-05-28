package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU07 extends Simulation {

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
		.pause(5)
	}

	object LoginOwner8 {
		val loginOwner8 = exec(http("LoginOwner8")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(23)
		.exec(http("LoggedOwner8")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner8")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}"))
		.pause(15)
	}

	object AnnouncementsList {
		val announcementsList = exec(http("AnnouncementsList")
			.get("/announcements")
			.headers(headers_0))
		.pause(12)
	}

	object ShowAnnouncement {
		val showAnnouncement = exec(http("ShowAnnouncement")
			.get("/announcements/2")
			.headers(headers_0))
		.pause(11)
	}

	object NewAnswer {
		val newAnswer = exec(http("AnswerForm")
			.get("/announcements/2/answer/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(40)
		exec(http("AnswerCreation")
			.post("/announcements/2/answer/new")
			.headers(headers_3)
			.formParam("date", "2020/05/04")
			.formParam("description", "Esta mascota es muy bonita, me gustaria poder adoptarla!")
			.formParam("answerId", "")
			.formParam("_csrf", "${stoken}"))
		.pause(23)
	}

	object LoginOwner4 {
		val loginOwner4 = exec(http("LoginOwner4")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(25)
		.exec(http("LoggedOwner4")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner4")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}"))
		.pause(17)
	}

	object ErrorBadHistory {
		val errorBadHistory = exec(http("request_14")
			.get("/announcements/2")
			.headers(headers_0))
		.pause(24)
	}

	val goodHistoryScn = scenario("HU07_GoodHistory").exec(Home.home,
														  LoginOwner8.loginOwner8,
														  AnnouncementsList.announcementsList,
														  ShowAnnouncement.showAnnouncement,
														  NewAnswer.newAnswer)
	val badHistoryScn = scenario("HU07_BadHistoryError").exec(Home.home,
														  LoginOwner4.loginOwner4,
														  AnnouncementsList.announcementsList,
														  ShowAnnouncement.showAnnouncement,
														  ErrorBadHistory.errorBadHistory)
		

	setUp(goodHistoryScn.inject(rampUsers(2000) during (100 seconds)),
            badHistoryScn.inject(rampUsers(2000) during (100 seconds))
            ).protocols(httpProtocol)
			 .assertions(
                 global.responseTime.max.lt(6000),
                 global.responseTime.mean.lt(1000),
                 global.successfulRequests.percent.gt(95)
             )
}