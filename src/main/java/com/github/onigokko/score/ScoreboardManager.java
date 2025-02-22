package com.github.onigokko.score;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager {

    private final Scoreboard scoreboard;
    private final Objective objective;

    public ScoreboardManager() {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("gameinfo", Criteria.DUMMY, "サーバー情報");

        setDefaultScoreboard();
    }

    //他のクラスが使うためのスコアボードを渡す　必要なければ後に削除
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    //初期スコアボードの作成
    private void setDefaultScoreboard() {
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.AQUA + "サーバー情報");
    }

    //スコアボードの一番上に出る名称をgameModeに変更
    public void setGameNameToDisplay(String gameMode) {
        objective.setDisplayName(ChatColor.AQUA + gameMode);
    }

    //スコアボードに登録　keyでセットしてvalueが値 色は引数に事前につけておく
    public void setScore(String key, int amount, int value) {
        // %d を含む場合は置換して完全な文字列を生成
        String newEntry = key.contains("%d") ? String.format(key, amount) : key;

        //更新前のスコアの削除
        removeScore(key);

        objective.getScore(newEntry).setScore(value); // スコアの設定と順番の設定

    }

    // スコアの削除処理（ChatColorを除去して比較）
    public void removeScore(String key) {
        // 渡されたキーから色コードを除去して、最初の空白までの部分を固定部分とする
        String strippedKey = ChatColor.stripColor(key);
        int spaceIndex = strippedKey.indexOf(" ");
        String prefix = (spaceIndex > 0) ? strippedKey.substring(0, spaceIndex) : strippedKey;

        // 現在のスコアボードにある各エントリについて、色コード除去後に固定部分を取得し、比較する
        for (String existingEntry : scoreboard.getEntries()) {
            String strippedExistingEntry = ChatColor.stripColor(existingEntry);
            int idx = strippedExistingEntry.indexOf(" ");
            String existingPrefix = (idx > 0) ? strippedExistingEntry.substring(0, idx) : strippedExistingEntry;
            if (existingPrefix.equals(prefix)) {
                // 固定部分が一致すれば、既存のエントリを削除する
                scoreboard.resetScores(existingEntry);
            }
        }
    }


    //プレイヤーのサイドバーにスコアボードを表示。
    public void showScorebordToPlayer(Player player) {
        player.setScoreboard(scoreboard);
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
