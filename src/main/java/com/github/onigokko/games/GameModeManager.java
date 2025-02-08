package com.github.onigokko.games;

import org.bukkit.entity.Player;

public interface GameModeManager {

    void setup();

    void startGame();

    void endGame();

    void sendMessage(Player attacker, Player damagedPlayer);
}
