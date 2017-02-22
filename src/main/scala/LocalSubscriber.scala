
import java.net.InetSocketAddress

import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
import com.typesafe.config.ConfigFactory
import net.sigusr.mqtt.api._

class LocalSubscriber(topics: Vector[String]) extends Actor {

  val localSubscriber = "test"

  val stopTopic: String = s"$localSubscriber/stop"

  context.actorOf(Manager.props(new InetSocketAddress("127.0.0.1", 1883))) ! Connect(localSubscriber)

  def receive: Receive = {
    case Connected ⇒
      println("Successfully connected to localhost:1883")
      sender() ! Subscribe((stopTopic +: topics) zip Vector.fill(topics.length + 1) { AtMostOnce }, 1)
      context become ready(sender())
    case ConnectionFailure(reason) ⇒
      println(s"Connection to localhost:1883 failed [$reason]")
  }

  def ready(mqttManager: ActorRef): Receive = {
    case Subscribed(vQoS, MessageId(1)) ⇒
      println("Successfully subscribed to topics:")
      println(topics.mkString(" ", ",\n ", ""))
    case Message(`stopTopic`, _) ⇒
      mqttManager ! Disconnect
      context become disconnecting
    case Message(topic, payload) ⇒
      val message = new String(payload.to[Array], "UTF-8")
      println(s"[$topic] $message")
  }

  def disconnecting(): Receive = {
    case Disconnected ⇒
      println("Disconnected from localhost:1883")
      LocalSubscriber.shutdown()
  }
}

object LocalSubscriber {

  val localSubscriber = "test"

  val config =
    """akka {
         loglevel = INFO
         actor {
            debug {
              receive = off
              autoreceive = off
              lifecycle = off
            }
         }
       }
    """
  val system = ActorSystem(localSubscriber, ConfigFactory.parseString(config))

  def shutdown(): Unit = {
    system.shutdown()
    println(s"<$localSubscriber> stopped")
  }

  def main(args: Array[String]) = {
    system.actorOf(Props(classOf[LocalSubscriber], args.to[Vector]))
    sys.addShutdownHook { shutdown() }
    println(s"<$localSubscriber> started")
  }
}
