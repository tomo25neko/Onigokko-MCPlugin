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
            sender.sendMessage(ChatColor.RED + "すでにゲーム中です!!");
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "プレイヤーのみ実行可能です！");
            return false;
        }

        if (sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "OP権限があるプレイヤーのみ実行可能です!");
        }

        gameManager.getGameModeManager().startGame();//現在のゲームモードのスタート処理を呼び出す
        timer.startTimer();//カウントダウン　スタート処理呼び出し

        return true;
    }
}
