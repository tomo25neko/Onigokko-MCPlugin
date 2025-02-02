package com.github.onigokko.commands;

import com.github.onigokko.games.GameManager;
import com.github.onigokko.games.GameMode;
import com.github.onigokko.games.GameModeManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class setGameMode implements CommandExecutor {

    private final GameManager gameManager;
    private GameModeManager gameModeManager;


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
            sender.sendMessage(ChatColor.RED + "使用方法: /setgamemode <ゲームモード>");
            return true;
        }

        // 入力されたゲームモードを取得
        GameMode mode = GameMode.fromString(args[0]);

        // 無効なゲームモードの場合
        if (mode == null) {
            sender.sendMessage("§cエラー: 無効なゲームモードです！");
            return true;
        }

        // ゲームモードを設定
        gameManager.setGameMode(mode);
        sender.sendMessage(ChatColor.GREEN + "ゲームモードが " + mode.getDisplayName() + " に設定されました！");


        return true;
    }
    /*　次回以降　
    ゲームのスコアボードに鬼と逃げチームのプレイヤー数を追加
    プレイヤーを鬼に追加するコマンドの追加
     */

}
