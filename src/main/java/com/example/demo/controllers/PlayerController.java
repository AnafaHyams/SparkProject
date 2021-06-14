package com.example.demo.controllers;

import com.example.demo.model.Player;
import com.example.demo.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/player")
public class PlayerController {
    @Autowired
    private PlayerService playerService;


    @GetMapping("/index")
    public String index() {

        List<Player> allPlayers = playerService.getAllPlayers();

        return allPlayers.toString();
    }

}
