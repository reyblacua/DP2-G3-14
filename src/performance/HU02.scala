package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU02 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"DNT" -> "1",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"DNT" -> "1",
		"Origin" -> "http://www.dp2.com",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_7 = Map(
		"Accept" -> "image/webp,*/*",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"DNT" -> "1")

	val headers_9 = Map(
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft-WNS/10.0")

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(7)
	}

	object ListAnnouncements {
		val listAnnouncements = exec(http("ListAnnouncements")
			.get("/announcements")
			.headers(headers_0))
		.pause(100)
	}

	object CannotSeeAnswersOfAnnouncement {
		val cannotSeeAnswersOfAnnouncement = exec(http("CannotSeeAnswersOfAnnouncement")
			.get("/announcements/1")
			.headers(headers_0))
		.pause(9)
	}

	val scn1 = scenario("Cannot see another owner announcement answers").exec(Home.home,
										ListAnnouncements.listAnnouncements,
										CannotSeeAnswersOfAnnouncement.cannotSeeAnswersOfAnnouncement)

	val scn2 = scenario("Can see announcements").exec(Home.home,
										ListAnnouncements.listAnnouncements)

		
	setUp(
		scn1.inject(rampUsers(105000) during (10 seconds)),
		scn2.inject(rampUsers(105000) during (10 seconds))
		).protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(6000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95)
		)
		
}