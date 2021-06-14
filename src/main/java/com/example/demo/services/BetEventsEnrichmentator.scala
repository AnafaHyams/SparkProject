package com.example.demo.services

import com.example.demo.model.Player
import com.example.demo.repo.BetEventsRepository
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, Encoders, Row, SparkSession}
import org.springframework.stereotype.Service
import com.example.demo.model.PlayerAdapter._
import com.example.demo.model.ResultObjects.{GameStatistics, SuspiciousActivity}
import org.apache.spark.sql

import java.util

@Service
class BetEventsEnrichmentator (@transient playerService: PlayerService, @transient eventsRepository: BetEventsRepository, @transient sparkSession: SparkSession) extends Serializable {

  final val ID: String = "id"
  final val PLAYER_ID: String = "playerId"
  final val USER_ID: String = "userId"
  final val NAME: String = "name"
  final val FIRST_NAME: String = "firstName"
  final val GAME_NAME: String = "gameName"
  final val DEMO: String = "-demo"
  final val COUNT: String = "count"
  final val COUNTRY_OF_ORIGIN: String = "countryOfOrigin"
  final val CURRENCY_CODE: String = "currencyCode"
  final val EVENT_CURRENCY_CODE: String = "eventCurrencyCode"
  final val EVENT_TIME: String = "eventTime"
  final val EVENT_COUNTRY: String = "eventCountry"
  final val USA: String = "USA"
  final val EUR: String = "EUR"
  final val USD: String = "USD"
  final val BET: String = "bet"
  final val BET_USD_VALUE: String = "betUSDValue"
  final val WIN: String = "win"
  final val PROFIT: String = "profit"
  final val ONLINE_TIME_SECS: String = "onlineTimeSecs"
  final val FIVE_HOURS_IN_SEC: Float = 5 * 3600
  final val ONE_TO_TEN: Int = 1 / 10
  final val CONVERSION_RATE: Float = 1.1f
  final val AVG: String = "avg"
  final val MAX: String = "max"
  final val MIN: String = "min"

  val statisticEncoder: sql.Encoder[GameStatistics] = Encoders.product[GameStatistics]
  val suspiciousActivityEncoder: sql.Encoder[SuspiciousActivity] = Encoders.product[SuspiciousActivity]

  val eventsDf = eventsRepository.readEvents()
  val players: java.util.List[Player] = playerService.getAllPlayers()

  val playersDf = sparkSession.createDataFrame(players)
    .withColumnRenamed(ID, PLAYER_ID)
    .withColumnRenamed(NAME, FIRST_NAME)

  val frame: DataFrame = eventsDf.join((playersDf), playersDf(PLAYER_ID) === eventsDf(USER_ID))
    .drop(USER_ID)


  val allPlayerEventsRequiredDF: DataFrame = frame.filter(not(col(GAME_NAME).contains(DEMO).and(col(COUNTRY_OF_ORIGIN).equalTo(USA))))
    .withColumn(CURRENCY_CODE, when(col(EVENT_CURRENCY_CODE).equalTo(EUR), lit(USD)).otherwise(col(EVENT_CURRENCY_CODE)))
    .withColumn(BET_USD_VALUE, when(col(EVENT_CURRENCY_CODE).equalTo(EUR), (col(BET).multiply(CONVERSION_RATE))).otherwise(col(BET)))
    .drop(EVENT_CURRENCY_CODE)
    .drop(BET)


  // more than 5 hours solution
  def showSuspiciousActivityOfPlayersC(): util.List[SuspiciousActivity] = {

    val suspiciousActivityC: Dataset[Row] = allPlayerEventsRequiredDF.filter(col(ONLINE_TIME_SECS) >= FIVE_HOURS_IN_SEC)

    //println("There are: " + suspiciousActivityC.count() + " players event with more than 5 hours online game")

    suspiciousActivityC.show(false)
    suspiciousActivityC.as[SuspiciousActivity](suspiciousActivityEncoder).collectAsList()
  }


  // show all events-players where Win/Bet ratio is higher than 1/10
  def showSuspiciousActivityOfPlayersB(): util.List[SuspiciousActivity] = {

    val suspiciousActivityB: Dataset[Row] = allPlayerEventsRequiredDF.filter(col(WIN).divide(col(BET_USD_VALUE)) >= ONE_TO_TEN)

    //println("There are: " + suspiciousActivityB.count() + " players event with " + WIN + "/bet higher than 1/10")

    suspiciousActivityB.show(false)
    suspiciousActivityB.as[SuspiciousActivity](suspiciousActivityEncoder).collectAsList()
  }


  // User/Player made bets from different countries in the provided time period.
  def showSuspiciousActivityOfPlayersA(startPeriodTime: String, endPeriodTime:String): util.List[SuspiciousActivity] = {

    val frame1 = allPlayerEventsRequiredDF.filter(col(EVENT_TIME).between(startPeriodTime, endPeriodTime))
      .groupBy(col(PLAYER_ID))
      .agg(countDistinct(col(EVENT_COUNTRY)).alias(COUNT))
      .where(col(COUNT).gt(1))
      .drop(col(COUNT))

    val suspiciousActivityA: Dataset[Row] = allPlayerEventsRequiredDF.filter(col(EVENT_TIME).between(startPeriodTime, endPeriodTime))
      .filter(col(PLAYER_ID).isin(frame1.col(PLAYER_ID)))
      .orderBy(col(PLAYER_ID), col(EVENT_TIME))

    suspiciousActivityA.show(false)
    suspiciousActivityA.as[SuspiciousActivity](suspiciousActivityEncoder).collectAsList()
  }


  // 2c.	Calculate average, max and min bet for requested game in time period.
  def showSpecificGameProfitStatistics(specificGameName: String, startPeriodTime: String, endPeriodTime:String): util.List[GameStatistics] = {
    val gameProfitStatisticsDF: DataFrame = allPlayerEventsRequiredDF.filter(col(EVENT_TIME).between(startPeriodTime, endPeriodTime))
      .filter(col(GAME_NAME).equalTo(specificGameName))
      .withColumn(PROFIT, col(WIN).minus(col(BET_USD_VALUE)))
      .select(col(GAME_NAME), col(PROFIT))
      .groupBy(col(GAME_NAME))
      .agg(avg(col(PROFIT)).alias(AVG), max(col(PROFIT)).alias(MAX), min(col(PROFIT)).alias(MIN))

    gameProfitStatisticsDF.show(false)
    gameProfitStatisticsDF.as[GameStatistics](statisticEncoder).collectAsList()
  }

  // 3c.	Calculate average, max and min bet for requested game in time period.
  def showAllGameProfitStatistics(startPeriodTime: String, endPeriodTime:String): util.List[GameStatistics] = {

    val allGameProfitStatistics: DataFrame = allPlayerEventsRequiredDF.filter(col(EVENT_TIME).between(startPeriodTime, endPeriodTime))
      .withColumn(PROFIT, col(WIN).minus(col(BET_USD_VALUE)))
      .select(col(GAME_NAME), col(PROFIT))
      .groupBy(col(GAME_NAME))
      .agg(avg(col(PROFIT)).alias(AVG), max(col(PROFIT)).alias(MAX), min(col(PROFIT)).alias(MIN))

    allGameProfitStatistics.show(false)
    allGameProfitStatistics.as[GameStatistics](statisticEncoder).collectAsList()
  }


  // 2a.	Calculate average, max and min bet for requested game in time period.
  // 3a.	Calculate average, max and min bet for requested game in time period.
  def showSpecificGameSituationStatistics(situation: String ,specificGameName: String, startPeriodTime: String, endPeriodTime:String): util.List[GameStatistics] = {

    val gameWinStatistics: DataFrame = allPlayerEventsRequiredDF.filter(col(EVENT_TIME).between(startPeriodTime, endPeriodTime))
      .filter(col(GAME_NAME).equalTo(specificGameName))
      .select(col(GAME_NAME), col(situation))
      .groupBy(col(GAME_NAME))
      .agg(avg(col(situation)).alias(AVG), max(col(situation)).alias(MAX), min(col(situation)).alias(MIN))

    gameWinStatistics.show(false)
    gameWinStatistics.as[GameStatistics](statisticEncoder).collectAsList()
  }

  // 2b.	Calculate average, max and min bet for requested game in time period.
  // 3b.	Calculate average, max and min bet for requested game in time period.
  def showAllGameSituationStatistics(situation: String, startPeriodTime: String, endPeriodTime:String): util.List[GameStatistics] = {

    val allGamesWinStatistics: DataFrame = allPlayerEventsRequiredDF.filter(col(EVENT_TIME).between(startPeriodTime, endPeriodTime))
      .select(col(GAME_NAME), col(situation))
      .groupBy(col(GAME_NAME))
      .agg(avg(col(situation)).alias(AVG), max(col(situation)).alias(MAX), min(col(situation)).alias(MIN))

    allGamesWinStatistics.show(false)
    allGamesWinStatistics.as[GameStatistics](statisticEncoder).collectAsList()
  }
}
