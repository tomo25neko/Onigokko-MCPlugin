package com.github.onigokko.Event;


import com.github.onigokko.score.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private final ScoreboardManager scoreboardManager;

    public PlayerJoin(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        scoreboardManager.setScore("総プレイヤー数: %d人", Bukkit.getOnlinePlayers().size(), 10);
        scoreboardManager.showScorebordToPlayer(event.getPlayer());
    }
}
