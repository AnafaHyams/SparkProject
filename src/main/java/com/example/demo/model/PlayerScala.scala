package com.example.demo.model

import scala.collection.JavaConverters._
import scala.language.implicitConversions


case class PlayerScala(id: Long, name: String, lastName: String, countryOfOrigin: String, email: String)

object PlayerAdapter{
  implicit def toProduct(player: Player): PlayerScala = {
    PlayerScala(player.getId, player.getName, player.getLastName, player.getCountryOfOrigin, player.getEmail)
  }
  implicit def toProduct1(players: java.util.List[Player]): List[PlayerScala] = {
    players.asScala.toList.map(player =>PlayerScala(player.getId, player.getName, player.getLastName, player.getCountryOfOrigin, player.getEmail))
  }

}
