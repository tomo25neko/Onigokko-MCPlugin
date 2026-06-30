package com.github.onigokko.games;

import com.github.onigokko.games.mode.Fueoni;
import com.github.onigokko.games.mode.Onigo;
import com.github.onigokko.score.ScoreboardManager;
import com.github.onigokko.score.TeamManager;

/**
 * ゲームモードの生成を担当するFactoryクラス
 * 新しいゲームモードを追加する場合は、このクラスにケースを追加する
 */
public record GameModeFactory(TeamManager teamManager, ScoreboardManager sbManager, StartPointManager spManager) {

    /**
     * 指定されたゲームモードに対応するGameModeManagerを生成する
     *
     * @param mode        ゲームモード
     * @param gameManager GameManagerインスタンス
     * @return GameModeManagerインスタンス、該当しない場合はnull
     */
    public GameModeManager createGameMode(GameMode mode, GameManager gameManager) {
        return switch (mode) {
            case FUEONI -> new Fueoni(teamManager, sbManager, spManager, gameManager);
            case ONIGO -> new Onigo(teamManager, sbManager, spManager, gameManager);
        };
    }
}
