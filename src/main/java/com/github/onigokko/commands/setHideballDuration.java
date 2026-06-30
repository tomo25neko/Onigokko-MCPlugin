package com.github.onigokko.commands;

import com.github.onigokko.games.HideballManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public record setHideballDuration(HideballManager hideballManager) implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender.isOp())) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                    ChatColor.RED + "OP権限があるプレイヤーのみ実行可能です！");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                    ChatColor.RED + "使用法: /sethideballduration <秒数>");
            return true;
        }

        try {
            int seconds = Integer.parseInt(args[0]);
            if (seconds < 0) {
                sender.sendMessage(ChatColor.AQUA + "[System]: " +
                        ChatColor.RED + "秒数は0以上の整数を指定してください！");
                return true;
            }

            hideballManager.setDuration(seconds);

            if (seconds == 0) {
                sender.sendMessage(ChatColor.AQUA + "[System]: " +
                        ChatColor.YELLOW + "影玉を無効化しました（配布しません）");
            } else {
                sender.sendMessage(ChatColor.AQUA + "[System]: " +
                        ChatColor.GREEN + "影玉の持続時間を " + seconds + " 秒に設定しました");
            }

        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                    ChatColor.RED + "秒数は整数で指定してください！");
        }

        return true;
    }
}
