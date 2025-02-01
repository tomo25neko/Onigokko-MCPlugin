package com.github.onigokko.score;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

    private final Scoreboard scoreboard;
    private final Objective objective;

    private int time = 0;

    public ScoreboardManager(){
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("gameinfo", Criteria.DUMMY,"サーバー情報");

        setDefaultScoreboard();
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    private void setDefaultScoreboard() {
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.AQUA +"サーバー情報");
        //一番上に総プレイヤー数
        Score score1 = objective.getScore(ChatColor.GREEN + "総プレイヤー数: 0");
        score1.setScore(10);
        Score score2 = objective.getScore(ChatColor.GREEN + "ゲーム時間: 0秒");
        score2.setScore(9);
    }

    public void setGameMode(String gameMode) {
        objective.setDisplayName(ChatColor.AQUA + gameMode);

    }
    //keyでセットしてvalueが順番
    public void setScore(String key, int value) {
        Score score = objective.getScore(ChatColor.GREEN + key);
        score.setScore(value);
    }

    public void showScorboard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    public void removeScoreboard() {
        if (objective != null) {
            objective.unregister();  // Objectiveを削除（スコアボードも消える）
        }

        Scoreboard emptyBoard = Bukkit.getScoreboardManager().getNewScoreboard();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(emptyBoard);
        }
    }

}
