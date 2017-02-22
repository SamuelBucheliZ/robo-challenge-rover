import RoboChallengeMessage._
import org.scalatest.FlatSpec
import argonaut._
import Argonaut._

class RoboChallengeMessageSpec extends FlatSpec {

  val registerCommandJson = "{\"command\":\"register\",\"args\":[]}"
  val forwardCommandJson = "{\"command\":\"forward\",\"args\":[100]}"

  val gameStateJson = "{\"robot\":{\"r\":15,\"x\":920.0,\"y\": 750.0},\"world\":{\"y_max\":960,\"x_max\": 1920},\"points\":[{\"collected\":false,\"r\":5,\"x\":908,\"y\":831,\"score\":1}]}"

  "Register command" should "be parsed correctly" in {
    val parsedCommand = registerCommandJson.decodeOption[Command]
    assert(parsedCommand.isDefined)
    val registerCommand = parsedCommand.get
    assert(registerCommand.command == "register")
  }

  "Register command" should "be written as JSON correctly" in {
    val registerCommand = RoboChallengeMessage.register
    val json = registerCommand.asJson
    assert(json.nospaces == registerCommandJson)
  }

  "Forward command" should "be parsed correctly" in {
    val parsedCommand = forwardCommandJson.decodeOption[Command]
    assert(parsedCommand.isDefined)
    val forwardCommand = parsedCommand.get
    assert(forwardCommand == RoboChallengeMessage.forward(100))
  }

  "Forward command" should "be written as JSON correctly" in {
    val forwardCommand = RoboChallengeMessage.forward(100)
    val json = forwardCommand.asJson
    assert(json.nospaces == forwardCommandJson)
  }

  "Game state" should "be parsed correctly" in {
    val parsedState = gameStateJson.decodeOption[GameState]
    assert(parsedState.isDefined)
    val gameState = parsedState.get
    assert(gameState == GameState(RobotPosition(15, 920, 750), WorldDimension(960, 1920), List(Point(false, 5, 908, 831, 1))))
  }

}
