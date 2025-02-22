package com.github.onigokko.commands;

import com.github.onigokko.games.GameManager;
import com.github.onigokko.games.StartPointManager;
import com.github.onigokko.score.TeamManager;
import com.github.onigokko.score.Timer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class startGame implements CommandExecutor {

    private final GameManager gameManager;
    private final Timer timer;
    private final StartPointManager spManager;
    private final TeamManager teamManager;

    public startGame(GameManager gameManager, Timer timer, StartPointManager spManager, TeamManager teamManager) {
        this.gameManager = gameManager;
        this.timer = timer;
        this.spManager = spManager;
        this.teamManager = teamManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (gameManager.isGameStart()) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                    ChatColor.RED + "すでにゲーム中です!!");
            return true;
        }
        if (timer.isStartEscapeCountdown()) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                    ChatColor.RED + "すでに逃げ中です!!");
            return true;
        }

        if (!(sender instanceof Player) && !(sender.isOp())) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                    ChatColor.RED + "OP権限があるプレイヤーのみ実行可能です！");
            return true;
        }


        if (timer.getTime() == 0) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                    ChatColor.RED + "ゲームの時間が0秒です！！");
            return true;
        }

        if (gameManager.getGameModeManager() == null) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                    ChatColor.RED + "ゲームモードが設定されていません!!");
            return true;
        }

        World world = ((Player) sender).getWorld();
        //スタート地点が設定されているか確認
        if (spManager.getStartPoint(world) == null) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                    ChatColor.RED + "スタート地点が設定されていません");
            return true;
        }

        gameManager.playSoundToAllPlayer(Sound.EVENT_RAID_HORN);

        gameManager.getGameModeManager().startGame();//現在のゲームモードのスタート処理を呼び出す
        spManager.teleportTeam(teamManager.getNige());//逃げチームを先にテレポート
        timer.startTimer(teamManager.getOni());//スタート処理呼び出し


        return true;
    }
}
