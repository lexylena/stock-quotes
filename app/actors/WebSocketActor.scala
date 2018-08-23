package actors

/**
  * Created by aknay on 8/6/17.
  */

import java.util.Calendar

import akka.actor.Props

import scala.concurrent.duration._
import scala.concurrent.duration.Duration
import akka.actor.{Actor, ActorLogging, ActorRef, Cancellable}
import yahoofinance.{Stock, YahooFinance}

object WebSocketActor {
  def props(out: ActorRef, stock: Stock): Props = Props(new WebSocketActor(out, stock))
}

class WebSocketActor(out: ActorRef, stock: Stock) extends Actor with ActorLogging {

  val tick: Cancellable = {
    context.system.scheduler.schedule(Duration.Zero, 15.seconds, self, SendLatestMessage)(context.system.dispatcher)
  }

  def receive = {
    case SendLatestMessage =>
      log.info("displaying")
      out ! "Displaying message from Akka at " + stock.getQuote(true).toString
    case msg: String =>
      log.info("sending I received your message: "+msg)
      out ! "I renceived your message: " + msg + " at " + Calendar.getInstance().getTime.toString
  }

}

case object SendLatestMessage