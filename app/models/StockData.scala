package models

import play.api.libs.json._

case class StockData(symbol: String, isValid: Boolean, name: String,
                     currency: String, stockExchange: String,
                     quotePrice: BigDecimal, prevClose: BigDecimal,
                     change: BigDecimal, changePercent: BigDecimal)

// for serialization of stock
object StockData {
    implicit val stockDataWrites: Writes[StockData] = new Writes[StockData] {
      def writes(stockData: StockData) = Json.obj(
        "symbol" -> stockData.symbol,
        "isValid" -> stockData.isValid,
        "name" -> stockData.name,
        "currency" -> stockData.currency,
        "stockExchange" -> stockData.stockExchange,
        "quotePrice" -> stockData.quotePrice,
        "prevClose" -> stockData.prevClose,
        "change" -> stockData.change,
        "changePercent" -> stockData.changePercent
      )
    }
}