package com.example.demo.model.ResultObjects

import java.time.Instant

case class SuspiciousActivity(eventId: Int,
                              eventTime:Instant,
                              eventCountry:String,
                              gameName:String,
                              win:Double,
                              onlineTimeSecs:Int,
                              playerId:Long,
                              firstName:String,
                              lastName:String,
                              countryOfOrigin:String,
                              email:String,
                              currencyCode:String,
                              betUSDValue:Double)
