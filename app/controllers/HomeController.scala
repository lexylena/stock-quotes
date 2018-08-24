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

  // keep track of the current stock to pass to actor
  var currentStock: Stock = _

  /**
    * Renders home page template
    */
  def index = Action { implicit request =>
    Ok(views.html.home())
  }

  /**
    * Gets stock with matching name or symbol from Yahoo finance API
    * and updates currentStock. Renders index template, triggering
    * the websocket connection.
    * @param stock the stock symbol to search for
    */
  def getStock(stock: String) = Action { implicit request =>
    currentStock = YahooFinance.get(stock)
    Ok(views.html.index())
  }

  /**
    * Create a new websocket connection for the current stock
    */
  def ws: WebSocket = WebSocket.accept[JsValue, JsValue] { request =>
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
    currentStock = YahooFinance.get(searchText.searchText)
    Redirect(routes.HomeController.getStock(searchText.searchText))
  }

}