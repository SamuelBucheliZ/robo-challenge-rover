import RoboChallengeMessage.Command
import org.scalatest.FlatSpec
import argonaut._, Argonaut._

class RoboChallengeMessageSpec extends FlatSpec {

   val registerComandJson = "{\"command\":\"register\",\"args\":[]}"

  "Register command" should "be parsed correctly" in {
    val parsedCommand = registerComandJson.decodeOption[Command]
    assert(parsedCommand.isDefined)
    val registerCommand = parsedCommand.get
    assert(registerCommand.command == "register")
  }

  "Register command" should "be written as JSON correctly" in {
    val registerCommand = Command("register", List())
    val json = registerCommand.asJson
    assert(json.nospaces == registerComandJson)
  }

}
