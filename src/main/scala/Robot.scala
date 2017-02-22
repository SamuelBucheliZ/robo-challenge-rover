import akka.actor.{Actor, ActorRef}
import akka.actor.Actor.Receive

class Robot(mqttMgr: ActorRef) extends  Actor {
  override def receive: Receive = ???
}
