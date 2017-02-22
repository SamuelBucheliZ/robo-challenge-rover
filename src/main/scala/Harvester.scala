import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.event.LoggingReceive
import akka.util.ByteString
import argonaut.Argonaut._
import com.typesafe.config.ConfigFactory
import net.sigusr.mqtt.api._

class Game(teamName: String) extends Actor with ActorLogging {

  context.actorOf(Manager.props(new InetSocketAddress("127.0.0.1", 1883)), "mqtt-broker") ! Connect(self.path.name)

  val baseTopic = s"player/$teamName"
  val incomingTopic = s"$baseTopic/incoming"
  val gameTopic = s"$baseTopic/game"

  override def receive: Receive = {
    case Connected => context become registering(sender())
  }


  def registering(mqttMgr: ActorRef): Receive = {
    log.info("Subscribing")
    mqttMgr ! Subscribe(Vector((baseTopic, AtMostOnce), (incomingTopic, AtMostOnce)), 1)
    mqttMgr ! Publish(baseTopic, ByteString(RoboChallengeMessage.register.asJson.nospaces).toVector)

    LoggingReceive {
      case Subscribed(topics, MessageId(1)) =>
        log.info(s"Subscribed to $topics")
      case Message(t, content) if t == baseTopic =>
        val cmd = ByteString.fromArray(content.toArray).toString.decodeOption[RoboChallengeMessage.Command]

        log.info(s"Got $cmd")
        context become registered()
    }
  }

  def registered(): Receive = {
    val robot = context.actorOf(Props(new Robot(ActorRef.noSender)))

    LoggingReceive {
      case Message(t, content) if t == baseTopic =>
      case Message(t, content) if t == incomingTopic =>
      case Message(t, content) if t == gameTopic =>

    }
  }
}


object Harvester {

  val config =
    """akka {
         loglevel = DEBUG
         actor {
            debug {
              receive = off
              autoreceive = off
              lifecycle = off
            }
         }
       }
    """


  val teamName = "akkaers"
  val system = ActorSystem(s"$teamName-system", ConfigFactory.parseString(config))

  def shutdown(): Unit = {
    system.shutdown()
  }

  def main(args: Array[String]): Unit = {
    system.actorOf(Props(new Game(teamName)))
    sys.addShutdownHook {
      shutdown()
    }
  }
}


