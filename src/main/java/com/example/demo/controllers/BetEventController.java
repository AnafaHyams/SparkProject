package com.example.demo.controllers;

import com.example.demo.model.ResultObjects.GameStatistics;
import com.example.demo.model.ResultObjects.SuspiciousActivity;
import com.example.demo.services.BetEventsEnrichmentator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/event")
public class BetEventController {

    @Autowired
    private BetEventsEnrichmentator enrichmentator;

    private final String BET_USD_VALUE = "betUSDValue";
    private final String WIN = "win";

    @GetMapping("/show_suspicious_activity_1c")
    public String showSuspiciousActivity1C() {

        List<SuspiciousActivity> suspiciousActivities = enrichmentator.showSuspiciousActivityOfPlayersC();

        return suspiciousActivities.toString();
    }

    @GetMapping("/show_suspicious_activity_1b")
    public String showSuspiciousActivity1B() {

        List<SuspiciousActivity> suspiciousActivities = enrichmentator.showSuspiciousActivityOfPlayersB();

        return suspiciousActivities.toString();
    }

    @GetMapping("/show_suspicious_activity_1a/{startPeriodTime}/{endPeriodTime}")
    public String showSuspiciousActivity1A(@PathVariable String startPeriodTime, @PathVariable String endPeriodTime) {

        List<SuspiciousActivity> suspiciousActivities = enrichmentator.showSuspiciousActivityOfPlayersA(startPeriodTime, endPeriodTime);

        return suspiciousActivities.toString();
    }


    @GetMapping("/show_game_statistics/bet/{gameName}/{startPeriodTime}/{endPeriodTime}")
    public String showGameBetStatistics(@PathVariable String gameName, @PathVariable String startPeriodTime, @PathVariable String endPeriodTime) {

        List<GameStatistics> gameStatistics = enrichmentator.showSpecificGameSituationStatistics(BET_USD_VALUE,gameName, startPeriodTime, endPeriodTime);

        return gameStatistics.toString();
    }

    @GetMapping("/show_all_game_statistics/bet/{startPeriodTime}/{endPeriodTime}")
    public String showAllGameBetStatistics(@PathVariable String startPeriodTime, @PathVariable String endPeriodTime) {

        List<GameStatistics> gameStatistics = enrichmentator.showAllGameSituationStatistics(BET_USD_VALUE,startPeriodTime, endPeriodTime);

        return gameStatistics.toString();
    }

    @GetMapping("/show_game_statistics/win/{gameName}/{startPeriodTime}/{endPeriodTime}")
    public String showGameWinStatistics(@PathVariable String gameName, @PathVariable String startPeriodTime, @PathVariable String endPeriodTime) {

        List<GameStatistics> gameStatistics = enrichmentator.showSpecificGameSituationStatistics(WIN,gameName, startPeriodTime, endPeriodTime);

        return gameStatistics.toString();
    }

    @GetMapping("/show_all_game_statistics/win/{startPeriodTime}/{endPeriodTime}")
    public String showAllGameWinStatistics(@PathVariable String startPeriodTime, @PathVariable String endPeriodTime) {

        List<GameStatistics> gameStatistics = enrichmentator.showAllGameSituationStatistics(WIN,startPeriodTime, endPeriodTime);

        return gameStatistics.toString();
    }


    @GetMapping("/show_game_statistics/profit/{gameName}/{startPeriodTime}/{endPeriodTime}")
    public String showGameProfitStatistics(@PathVariable String gameName, @PathVariable String startPeriodTime, @PathVariable String endPeriodTime) {

        List<GameStatistics> gameStatistics = enrichmentator.showSpecificGameProfitStatistics(gameName, startPeriodTime, endPeriodTime);

        return gameStatistics.toString();
    }

    @GetMapping("/show_all_game_statistics/profit/{startPeriodTime}/{endPeriodTime}")
    public String showAllGameProfitStatistics(@PathVariable String startPeriodTime, @PathVariable String endPeriodTime) {

        List<GameStatistics> gameStatistics = enrichmentator.showAllGameProfitStatistics(startPeriodTime, endPeriodTime);

        return gameStatistics.toString();
    }
}
