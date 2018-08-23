package models

case class StockData(symbol: String, name: String, currency: String,
                     stockExchange: String, quotePrice: BigDecimal,
                     roe: BigDecimal, isValid: Boolean, yahooStock: yahoofinance.Stock)

//case class Stock(symbol: String, price: BigDecimal)

// for serialization of stock
//object StockData {
//  implicit val stockDataFormat = Json.format[StockData]
//}