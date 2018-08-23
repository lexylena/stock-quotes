package controllers

import javax.inject._
import actors._
import akka.actor._
import akka.stream._
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import yahoofinance.YahooFinance

/**
  * This class creates the actions and the websocket needed.
  */
@Singleton
class HomeController @Inject()(implicit actorSystem: ActorSystem,
                               mat: Materializer
                              ) extends Controller {

  // Home page that renders template
  def index = Action { implicit request =>
    Ok(views.html.index())
  }




  def ws: WebSocket = WebSocket.accept[String, String] { request =>
    println("WS ALIVE!")
    val stock = YahooFinance.get("intc")
    ActorFlow.actorRef(out => WebSocketActor.props(out, stock))
  }

}