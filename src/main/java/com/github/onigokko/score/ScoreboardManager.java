package com.github.onigokko.score;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

    private final Scoreboard scoreboard;
    private final Objective objective;

    public ScoreboardManager(){
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("gameinfo", Criteria.DUMMY,"サーバー情報");

        setDefaultScoreboard();
    }

    //他のクラスが使うためのスコアボードを渡す
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    //初期スコアボードの作成
    private void setDefaultScoreboard() {
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.AQUA +"サーバー情報");
    }

    //スコアボードの一番上に出る名称をgameModeに変更
    public void setGameMode(String gameMode) {
        objective.setDisplayName(ChatColor.AQUA + gameMode);

    }

    //スコアボードに登録　keyでセットしてvalueが値
    public void setScore(String key, int amount,int value) {
        if (key.contains("%d")) {
            key = String.format(key, amount);
        }
        String entry = ChatColor.GREEN + key;

        // 変更前のスコアを削除（プレフィックスが一致するものを削除）
        for (String existingEntry : scoreboard.getEntries()) {
            if (existingEntry.startsWith(ChatColor.GREEN + key.replace("%d", ""))) {
                scoreboard.resetScores(existingEntry);
            }
        }

        // 新しいスコアを設定(valueは表示順位)
        objective.getScore(entry).setScore(value); // スコアの設定と順番の設定
    }


    //全プレイヤーにスコアボードの表示
    public void showScoreboard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    //スコアボードの削除(表示)
    public void removeScoreboard() {
        if (objective != null) {
            objective.unregister();// Objectiveを削除（スコアボードも消える）
        }

        Scoreboard emptyBoard = Bukkit.getScoreboardManager().getNewScoreboard();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(emptyBoard);
        }
    }


}
