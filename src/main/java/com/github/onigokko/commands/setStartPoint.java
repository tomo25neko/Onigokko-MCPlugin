package com.github.onigokko.commands;

import com.github.onigokko.games.StartPointManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setStartPoint implements CommandExecutor {

    private final StartPointManager spManager;

    public setStartPoint(StartPointManager spManager) {
        this.spManager = spManager;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //OPかつプレイヤーのみ実行可能
        if (!(sender instanceof Player) || !sender.isOp()) {
            sender.sendMessage(ChatColor.AQUA + "[System]: " +
                    ChatColor.RED + "OP権限のあるプレイヤーのみ実行可能です！");
            return true;
        }

        //Player型に変換して渡す
        spManager.setStartPoint((Player) sender);
        return true;
    }
}
