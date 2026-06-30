package com.github.onigokko.event;


import com.github.onigokko.Onigokko;
import com.github.onigokko.score.ScoreboardManager;
import com.github.onigokko.score.TeamManager;
import com.github.onigokko.score.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public record PlayerConnection(ScoreboardManager sbManager, TeamManager teamManager, Timer timer) implements Listener {

    @EventHandler //プレイヤーが参加する時
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        sbManager.setScore(ChatColor.GREEN + "総プレイヤー： %d人", Bukkit.getOnlinePlayers().size(), 5);
        sbManager.showScorebordToPlayer(event.getPlayer());

        if (event.getPlayer().isOp()) {
            event.getPlayer().sendMessage(
                    """
                            [System] Onikokkoプラグインからのお知らせ
                            /setgamemodeでゲームを設定できます
                            /setgametimeでゲームの時間が設定できます
                            /setstartでゲームのスタート地点を決めれます
                            /setteamでチームを設定できます。ランダムにしたいときはマグマブロックの上３マス以内にいるプレイヤーから抽選できます
                            /startgameでゲームをスタートします。この時足りない操作があれば案内が出ます
                            /stopgameで強制中断が可能です。チームもリセットされます
                            ※各コマンドを一度実行すると細かな使用方法が出ます
                            ※このメッセージはOPの方全員に参加時に送っています
                            =改善案があればgithubもしくは[とも猫]のいるdiscordにて教えて貰えれば対応します="""
            );
        }

        //プレイヤーにボスバーを表示
        timer.showBossbar(event.getPlayer());

        //プレイヤーを逃げチームに標準で追加
        teamManager.addPlayerToTeam(teamManager.getNige(), event.getPlayer().getName());
    }

    @EventHandler //プレイヤーが抜ける時
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        //プレイヤーをチームから削除
        teamManager.removePlayerAllTeam(event.getPlayer().getName());


        // 1 tick 遅延して正しい人数を取得
        Bukkit.getScheduler().runTaskLater(Onigokko.getInstance(), () ->
                        sbManager.setScore(
                                ChatColor.GREEN + "総プレイヤー： %d人",
                                Bukkit.getOnlinePlayers().size(),
                                5),
                1L);
    }
}
