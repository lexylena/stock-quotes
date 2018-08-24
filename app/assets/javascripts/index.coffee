$ ->
  ws = new WebSocket $("body").data("ws-url")

  ws.onmessage = (event) ->
    message = JSON.parse event.data
    display(message)

  display = (message) ->
    # assign each value in json message to corresponding html element
    stockDetails = message.symbol + " | Stock Exchange: " + message.stockExchange + " | Currency: " + message.currency

    $("#stock-name").text(message.name)
    $("#stock-details").text(stockDetails)
    $("#price").text(message.quotePrice)
    $("#start-price").text(message.prevClose)

    # change `#change` and `#change-percent` elements' text to green or red based on numeric value
    if message.change < 0
        $("#change").text(message.change)
        $("#change").css("color", "red")
    if message.change > 0
        $("#change").text("+"+message.change)
        $("#change").css("color", "green")


    if message.changePercent < 0
        $("#change-percent").text(message.changePercent+"%")
        $("#change-percent").css("color", "red")

    if message.changePercent > 0
        $("#change-percent").text("+"+message.changePercent+"%")
        $("#change-percent").css("color", "green")

