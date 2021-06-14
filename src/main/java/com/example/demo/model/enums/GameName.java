package com.example.demo.model.enums;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
public enum GameName implements Serializable {
    BACCARAT("baccarat"),
    POKER("poker"),
    BLACKJACK("blackjack"),
    CANASTA("canasta"),
    CRIBBAGE("cribbage"),
    FARO("faro"),
    MONTE("monte"),
    RUMMY("rummy"),
    WHIST("whist"),
    BACCARAT_DEMO("baccarat-demo"),
    POKER_DEMO("poker-demo"),
    BLACKJACK_DEMO("blackjack-demo"),
    CANASTA_DEMO("canasta-demo"),
    CRIBBAGE_DEMO("cribbage-demo"),
    FARO_DEMO("faro-demo"),
    MONTE_DEMO("monte-demo"),
    RUMMY_DEMO("rummy-demo"),
    WHIST_DEMO("whist-demo");

    private final String name;
}
