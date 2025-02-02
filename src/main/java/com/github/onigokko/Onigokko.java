package com.github.onigokko;

import com.github.onigokko.Event.PlayerJoin;
import com.github.onigokko.commands.setGameMode;
import com.github.onigokko.commands.setGameTime;
import com.github.onigokko.games.GameManager;
import com.github.onigokko.games.GameModeManager;
import com.github.onigokko.score.ScoreboardManager;
import com.github.onigokko.score.TeamManager;
import com.github.onigokko.score.Timer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Onigokko extends JavaPlugin {
    //Bukkit　プラグインマネージャー(イベント登録)
    private PluginManager plManager;

    //プラグインコンストラクタ
    private ScoreboardManager sbManager;
    private Timer timer;
    private TeamManager teamManager;
    private GameModeManager gameModeManager;
    private GameManager gameManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        //BukkitAPI
        this.plManager = getServer().getPluginManager();

        //コンストラクタ生成(クラス)
        this.sbManager = new ScoreboardManager();
        this.timer = new Timer(this,sbManager);
        this.teamManager = new TeamManager();
        this.gameManager = new GameManager(teamManager,sbManager);

        //イベント登録
        plManager.registerEvents(new PlayerJoin(sbManager), this);

        //コマンド登録
        getCommand("setgametime").setExecutor(new setGameTime(timer));
        getCommand("setgamemode").setExecutor(new setGameMode(gameManager));

        //起動通知
        Bukkit.getLogger().info("増やし鬼プラグインが起動しました/Fuyasi Oni Plugin has started.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        //プラグインで作成したスコアボード及びチームの削除
        sbManager.removeScoreboard();
        teamManager.removeScoreboard();

        //停止通知
        Bukkit.getLogger().info("増やし鬼プラグインが停止しました / Fuyasi Oni Plugin has finished.");
    }

}
