package actors

/**
  * Created by aknay on 8/6/17.
  */

import java.util.Calendar

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
    log.info("THIS IS IN CANCELLABLE IN ACTOR?")
    context.system.scheduler.schedule(
      Duration.Zero, 15.seconds, self, SendLatestMessage)(context.system.dispatcher)
  }

  def receive = {
    case SendLatestMessage =>
      log.info("displaying")
      val quote = stock.getQuote(true)
      val msg = StockData(stock.getSymbol, stock.isValid, stock.getName, stock.getCurrency,
        stock.getStockExchange, quote.getPrice, quote.getPreviousClose, quote.getChange,
        quote.getChangeInPercent)
      out ! Json.toJson(msg)
    case msg: String =>
      log.info("sending I received your message: "+msg)
      out ! Json.toJson(Calendar.getInstance().getTime.toString)
  }

}

case object SendLatestMessage