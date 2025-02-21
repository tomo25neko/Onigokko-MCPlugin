package com.github.onigokko.commands;

import com.github.onigokko.games.GameManager;
import com.github.onigokko.games.OniManager;
import com.github.onigokko.score.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class setTeamToPlayer implements CommandExecutor {

    private final GameManager gameManager;
    private final TeamManager teamManager;
    private final OniManager oniManager;

    public setTeamToPlayer(GameManager gameManager, TeamManager teamManager, OniManager oniManager) {
        this.gameManager = gameManager;
        this.teamManager = teamManager;
        this.oniManager = oniManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // OPチェック
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "このコマンドはOPのみ実行可能です！");
            return true;
        }

        // ゲーム中は変更不可
        if (gameManager.isGameStart()) {
            sender.sendMessage(ChatColor.RED + "ゲーム中はチームの変更はできません！");
            return true;
        }

        // 引数が足りない場合、使用方法を表示
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "使用方法:");
            sender.sendMessage(ChatColor.YELLOW + "/setteam <oni|nige> <playerName>");
            sender.sendMessage(ChatColor.YELLOW + "/setteam random <number>");
            return true;
        }

        String option = args[0].toLowerCase();

        // 個別追加の場合：/setteam oni <playerName> または /setteam nige <playerName>
        if (option.equals("oni") || option.equals("nige")) {
            //プレイヤーを取得
            Player player = Bukkit.getPlayer(args[1]);

            if (option.equals("oni")) {
                teamManager.addPlayerToTeam(teamManager.getOni(), player.getName());
                sender.sendMessage(ChatColor.GREEN + player.getName() + " を鬼チームに追加しました。");
            } else {
                teamManager.addPlayerToTeam(teamManager.getNige(), player.getName());
                sender.sendMessage(ChatColor.GREEN + player.getName() + " を逃げチームに追加しました。");
            }
            return true;
        }
        // ランダム追加の場合：/setteam random <number>
        else if (option.equals("random")) {
            int count;
            try {
                count = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "数値を指定してください。");
                return true;
            }

            // 候補となるプレイヤーのリストを作成
            // 条件：足元から最大3ブロック下に マグマブロックがある場合
            List<Player> candidates = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (isPlayerNearLava(player)) {
                    candidates.add(player);
                }
            }

            // 候補がいない場合
            if (candidates.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "マグマブロックの上3マス以内にいるプレイヤーが見つかりませんでした。");
                return true;
            }

            // 候補リストをランダムにシャッフル
            Collections.shuffle(candidates);

            // アナウンス（全体通知）
            Bukkit.broadcastMessage(ChatColor.AQUA + "[System]: " +
                                    ChatColor.DARK_RED + "「今回の鬼はこの人たちです!!」");

            int added = 0;
            for (Player player : candidates) {
                if (added >= count) break;
                oniManager.applyOniEquipment(player);//鬼装備セット
                teamManager.addPlayerToTeam(teamManager.getOni(), player.getName());//鬼に追加
                Bukkit.broadcastMessage(" ・" + player.getName());//全プレイヤーに告知
                added++;
            }

            if (added < count) {
                sender.sendMessage(ChatColor.GREEN + String.format("指定された人数よりも対象プレイヤーが少なかったため%d人中%d人が鬼チームに追加されました。", count, added));
            } else {
                sender.sendMessage(ChatColor.GREEN + "鬼チームに" + added + "人のプレイヤーを追加しました。");
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "無効なオプションです。使用可能なのは oni, nige, random です。");
            return true;
        }
    }

    /**
     * 指定したプレイヤーが、足元から最大3ブロック下に
     * 溶岩 (Material.LAVA) または マグマブロック (Material.MAGMA_BLOCK) が存在するかを判定する。
     */
    private boolean isPlayerNearLava(Player player) {
        Location loc = player.getLocation();
        for (int i = 1; i <= 3; i++) {
            Block block = loc.clone().subtract(0, i, 0).getBlock();
            if (block.getType() == Material.MAGMA_BLOCK) {
                return true;
            }
        }
        return false;
    }
}
