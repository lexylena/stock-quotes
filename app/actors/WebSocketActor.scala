package actors

import akka.actor.Props
import play.api.libs.json._
import scala.concurrent.duration._
import scala.concurrent.duration.Duration
import akka.actor.{Actor, ActorLogging, ActorRef, Cancellable}
import yahoofinance.{Stock, YahooFinance}
import models.StockData

object WebSocketActor {
  def props(out: ActorRef, stock: Stock): Props = Props(new WebSocketActor(out, stock))
}

class WebSocketActor(out: ActorRef, stock: Stock) extends Actor with ActorLogging {

  val tick: Cancellable = {
    context.system.scheduler.schedule(
      Duration.Zero, 15.seconds, self, QuoteUpdate)(context.system.dispatcher)
  }

  def receive = {
    case QuoteUpdate =>
      val quote = stock.getQuote(true)
      val msg = StockData(stock.getSymbol, stock.isValid, stock.getName, stock.getCurrency,
        stock.getStockExchange, quote.getPrice, quote.getPreviousClose, quote.getChange,
        quote.getChangeInPercent)
      out ! Json.toJson(msg)
  }

}

case object QuoteUpdate