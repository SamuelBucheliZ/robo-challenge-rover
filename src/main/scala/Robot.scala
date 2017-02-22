
import akka.actor.{Actor, ActorRef}
import net.sigusr.mqtt.api.{AtMostOnce, Message, MessageId, Subscribe, Subscribed}

class Robot(manager: ActorRef) extends Actor {

  val localSubscriber = "test"

  val topics = Vector("robot/state", "robot/done", "robot/error")

  println(s"Got $manager")

  manager ! Subscribe(topics zip Vector.fill(topics.length) {
    AtMostOnce
  }, 1)

  def receive: Receive = {
    case Subscribed(vQoS, MessageId(1)) ⇒
      println("Successfully subscribed to topics:")
      println(topics.mkString(" ", ",\n ", ""))
    case Message(topic, payload) ⇒
      val message = new String(payload.to[Array], "UTF-8")
      println(s"[$topic] $message")
  }

}
