package rminhas.actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.util.Random

object TokenActor {
  sealed trait TokenActorMessage

  final case object RefreshToken extends TokenActorMessage

  final case object PrintToken extends TokenActorMessage

  final case class GetToken(replyTo: ActorRef[String]) extends TokenActorMessage

  def generateToken: String =
    Random.alphanumeric.take(10).mkString

  def apply(token: String = generateToken): Behavior[TokenActorMessage] =
    Behaviors.receiveMessage {
      case RefreshToken =>
        val t: String = generateToken
        println(s"Token refreshed: $t")
        apply(t)
      case PrintToken =>
        println(s"Token state in actor: $token")
        apply(token)
      case GetToken(replyTo) =>
        replyTo ! token
        Behaviors.same
    }
}