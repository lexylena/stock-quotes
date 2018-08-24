# stock-quotes
This is a simple web app for displaying stock quotes in real time using the [Yahoo Finance API](http://financequotes-api.com).

## Running
Run this using [sbt](https://www.scala-sbt.org/). It requires [JDK1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or higher.
Use the command: 
<br/>`sbt run`
<br/>then go to http://localhost:9000 in your browser.

## Credits
This project was created using the Play Framework using Websocket and Akka tutorial found [here](https://aknay.github.io/2017/06/08/how-to-build-a-simple-reactive-web-page-with-play-framework-using-websocket-and-akka.html) and its corresponding [repository](https://github.com/aknay/scala-play-websocket-akka).
<br/><br/>The [Play Scala Websocket example](https://github.com/playframework/play-scala-websocket-example) was also used as guidance. 