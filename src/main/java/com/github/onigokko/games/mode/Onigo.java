package com.github.onigokko.games.mode;

import com.github.onigokko.games.GameManager;
import com.github.onigokko.games.GameModeManager;
import com.github.onigokko.games.StartPointManager;
import com.github.onigokko.score.ScoreboardManager;
import com.github.onigokko.score.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Onigo implements GameModeManager {

    private final TeamManager teamManager;
    private final ScoreboardManager sbManager;
    private final StartPointManager spManager;
    private final GameManager gameManager;

    public Onigo(TeamManager teamManager, ScoreboardManager sbManager, StartPointManager spManager, GameManager gameManager) {
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
        sbManager.setGameNameToDisplay("鬼ごっこ");
    }

    @Override
    public void startGame() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.BOLD + "" + ChatColor.BLUE + "鬼こっこスタート!!",
                    ChatColor.YELLOW + ">>鬼が出るまでに安全な場所を見つけよう<<",
                    10, 70, 20);
        }
    }

    @Override
    public void releaseHunter() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.BOLD + "" + ChatColor.RED + "鬼が動き始めた",
                    ChatColor.YELLOW + ">>最後まで逃げ切るのは誰だ<<",
                    10, 70, 20);
        }
    }

    @Override
    public void endGame() {
        // 全プレイヤーにタイトルを表示
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.BOLD + "" + ChatColor.GREEN + "鬼ごっこ終了！", ChatColor.YELLOW + "誰が逃げ切ったかな", 10, 100, 20);
        }
        StringBuilder message = new StringBuilder(ChatColor.GREEN + "====逃げ切った人====\n");
        teamManager.getNige().getEntries().forEach(entry ->
                message.append(ChatColor.WHITE+"・　" + ChatColor.AQUA + entry + "\n")); //ラムダ記法

        Bukkit.broadcastMessage(message.toString());//終了時に鬼でないプレイヤーの名前をチャットに乗せる

        gameManager.playSoundToAllPlayer(Sound.BLOCK_END_PORTAL_SPAWN);//エンドポータルの効果音
        //鬼チームのリセットのため鬼を逃げチームへ移動
        for (String player : teamManager.getOni().getEntries()) {
            teamManager.addPlayerToTeam(teamManager.getNige(), player);
        }
        Bukkit.broadcastMessage(ChatColor.AQUA + "[System]: " + "全てのプレイヤーを逃げチームに移動しました");

        //全プレイヤーをスタート地点へ転送 事前に逃げチームにしてるので逃げのみでOK
        spManager.teleportTeam(teamManager.getNige());

        GameManager.setGameStart(false);
    }

    @Override
    public void caughtPlayer(Player attacker, Player damagedPlayer) {
        // 逃げプレイヤーを鬼に変更
        teamManager.addPlayerToTeam(teamManager.getOni(), damagedPlayer.getName());
        //　鬼を逃げプレイヤーに変更
        teamManager.addPlayerToTeam(teamManager.getNige(), attacker.getName());

        //捕まった人は個別メッセージ
        damagedPlayer.sendTitle(ChatColor.YELLOW + "あなたは" +
                        ChatColor.RED + "[鬼]" + ChatColor.YELLOW + "になった",
                "時間までに他の人を捕まえよう！", 10, 40, 10);
        damagedPlayer.getWorld().playSound(
                damagedPlayer.getLocation(),
                Sound.ENTITY_ZOMBIE_INFECT,
                1.0f, 1.0f); //ゾンビ音再生

        //全体メッセージ
        Bukkit.broadcastMessage(ChatColor.AQUA + "[System]: " +
                ChatColor.YELLOW + damagedPlayer.getName() + " は、" +
                attacker.getName() + "に捕まって鬼になった");
    }
}
