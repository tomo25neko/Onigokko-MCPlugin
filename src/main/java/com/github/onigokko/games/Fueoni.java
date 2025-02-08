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
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.BOLD + "" + ChatColor.BLUE + "増やし鬼スタート!!",
                             ChatColor.YELLOW + "~ 増える鬼から逃げ切れ ~",
                            10,70,20 );
        }

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
            player.sendTitle(ChatColor.BOLD + "" +titleColor + title, ChatColor.YELLOW + subtitle, 10, 70, 20);
        }


        //鬼チームのリセットのため鬼を逃げチームへ移動
        for (String player : new ArrayList<>(teamManager.getOni().getEntries())) {
            teamManager.addPlayerToTeam(teamManager.getNige(), player);
        }
    }

    @Override
    public void sendMessage(Player attacker, Player damagedPlayer) {
        //捕まった人は個別メッセージ
        damagedPlayer.sendTitle(ChatColor.BOLD + "" +ChatColor.YELLOW +  "～あなたは鬼になった～",
                             "", 10, 50, 10);
        //全体メッセージ
        Bukkit.broadcastMessage(ChatColor.YELLOW + damagedPlayer.getName() + " は、" +
                                attacker.getName() + "によって鬼にされた");

    }

}
