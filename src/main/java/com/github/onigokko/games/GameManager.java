package com.github.onigokko.games;

import com.github.onigokko.games.mode.Fueoni;
import com.github.onigokko.games.mode.Onigo;
import com.github.onigokko.score.ScoreboardManager;
import com.github.onigokko.score.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class GameManager {

    //ゲームモードの保存
    private GameModeManager gameModeManager;

    private final TeamManager teamManager;
    private final ScoreboardManager sbManager;
    private final StartPointManager spManager;

    private static boolean gameStart = false;

    public GameManager(TeamManager teamManager, ScoreboardManager sbManager, StartPointManager spManager) {
        this.teamManager = teamManager;
        this.sbManager = sbManager;
        this.spManager = spManager;
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
        // ゲームモードに応じたクラスを生成
        switch (mode) {
            case FUEONI:
                this.gameModeManager = new Fueoni(teamManager, sbManager, spManager, this);
                break;
            case ONIGO:
                this.gameModeManager = new Onigo(teamManager, sbManager, spManager, this);
                break;
            default:
                this.gameModeManager = null;
                break;
        }

        //
        if (gameModeManager != null) {
            gameModeManager.setup();
        }
    }
}
