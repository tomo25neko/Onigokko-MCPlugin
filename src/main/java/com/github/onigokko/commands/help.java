package com.github.onigokko.commands;

import com.github.onigokko.games.GameManager;
import com.github.onigokko.games.StartPointManager;
import com.github.onigokko.score.TeamManager;
import com.github.onigokko.score.Timer;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public record help(GameManager gameManager, Timer timer, StartPointManager spManager,
                   TeamManager teamManager) implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        sender.sendMessage(ChatColor.AQUA + "========== ゲーム開始準備状況チェック ==========");

        boolean allReady = true;

        // ゲームモードチェック
        if (gameManager.getGameModeManager() == null) {
            sender.sendMessage(ChatColor.RED + "[×] ゲームモード: 未設定 (コマンド: /setgamemode)");
            allReady = false;
        } else {
            sender.sendMessage(ChatColor.GREEN + "[○] ゲームモード: 設定済み");
        }

        // ゲーム時間チェック
        if (timer.getTime() == 0) {
            sender.sendMessage(ChatColor.RED + "[×] ゲーム時間: 未設定 (コマンド: /setgametime)");
            allReady = false;
        } else {
            sender.sendMessage(ChatColor.GREEN + "[○] ゲーム時間: " + timer.getTime() + "秒");
        }

        // スタート地点チェック
        World world = sender instanceof Player ? ((Player) sender).getWorld() : null;
        if (world != null && spManager.getStartPoint(world) == null) {
            sender.sendMessage(ChatColor.RED + "[×] スタート地点: 未設定 (コマンド: /setstart)");
            allReady = false;
        } else if (world != null) {
            sender.sendMessage(ChatColor.GREEN + "[○] スタート地点: 設定済み");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "[?] スタート地点: プレイヤーから確認できません");
        }

        // チームチェック
        int oniCount = teamManager.getOni().getSize();
        int nigeCount = teamManager.getNige().getSize();
        if (oniCount == 0 && nigeCount == 0) {
            sender.sendMessage(ChatColor.RED + "[×] チーム設定: 未設定 (コマンド: /setteam)");
            allReady = false;
        } else {
            sender.sendMessage(ChatColor.GREEN + "[○] チーム設定: 鬼" + oniCount + "人, 逃げ" + nigeCount + "人");
        }

        // ゲーム進行中チェック
        if (GameManager.isGameStart()) {
            sender.sendMessage(ChatColor.YELLOW + "[!] ゲーム状態: 進行中");
        } else if (timer.isStartEscapeCountdown()) {
            sender.sendMessage(ChatColor.YELLOW + "[!] ゲーム状態: 逃げカウントダウン中");
        } else {
            sender.sendMessage(ChatColor.GREEN + "[○] ゲーム状態: 停止中");
        }

        sender.sendMessage(ChatColor.AQUA + "==========================================");

        if (allReady && !GameManager.isGameStart() && !timer.isStartEscapeCountdown()) {
            sender.sendMessage(ChatColor.GREEN + "全ての準備が完了しました！ /startgame で開始できます。");
        } else if (GameManager.isGameStart()) {
            sender.sendMessage(ChatColor.YELLOW + "ゲームは現在進行中です。");
        } else {
            sender.sendMessage(ChatColor.RED + "準備が完了していません。上記の項目を確認してください。");
        }

        return true;
    }
}
