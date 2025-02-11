package com.github.onigokko.Event;


import com.github.onigokko.score.ScoreboardManager;
import com.github.onigokko.score.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private final ScoreboardManager sbManager;
    private final TeamManager teamManager;

    public PlayerJoin(ScoreboardManager scoreboardManager, TeamManager teamManager) {
        this.sbManager = scoreboardManager;
        this.teamManager  = teamManager;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        sbManager.setScore("総プレイヤー数: %d人", Bukkit.getOnlinePlayers().size(), 10);
        sbManager.showScorebordToPlayer(event.getPlayer());

        //プレイヤーを逃げチームに標準で追加
        teamManager.addPlayerToTeam(teamManager.getNige(), event.getPlayer().getName());
    }
}
