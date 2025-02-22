package com.github.onigokko.games;

import com.github.onigokko.score.ScoreboardManager;
import com.github.onigokko.score.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class Fueoni implements GameModeManager {

    private final TeamManager teamManager;
    private final ScoreboardManager sbManager;
    private final StartPointManager spManager;
    private final GameManager gameManager;

    public Fueoni(TeamManager teamManager, ScoreboardManager sbManager, StartPointManager spManager, GameManager gameManager) {
        this.teamManager = teamManager;
        this.sbManager = sbManager;
        this.spManager = spManager;
        this.gameManager = gameManager;
    }

    @Override
    public void setup() {
        // 既存のチームを削除し、再作成
        teamManager.setTeamName(teamManager.getOni(), "鬼");
        teamManager.setTeamName(teamManager.getNige(), "逃げ");
        //スコアボードの表記を変更
        sbManager.setGameNameToDisplay("増やし鬼");
    }

    @Override
    public void startGame() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.BOLD + "" + ChatColor.BLUE + "増やし鬼スタート!!",
                    ChatColor.YELLOW + ">>鬼が出るまでに安全な場所を見つけよう<<",
                    10, 70, 20);
        }
    }

    @Override
    public void releaseHunter() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.BOLD + "" + ChatColor.RED + "鬼が動き始めた",
                    ChatColor.YELLOW + ">>増える鬼から逃げ切れ<<",
                    10, 70, 20);
        }
    }

    @Override
    public void endGame() {
        String title;
        String subtitle;
        ChatColor titleColor;

        if (teamManager.getNige().getSize() <= 0) {
            title = "鬼チームの勝利！";
            subtitle = "===生存者がいなくなりました！===";
            titleColor = ChatColor.RED;
        } else {
            title = "逃げチームの勝利！";
            subtitle = "===鬼から逃げ切りました！===";
            titleColor = ChatColor.GREEN;
        }

        // 全プレイヤーにタイトルを表示
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.BOLD + "" + titleColor + title, ChatColor.YELLOW + subtitle, 10, 100, 20);
        }

        gameManager.playSoundToAllPlayer(Sound.BLOCK_END_PORTAL_SPAWN);//エンドポータルの効果音
        //鬼チームのリセットのため鬼を逃げチームへ移動
        for (String player : teamManager.getOni().getEntries()) {
            teamManager.addPlayerToTeam(teamManager.getNige(), player);
        }
        Bukkit.broadcastMessage(ChatColor.AQUA + "[System]: " + "全てのプレイヤーを逃げチームに移動しました");

        //全プレイヤーをスタート地点へ転送 事前に逃げチームにしてるので逃げのみでOK
        spManager.teleportTeam(teamManager.getNige());

        gameManager.setGameStart(false);
    }

    @Override
    public void caughtPlayer(Player attacker, Player damagedPlayer) {
        // 逃げプレイヤーを鬼に変更
        teamManager.addPlayerToTeam(teamManager.getOni(), damagedPlayer.getName());
        //もし逃げチームが全滅なら終了
        if (teamManager.getNige().getSize() == 0) {
            endGame();
            return;
        }

        //鬼になったプレイヤーをスタート地点へテレポート
        spManager.teleportPlayer(damagedPlayer);
        //捕まった人は個別メッセージ
        damagedPlayer.sendTitle(ChatColor.YELLOW + "あなたは" +
                        ChatColor.RED + "[鬼]" + ChatColor.YELLOW + "になった",
                "", 10, 40, 10);
        //全体メッセージ
        Bukkit.broadcastMessage(ChatColor.AQUA + "[System]: " +
                ChatColor.YELLOW + damagedPlayer.getName() + " は、" +
                attacker.getName() + "によって鬼にされた");

    }

}
