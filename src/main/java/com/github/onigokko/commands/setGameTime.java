package com.github.onigokko.commands;

import com.github.onigokko.score.Timer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class setGameTime implements CommandExecutor {

    private final Timer timer;

    public setGameTime(Timer timer) {
        this.timer = timer;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        //op権限チェック
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "このコマンドはOPのみ実行可能です。");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "ゲーム時間を指定してください。使用例: /setgametime [time]");
            return false;
        }

        int time;//エラーハンドリングを行い代入
        try {
            time = Integer.parseInt(args[0]);
            if (time <= 0) {
                sender.sendMessage(ChatColor.RED + "ゲーム時間は正の整数で指定してください。");
                return false;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "ゲーム時間は整数で指定してください。");
            return false;
        }

        //全条件を満たす場合時間をセット
        timer.setTime(time);
        // 設定された時間をユーザーに通知
        sender.sendMessage(ChatColor.GREEN + "ゲーム時間を " + time + " 秒に設定しました。");
        return true;
    }
}
