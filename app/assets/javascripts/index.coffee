$ ->
  ws = new WebSocket $("body").data("ws-url")

  ws.onmessage = (event) ->
    message = JSON.parse event.data
    display(message)
    console.log(message)

  ws.onopen = (event) ->
    console.log("opened")

  ws.onclose = (event) ->
    console.log("close")

  ws.onerror = (event) ->
    console.log("error")

  display = (message) ->
    $("#message_holder").text(message)
    stockDetails = message.symbol + " | Stock Exchange: " + message.stockExchange + " | Currency: " + message.currency

    $("#stock-name").text(message.name)
    $("#stock-details").text(stockDetails)
    $("#price").text(message.quotePrice)
    $("#start-price").text(message.prevClose)

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

