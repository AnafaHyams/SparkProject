package com.example.demo.model

import java.time.Instant

case class Event(eventId:Int,
                 eventTime:Instant,
                 eventCountry:String,
                 eventCurrencyCode:String,
                 userId:Int,
                 bet:Double,
                 gameName:String,
                 win:Double,
                 onlineTimeSecs:Int)
