package com.github.onigokko.games;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class GameManager {

    //ゲームモードの保存
    private GameModeManager gameModeManager;

    private final GameModeFactory gameModeFactory;

    private static boolean gameStart = false;

    public GameManager(GameModeFactory gameModeFactory) {
        this.gameModeFactory = gameModeFactory;
    }

    public void setGameMode(GameMode mode) {
        setupGame(mode);
    }

    public GameModeManager getGameModeManager() {
        return gameModeManager;
    }

    //ゲームが進行中かをセット true||false
    public static void setGameStart(boolean gamestart) {
        gameStart = gamestart;
    }

    //現在のゲームの状況を返す return true||false1
    public static boolean isGameStart() {
        return gameStart;
    }

    /*
     *全プレイヤーに任意の音を再生
     * @parm sound 再生する音
     */
    public void playSoundToAllPlayer(Sound sound) {
        Bukkit.getOnlinePlayers().forEach(player ->
                player.playSound(player, sound, 1.0f, 1.0f));
    }

    private void setupGame(GameMode mode) {
        // Factory経由でゲームモードを生成
        this.gameModeManager = gameModeFactory.createGameMode(mode, this);

        if (gameModeManager != null) {
            gameModeManager.setup();
        }
    }
}
