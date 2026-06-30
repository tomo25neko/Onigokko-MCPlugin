package com.github.onigokko.commands;

import com.github.onigokko.games.GameManager;
import com.github.onigokko.games.HideballManager;
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

public record startGame(GameManager gameManager, Timer timer, StartPointManager spManager,
                        TeamManager teamManager, HideballManager hideballManager) implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player) && !(sender.isOp())) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                    ChatColor.RED + "OP権限があるプレイヤーのみ実行可能です！");
            return true;
        }

        if (GameManager.isGameStart()) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                    ChatColor.RED + "すでにゲーム中です!!");
            return true;
        }

        if (timer.isStartEscapeCountdown()) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                    ChatColor.RED + "すでにカウントダウン中です!!");
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

        World world = null;
        if (sender instanceof Player) {
            world = ((Player) sender).getWorld();
        }
        //スタート地点が設定されているか確認
        if (world != null && spManager.getStartPoint(world) == null) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                    ChatColor.RED + "スタート地点が設定されていません");
            return true;
        }

        gameManager.playSoundToAllPlayer(Sound.EVENT_RAID_HORN);

        gameManager.getGameModeManager().startGame();//現在のゲームモードのスタート処理を呼び出す
        spManager.teleportTeam(teamManager.getNige());//逃げチームを先にテレポート

        // 影玉を全プレイヤーに配布（持続時間が0の場合は配布しない）
        if (hideballManager.getDuration() > 0) {
            for (Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
                // 影玉時間を初期化
                hideballManager.initializePlayerTime(player);
                // 影玉アイテムを配布
                player.getInventory().addItem(hideballManager.createHideballItem());
            }
        }

        timer.startTimer(teamManager.getOni());//スタート処理呼び出し


        return true;
    }
}
