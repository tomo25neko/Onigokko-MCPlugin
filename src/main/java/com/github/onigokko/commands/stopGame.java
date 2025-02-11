package com.github.onigokko.commands;

import com.github.onigokko.games.GameManager;
import com.github.onigokko.score.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class stopGame implements CommandExecutor {

    private final GameManager gameManager;
    private final Timer timer;

    public stopGame(GameManager gameManager, Timer timer) {
        this.gameManager = gameManager;
        this.timer = timer;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(gameManager.isGameStart())) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                               ChatColor.RED + "現在ゲーム中ではありません!!");
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

        timer.stopTimer();
        gameManager.setGameStart(false);//ゲーム中ではない用に変更

        Bukkit.broadcastMessage(ChatColor.AQUA +"[System]: " +
                                ChatColor.RED + "管理者によってゲームが中断されました！");

        return true;
    }
}
