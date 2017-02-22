import argonaut._, Argonaut._

object RoboChallengeMessage {

  case class Command(command: String, args: List[Int])

  object Command {
    implicit def CommmandCodecJson: CodecJson[Command] =
      casecodec2(Command.apply, Command.unapply)("command", "args")
  }

  def command(command: String, value: Int) = Command(command, List(value))

  def command(command: String) = Command(command, List())

  val register = command("register")
  val start = command("start")

  def forward(value: Int) = command("forward", value)

  def backward(value: Int) = command("backward", value)

  def left(value: Int) = command("left", value)

  def right(value: Int) = command("right", value)

  val stop = command("stop")
  val reset = command("reset")

  case class RobotPosition(r: Int, x: Int, y: Int)

  object RobotPosition {
    implicit def RobotPositionCodecJson: CodecJson[RobotPosition] =
      casecodec3(RobotPosition.apply, RobotPosition.unapply)("r", "x", "y")
  }

  case class WorldDimension(y_max: Int, x_max: Int)

  object WorldDimension {
    implicit def WorldDimensionCodecJson: CodecJson[WorldDimension] =
      casecodec2(WorldDimension.apply, WorldDimension.unapply)("y_max", "x_max")
  }

  case class Point(collected: Boolean, x: Int, y: Int, score: Int) {
    def isCrater = score == -1
  }

  object Point {
    implicit def PointCodecJson: CodecJson[Point] =
      casecodec4(Point.apply, Point.unapply)("collected", "x", "y", "score")
  }

  case class GameState(robot: RobotPosition, world: WorldDimension, points: List[Point])

  object GameState {
    implicit def GameStateCodecJson: CodecJson[GameState] =
      casecodec3(GameState.apply, GameState.unapply)("robot", "world", "points")
  }

  case class HardwareState(angle: Int, left_motor: Int, right_motor: Int)

  object HardwareState {
    implicit def HardwareStateCodecJson: CodecJson[HardwareState] =
      casecodec3(HardwareState.apply, HardwareState.unapply)("angle", "left_motor", "right_motor")
  }

}
