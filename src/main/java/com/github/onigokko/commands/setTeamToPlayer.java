package com.github.onigokko.commands;

import com.github.onigokko.games.GameManager;
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

    public setTeamToPlayer(GameManager gameManager, TeamManager teamManager) {
        this.gameManager = gameManager;
        this.teamManager = teamManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        //OPかチェック
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "このコマンドはOPのみ実行可能です！");
            return true;
        }

        if (gameManager.isGameStart()) {
            sender.sendMessage((ChatColor.RED + "ゲームスタート中は変更不可です"));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "使用方法:");
            sender.sendMessage(ChatColor.YELLOW + "/setteam <nige|oni> <playerName>");
            sender.sendMessage(ChatColor.YELLOW + "/setteam oni random <number>");

            return true;
        }

        String teamArg = args[0].toLowerCase();
        if (!teamArg.equals("oni") && !teamArg.equals("nige")) {
            sender.sendMessage(ChatColor.RED + "無効なチーム名です。使用可能なのは oni または nige です。");
            return true;
        }

        // 個別追加の場合：/setteam <team> <playerName>
        if (args.length == 2) {
            String playerName = args[1];
            // 追加先チームを判定
            if (teamArg.equals("oni")) {
                teamManager.addPlayerToTeam(teamManager.getOni(), playerName);
                sender.sendMessage(ChatColor.GREEN + playerName + " を鬼チームに追加しました。");
            } else {
                teamManager.addPlayerToTeam(teamManager.getNige(), playerName);
                sender.sendMessage(ChatColor.GREEN + playerName + " を逃げチームに追加しました。");
            }

            return true;
        }

        // ランダム選択の場合：/setteam oni random <number>
        if (args.length == 3 && teamArg.equals("oni") && args[1].equalsIgnoreCase("random")) {
            int count;
            try {
                count = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "数値を指定してください。");
                return true;
            }

            // 候補となるプレイヤーのリストを作成（足元から最大3ブロック下に溶岩またはマグマブロックがある場合）
            List<Player> candidates = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (isPlayerNearLava(player)) {
                    candidates.add(player);
                }
            }

            //抽選対象がいない場合。
            if (candidates.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "溶岩またはマグマブロック上3マス以内にいるプレイヤーが見つかりませんでした。");
                return true;
            }

            // リストをランダムに並び替える
            Collections.shuffle(candidates);

            //アナウンス　１
            Bukkit.broadcastMessage(ChatColor.AQUA + "[System]: " +
                                    ChatColor.DARK_RED + "「今回の鬼はこの人たちです!!」");

            int added = 0;
            for (Player player : candidates) {
                if (added >= count) break;//追加人数が指定された人数を超えたら停止
                teamManager.addPlayerToTeam(teamManager.getOni(), player.getName());
                Bukkit.broadcastMessage(" ・" + player.getName());//アナウンス2 名前
                added++;
            }

            if(added < count) {
                sender.sendMessage(ChatColor.GREEN + String.format("指定された人数よりも対象プレイヤーが少なかったため%d人中%d人が鬼チームに追加されました。", count, added));
            } else {
                sender.sendMessage(ChatColor.GREEN + "鬼チームに" + added + "人のプレイヤーを追加しました。");
            }
            return true;
        }

        sender.sendMessage(ChatColor.RED + "使用方法が正しくありません。");
        return true;
    }

    /**
     * 指定したプレイヤーが、足元から最大 maxDistance ブロック下に
     * マグマブロック (Material.MAGMA_BLOCK) が存在するかを判定する。
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
