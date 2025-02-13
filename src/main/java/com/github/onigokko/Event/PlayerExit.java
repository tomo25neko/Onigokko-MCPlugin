package com.github.onigokko.Event;

import com.github.onigokko.score.ScoreboardManager;
import com.github.onigokko.score.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerExit implements Listener {
    private final ScoreboardManager scoreboardManager;
    private final TeamManager teamManager;

    public PlayerExit(ScoreboardManager scoreboardManager, TeamManager teamManager) {
        this.scoreboardManager = scoreboardManager;
        this.teamManager = teamManager;
    }

    @EventHandler
    public void onPlayerExitEvent(PlayerQuitEvent event) {
        scoreboardManager.setScore(ChatColor.GREEN + "総プレイヤー： %d人", Bukkit.getOnlinePlayers().size(), 5);

        //プレイヤーをチームから削除
        teamManager.removePlayerAllTeam(event.getPlayer().getName());
    }

}
