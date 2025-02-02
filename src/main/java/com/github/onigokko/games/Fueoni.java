package com.github.onigokko.games;

import com.github.onigokko.score.TeamManager;

public class Fueoni implements GameModeManager {

    private final TeamManager teamManager;

    public Fueoni(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @Override
    public void setup() {
        // 既存のチームを削除し、再作成
        teamManager.removeTeams();
        teamManager.createOniTeams("鬼");
        teamManager.createNigeTeams("逃げ");
        //スコアボードの表記を変更
        teamManager.setGameMode("増やし鬼");
    }

    @Override
    public void startGame() {

    }

    @Override
    public void endGame() {

    }

}
