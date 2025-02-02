package com.github.onigokko;

import com.github.onigokko.Event.PlayerJoin;
import com.github.onigokko.commands.setGameTime;
import com.github.onigokko.score.ScoreboardManager;
import com.github.onigokko.score.Timer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Onigokko extends JavaPlugin {

    private PluginManager pluginManager;

    private ScoreboardManager sbManager;
    private Timer timer;

    @Override
    public void onEnable() {
        // Plugin startup logic

        //BukkitAPI

        this.pluginManager = getServer().getPluginManager();

        //コンストラクタ生成(クラス)
        this.sbManager = new ScoreboardManager();
        this.timer = new Timer(this,sbManager);

        //イベント登録
        pluginManager.registerEvents(new PlayerJoin(sbManager), this);

        //コマンド登録
        getCommand("setgametime").setExecutor(new setGameTime(timer));

        //起動通知
        Bukkit.getLogger().info("増やし鬼プラグインが起動しました/Fuyasi Oni Plugin has started.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        sbManager.removeScoreboard();

        //停止通知
        Bukkit.getLogger().info("増やし鬼プラグインが停止しました / Fuyasi Oni Plugin has finished.");
    }
}
