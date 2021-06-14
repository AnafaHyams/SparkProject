package com.example.demo.model;

import com.example.demo.model.enums.Country;
import com.example.demo.model.enums.CurrencyCode;
import com.example.demo.model.enums.GameName;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BetEvent implements Serializable {
    private int eventId;
    private LocalDateTime eventTime;
    private String country;
    private String currencyCode;
    private int userId;
    private double bet;
    private String gameName;
    private double win;
    private int onlineTimeSecs;

}
