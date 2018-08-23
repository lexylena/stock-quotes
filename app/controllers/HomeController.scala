package controllers

import javax.inject._
import actors._
import akka.actor._
import akka.stream._
import play.api.Logger
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import yahoofinance.{Stock, YahooFinance}

/**
  * This class creates the actions and the websocket needed.
  */
@Singleton
class HomeController @Inject()(implicit actorSystem: ActorSystem,
                               mat: Materializer
                              ) extends Controller {
  var currentStock: Stock = _

  // Home page that renders template
  def index = Action { implicit request =>
    if (currentStock == null) {
      Logger.info("current stock when in index = null ")
    } else {
      Logger.info("current stock when in index = "+currentStock.getSymbol)
    }
    Ok(views.html.index())
  }

  // Home page that renders template
  def getStock(stock: String) = Action { implicit request =>
    currentStock = YahooFinance.get(stock)
    Logger.info("current stock set in getStock = "+currentStock.getSymbol)
    Ok(views.html.index())
  }




  def ws: WebSocket = WebSocket.accept[String, String] { request =>
    Logger.info("WS ALIVE!")
    Logger.info("current stock being passed to actor = "+currentStock.getSymbol)
//    val stock = YahooFinance.get("intc")
    ActorFlow.actorRef(out => WebSocketActor.props(out, currentStock))
  }

}