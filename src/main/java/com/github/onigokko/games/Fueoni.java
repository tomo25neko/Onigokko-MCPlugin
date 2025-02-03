package com.github.onigokko.games;

import com.github.onigokko.score.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;


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
        String title;
        String subtitle;
        ChatColor titleColor;

        if (teamManager.getNige().getSize() == 0) {
            title = "鬼チームの勝利！";
            subtitle = "生存者がいなくなりました！";
            titleColor = ChatColor.RED;
        } else {
            title = "逃げチームの勝利！";
            subtitle = "鬼から逃げ切りました！";
            titleColor = ChatColor.GREEN;
        }

        // 全プレイヤーにタイトルを表示
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(titleColor + title, ChatColor.YELLOW + subtitle, 10, 70, 20);
        }


        //鬼チームのリセットのため鬼を逃げチームへ移動
        for (String player : new ArrayList<>(teamManager.getOni().getEntries())) {
            teamManager.addPlayerToTeam(teamManager.getNige(), player);
        }
    }

}
