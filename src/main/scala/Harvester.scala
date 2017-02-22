import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging
import com.sandinh.paho.akka._

import scala.concurrent.duration._

/**
  * Created by lla on 22.02.17.
  */
object Harvester extends App {

  val system = ActorSystem("harvester-system")

  val log = Logging(system.eventStream, "my.nice.string")




}


