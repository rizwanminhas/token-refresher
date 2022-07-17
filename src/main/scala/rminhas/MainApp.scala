package rminhas

import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.AskPattern.Askable
import rminhas.actors.TokenActor
import rminhas.actors.TokenActor.{GetToken, PrintToken, RefreshToken}
import akka.actor.typed.{Scheduler, ActorSystem => TypedActorSystem}
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

object MainApp {
  def main(args: Array[String]): Unit = {
    implicit val rootActorSystem: ActorSystem = ActorSystem()

    val tokenActor = TypedActorSystem(TokenActor(), "TokenActor")

    rootActorSystem.scheduler.scheduleWithFixedDelay(0.seconds, 3.seconds) { () =>
      tokenActor ! RefreshToken
    }(rootActorSystem.dispatcher)

    rootActorSystem.scheduler.scheduleWithFixedDelay(0.seconds, 300.millisecond) { () =>
      tokenActor ! PrintToken

      implicit val scheduler: Scheduler = tokenActor.scheduler // needed for ask
      implicit val timeout: Timeout = Timeout(3.seconds) // needed for ask
      val future = tokenActor ? GetToken
      future.map(token => {
        println(s"Main received token: $token")
      })
    }(rootActorSystem.dispatcher)
  }
}
