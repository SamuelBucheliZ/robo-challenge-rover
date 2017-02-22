object RoboChallengeMessage {
  case class Command(command: String, args: Vector[Any])

  def gameCommand(command: String) = Command(command, Vector())
  val register = gameCommand("register")
  val start = gameCommand("start")

  case class RobotPosition(r: Int, x: Int, y: Int)
  case class WorldDimension(y_max: Int, x_max: Int)
  case class Point(collected: Boolean, x: Int, y: Int, score: Int) {
    def isCrater = score == -1
  }
  case class GameState(robot: RobotPosition, world: WorldDimension, points: Vector[Point])

  case class HardwareState(angle: Int, left_motor: Int, right_motor: Int)

  def robotCommand(command: String, optionalValue: Option[Int]) = optionalValue match {
    case Some(value) => Command(command, Vector(value))
    case None => Command(command, Vector())
  }
  def robotCommand(command: String, value: Int) = robotCommand(command, Some(value))
  def robotCommand(command: String) = robotCommand(command, None)

  def forward(value: Int) = robotCommand("forward", value)
  def backward(value: Int) = robotCommand("backward", value)
  def left(value: Int) = robotCommand("left", value)
  def right(value: Int) = robotCommand("right", value)
  val stop = robotCommand("stop")
  val reset = robotCommand("reset")

}
