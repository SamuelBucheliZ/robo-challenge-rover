import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import net.sigusr.mqtt.api.{Connect, Connected, Manager, Message, Publish, Subscribe, Subscribed}

class Broker extends Actor with ActorLogging {
  context.actorOf(Manager.props(new InetSocketAddress("127.0.0.1", 1883)), "mqtt-broker") ! Connect(self.path.name)

  var registry = Map[String, ActorRef]()

  override def receive: Receive = {
    case Connected => context become connected(sender())
  }

  def connected(mqttMgr: ActorRef): Receive = {
    case s@Subscribe(topics, _) => {
      for ((topic, _) <- topics) {
        registry = registry.updated(topic, sender())
      }
      mqttMgr ! s
    }
    case p: Publish => mqttMgr ! p
    case s@Subscribed(_, _) =>
        // TODO: send s
    case m@Message(topic, content) => {
      registry.get(topic).foreach(_ ! m)
    }
  }


}

object Broker {

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
  val system = ActorSystem(s"$teamName-system", ConfigFactory.parseString(config))

  def shutdown(): Unit = {
    system.shutdown()
  }

  def main(args: Array[String]): Unit = {
    system.actorOf(Props(classOf[Broker]), "broker")
    sys.addShutdownHook {
      shutdown()
    }
  }
}
