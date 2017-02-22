import java.net.InetSocketAddress

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import net.sigusr.mqtt.api._


class Game(teamName: String, mqttMgr: ActorRef) extends  Actor with ActorLogging {
  val baseTopic = s"player/$teamName"
  val incomingTopic = s"$baseTopic/incoming"
  val gameTopic = s"$baseTopic/game"

  log.info("Subscribing")
  mqttMgr ! Subscribe(Vector((baseTopic, AtMostOnce), (incomingTopic, AtMostOnce)), 0)

  val robot = context.actorOf(Props(new Robot(mqttMgr)))


  override def receive: Receive = {
    case Subscribed(topics, _) =>
      log.info(s"Subscribed to $topics")
      context become registered()
  }


  def registered():Receive = {

    {
      case "start" =>

    }
  }
}


class Harvester(teamName: String) extends Actor with ActorLogging {

  val mqttBroker = context.actorOf(Manager.props(new InetSocketAddress("127.0.0.1", 1883)), "mqtt-broker")

  mqttBroker ! net.sigusr.mqtt.api.Connect("Harvester")

  def receive: Receive = {
    case Connected ⇒
      log.info("Successfully connected")
      context become connected(sender())
    case ConnectionFailure(reason) ⇒
      println(s"Connection to localhost:1883 failed [$reason]")
  }


  def connected(mqttMgr: ActorRef): Receive = {
    val game = context.actorOf(Props(new Game(teamName, mqttMgr)))

    {
      case Disconnected =>
    }
  }
}

object Harvester {

  val config =
    """akka {
         loglevel = INFO
         actor {
            debug {
              receive = on
              autoreceive = off
              lifecycle = off
            }
         }
       }
    """


  val teamName = "akkaers"
  val system = ActorSystem(s"$teamName-system" , ConfigFactory.parseString(config))

  def shutdown(): Unit = {
    system.shutdown()
  }

  def main(args: Array[String]):Unit = {
    system.actorOf(Props(classOf[Harvester], teamName), "harvester")
    sys.addShutdownHook {
      shutdown()
    }
  }
}


