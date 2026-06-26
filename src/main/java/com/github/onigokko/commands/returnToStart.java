package com.github.onigokko.commands;

import com.github.onigokko.games.GameManager;
import com.github.onigokko.games.StartPointManager;
import com.github.onigokko.score.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public record returnToStart(StartPointManager spManager, TeamManager teamManager) implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // プレイヤーのみ実行可能
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行可能です。");
            return true;
        }

        // スタート地点が設定されているか確認
        if (spManager.getStartPoint(player.getWorld()) == null) {
            sender.sendMessage(ChatColor.RED + "スタート地点が設定されていません。");
            return true;
        }

        // ゲームが進行中の場合は鬼チームのみ実行可能
        if (GameManager.isGameStart()) {
            if (!teamManager.getOni().hasEntry(player.getName())) {
                sender.sendMessage(ChatColor.RED + "ゲーム進行中は鬼チームのプレイヤーのみ実行可能です。");
                return true;
            }
        }

        // スタート地点にテレポート
        spManager.teleportPlayer(player);
        player.sendMessage(ChatColor.GREEN + "スタート地点にテレポートしました。");

        return true;
    }
}
