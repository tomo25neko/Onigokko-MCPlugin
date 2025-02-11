package com.github.onigokko.games;

import com.github.onigokko.score.ScoreboardManager;
import com.github.onigokko.score.TeamManager;

public class GameManager {

    //ゲームモードの保存
    private GameModeManager gameModeManager;

    private final TeamManager teamManager;
    private final ScoreboardManager sbManager;

    private boolean gameStart;

    public GameManager(TeamManager teamManager, ScoreboardManager sbManager) {
        this.teamManager = teamManager;
        this.sbManager = sbManager;
    }

    public void setGameMode(GameMode mode) {
        setupGame(mode);
    }

    public GameModeManager getGameModeManager() {
        return gameModeManager;
    }

    //ゲームが進行中かをセット true||false
    public void setGameStart(boolean gameStart) {
        this.gameStart = gameStart;
    }
    //現在のゲームの状況を返す return true||false1
    public boolean isGameStart() {
        return gameStart;
    }

    private void setupGame(GameMode mode) {
        // ゲームモードに応じたクラスを生成
        switch (mode) {
            case FUEONI:
                this.gameModeManager = new Fueoni(teamManager,sbManager);
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
