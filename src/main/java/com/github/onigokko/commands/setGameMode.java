package com.github.onigokko.commands;

import com.github.onigokko.games.GameManager;
import com.github.onigokko.games.GameMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setGameMode implements CommandExecutor {

    private final GameManager gameManager;

    public setGameMode(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        //OPかチェック
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "このコマンドはOPのみ実行可能です！");
            return true;
        }

        // 引数が不足している場合
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "使用方法: /setgamemode <ゲームモード>\n" +
                                  ChatColor.YELLOW + "~~~ゲームモード一覧~~~");
            for (String modes:GameMode.getModeList()) {
                sender.sendMessage("  ・ " + ChatColor.AQUA + modes);
            } //ゲームのリストを表示
            return true;
        }

        // 入力されたゲームモードを取得
        GameMode mode = GameMode.fromString(args[0]);

        // 無効なゲームモードの場合
        if (mode == null) {
            sender.sendMessage(ChatColor.RED +"エラー: 無効なゲームモードです！\n" +
                                  ChatColor.YELLOW + "~~~ゲームモード一覧~~~");
            for (String modes:GameMode.getModeList()) {
                sender.sendMessage("  ・ " + ChatColor.AQUA + modes);
            } //ゲームのリストを表示
            return true;
        }

        if(gameManager.isGameStart()) {
            sender.sendMessage(ChatColor.RED + "ゲームは現在進行中です！！");
            return true;
        }

        // ゲームモードを設定
        gameManager.setGameMode(mode);

        // 全プレイヤーにゲームモードが変更されたことを通知
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.GREEN + "ゲームモードが" +
                            ChatColor.DARK_AQUA +"[" + mode.getDisplayName()
                            + "]" +ChatColor.GREEN + "になりました"
                    , "", 10, 70, 20);
        }

        return true;
    }

}
