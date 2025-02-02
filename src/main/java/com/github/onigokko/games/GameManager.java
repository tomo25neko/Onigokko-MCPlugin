package com.github.onigokko.games;

import com.github.onigokko.score.ScoreboardManager;
import com.github.onigokko.score.TeamManager;

public class GameManager {

    //ゲーム状態保存
    private GameModeManager gameModeManager;

    private final TeamManager teamManager;
    private final ScoreboardManager sbManager;

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


    private void setupGame(GameMode mode) {
        // ゲームモードに応じたクラスを生成
        switch (mode) {
            case FUEONI:
                this.gameModeManager = new Fueoni(teamManager);
                break;
            default:
                this.gameModeManager = null;
                break;
        }

        if (gameModeManager != null) {
            sbManager.setGameMode(mode.getDisplayName());
            gameModeManager.setup();
        }
    }
}
