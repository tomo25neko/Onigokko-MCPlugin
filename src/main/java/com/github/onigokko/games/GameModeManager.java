package com.github.onigokko.games;

import org.bukkit.entity.Player;

public interface GameModeManager {

    void setup();

    //ゲームのスタートコマンドが入力されたときの処理
    void startGame();

    //鬼が出現した時の処理
    void releaseHunter();

    void endGame();

    void caughtPlayer(Player attacker, Player damagedPlayer);
}
