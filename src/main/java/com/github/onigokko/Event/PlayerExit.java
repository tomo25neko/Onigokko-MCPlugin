package com.github.onigokko.Event;

import com.github.onigokko.score.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerExit implements Listener {
    private final ScoreboardManager scoreboardManager;

    public PlayerExit(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
    }

    @EventHandler
    public void onPlayerExitEvent(PlayerQuitEvent event) {
        scoreboardManager.setScore("総プレイヤー数: %d人", Bukkit.getOnlinePlayers().size(), 10);
    }

}
