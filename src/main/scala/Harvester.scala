import akka.actor.ActorSystem
import akka.event.Logging


object Harvester extends App {

  val system = ActorSystem("harvester-system")

  val log = Logging(system.eventStream, "my.nice.string")




}


