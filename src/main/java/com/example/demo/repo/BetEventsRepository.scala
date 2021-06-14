package com.example.demo.repo

import com.example.demo.model.Event
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Dataset, Encoder, Encoders, Row, SparkSession}
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class BetEventsRepository(sparkSession: SparkSession) {

  @Value("${events_file_source}")
  private val fileSourcePath: String = ""

  def readEvents(): Dataset[Row] = {

    val eventEncoder: Encoder[Event] = Encoders.product[Event]
    val schema: StructType = eventEncoder.schema
    //sparkSession.read.option("multiline", "true").schema(schema).json("data/generated_event.json")
    sparkSession.read.option("multiline", "true").schema(schema).json(fileSourcePath)
  }
}
