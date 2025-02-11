package com.github.onigokko.commands;

import com.github.onigokko.games.GameManager;
import com.github.onigokko.score.Timer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class startGame implements CommandExecutor {

    private final GameManager gameManager;
    private final Timer timer;

    public startGame(GameManager gameManager, Timer timer) {
        this.gameManager = gameManager;
        this.timer = timer;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (gameManager.isGameStart()) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                               ChatColor.RED + "すでにゲーム中です!!");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                               ChatColor.RED + "プレイヤーのみ実行可能です！");
            return true;
        }

        if (!(sender.isOp())) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                               ChatColor.RED + "OP権限があるプレイヤーのみ実行可能です!");
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

        gameManager.getGameModeManager().startGame();//現在のゲームモードのスタート処理を呼び出す
        timer.startTimer();//カウントダウン　スタート処理呼び出し

        return true;
    }
}
