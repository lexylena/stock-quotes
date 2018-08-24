package controllers

import javax.inject._
import actors._
import akka.actor._
import akka.stream._
import models.SearchText
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import scala.concurrent.ExecutionContext
import play.api.libs.json._
import yahoofinance.{YahooFinance, Stock}

/**
  * This class creates the actions and the websocket needed.
  */
@Singleton
class HomeController @Inject()(implicit actorSystem: ActorSystem,
                               mat: Materializer, ec: ExecutionContext) extends Controller {
  var currentStock: Stock = _

  // Home page that renders template
  def index = Action { implicit request =>
    if (currentStock == null) {
      Logger.info("current stock when in index = null ")
    } else {
      Logger.info("current stock when in index = "+currentStock.getSymbol)
    }
    Ok(views.html.home())
  }

  // Home page that renders template
  def getStock(stock: String) = Action { implicit request =>
    currentStock = YahooFinance.get(stock)
    Logger.info("current stock set in getStock = "+currentStock.getSymbol)
    Ok(views.html.index())
  }




  def ws: WebSocket = WebSocket.accept[JsValue, JsValue] { request =>
    Logger.info("WS ALIVE!")
    Logger.info("current stock being passed to actor = "+currentStock.getSymbol)
//    val stock = YahooFinance.get("intc")
    ActorFlow.actorRef(out => WebSocketActor.props(out, currentStock))
  }

  /**
    * The search form for the search bar.
    */
  val searchForm: Form[SearchText] = Form {
    mapping(
      "searchText" -> text
    )(SearchText.apply)(SearchText.unapply)
  }

  /**
    * Retrieve the search text from the form and redirect to display the stock
    */
  def searchStock = Action { implicit request: Request[AnyContent] =>
    val searchText = searchForm.bindFromRequest.get
    Logger.debug("getStock() stock = "+searchText)
    currentStock = YahooFinance.get(searchText.searchText)
    Redirect(routes.HomeController.getStock(searchText.searchText))
  }

}