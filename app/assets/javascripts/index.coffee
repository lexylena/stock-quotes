$ ->
  ws = new WebSocket $("body").data("ws-url")

  ws.onmessage = (event) ->
    message = event.data
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