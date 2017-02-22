import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.event.LoggingReceive
import akka.util.ByteString
import argonaut.Argonaut._
import net.sigusr.mqtt.api._

class Game(teamName: String, broker: ActorRef) extends Actor with ActorLogging {

  val baseTopic = s"player/$teamName"
  val incomingTopic = s"$baseTopic/incoming"
  val gameTopic = s"$baseTopic/game"

  broker ! Subscribe(Vector((baseTopic, AtMostOnce), (incomingTopic, AtMostOnce)), 1)
  broker ! Publish(baseTopic, ByteString(RoboChallengeMessage.register.asJson.nospaces).toVector)

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
      case m@Message(t, content) if t == baseTopic =>
        val cmd = ByteString.fromArray(content.toArray).toString.decodeOption[RoboChallengeMessage.Command]

        log.info(s"Got $m")
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