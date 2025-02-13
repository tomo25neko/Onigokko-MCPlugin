package com.github.onigokko.Event;


import com.github.onigokko.score.ScoreboardManager;
import com.github.onigokko.score.TeamManager;
import com.github.onigokko.score.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private final ScoreboardManager sbManager;
    private final TeamManager teamManager;
    private final Timer timer;

    public PlayerJoin(ScoreboardManager scoreboardManager, TeamManager teamManager, Timer timer) {
        this.sbManager = scoreboardManager;
        this.teamManager  = teamManager;
        this.timer = timer;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        sbManager.setScore(ChatColor.GREEN + "総プレイヤー： %d人", Bukkit.getOnlinePlayers().size(), 5);
        sbManager.showScorebordToPlayer(event.getPlayer());

        //プレイヤーにボスバーを表示
        timer.showBossbar(event.getPlayer());

        //プレイヤーを逃げチームに標準で追加
        teamManager.addPlayerToTeam(teamManager.getNige(), event.getPlayer().getName());
    }
}
