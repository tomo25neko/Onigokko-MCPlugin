package com.github.onigokko;

import com.github.onigokko.Event.PlayerJoin;
import com.github.onigokko.score.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Onigokko extends JavaPlugin {

    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        //コンストラクタ生成
        this.scoreboardManager = new ScoreboardManager();

        //イベント登録
        getServer().getPluginManager().registerEvents(new PlayerJoin(scoreboardManager), this);

        Bukkit.getLogger().info("増やし鬼プラグインが起動しました/Fuyasi Oni Plugin has started.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        scoreboardManager.removeScoreboard();

        Bukkit.getLogger().info("増やし鬼プラグインが停止しました / Fuyasi Oni Plugin has finished.");
    }
}
