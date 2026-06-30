package com.github.onigokko.games;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class GameManager {

    private static boolean gameStart = false;
    private final GameModeFactory gameModeFactory;
    //ゲームモードの保存
    private GameModeManager gameModeManager;

    public GameManager(GameModeFactory gameModeFactory) {
        this.gameModeFactory = gameModeFactory;
    }

    //現在のゲームの状況を返す return true||false1
    public static boolean isGameStart() {
        return gameStart;
    }

    //ゲームが進行中かをセット true||false
    public static void setGameStart(boolean set) {
        gameStart = set;
    }

    public void setGameMode(GameMode mode) {
        setupGame(mode);
    }

    public GameModeManager getGameModeManager() {
        return gameModeManager;
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

        gameModeManager.setup();
    }
}
