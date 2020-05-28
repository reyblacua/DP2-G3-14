package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU08 extends Simulation {

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
		.pause(8)
	}

	object LoginOwner9 {
		val loginOwner9 = exec(http("LoginOwner9")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(16)
		.exec(http("LoggedOwner9")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner9")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "${stoken}"))
		.pause(12)
	}

	object AnnouncementsList {
		val announcementsList = exec(http("AnnouncementsList")
			.get("/announcements")
			.headers(headers_0))
		.pause(20)
	}

	object ShowAnnouncement2 {
		val showAnnouncement2 = exec(http("ShowAnnouncement2")
			.get("/announcements/2")
			.headers(headers_0))
		.pause(14)
	}

	object NewAnswer {
		val newAnswer = exec(http("AnswerForm")
			.get("/announcements/2/answer/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
		).pause(43)
		exec(http("AnswerCreation")
			.post("/announcements/2/answer/new")
			.headers(headers_3)
			.formParam("date", "2020/05/11")
			.formParam("description", "Soy una buena persona y quiero cuidar de esta mascota")
			.formParam("answerId", "")
			.formParam("_csrf", "${stoken}"))
		.pause(15)
	}

	object ShowAnnouncement3 {
		val showAnnouncement3 = exec(http("ShowAnnouncement3")
			.get("/announcements/3")
			.headers(headers_0))
		.pause(16)
	}

	val readyForAddoptionScn = scenario("HU07_ReadyForAddoption").exec(Home.home,
														  LoginOwner9.loginOwner9,
														  AnnouncementsList.announcementsList,
														  ShowAnnouncement2.showAnnouncement2,
														  NewAnswer.newAnswer)
	val notReadyForAddoptionScn = scenario("HU08_NotReadyForAddoption").exec(Home.home,
														  LoginOwner9.loginOwner9,
														  AnnouncementsList.announcementsList,
														  ShowAnnouncement3.showAnnouncement3)
		

	setUp(readyForAddoptionScn.inject(rampUsers(1750) during (100 seconds)),
            notReadyForAddoptionScn.inject(rampUsers(1750) during (100 seconds))
            ).protocols(httpProtocol)
			 .assertions(
                 global.responseTime.max.lt(6000),
                 global.responseTime.mean.lt(1000),
                 global.successfulRequests.percent.gt(95)
             )
}