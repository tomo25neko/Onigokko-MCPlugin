package com.github.onigokko;

import com.github.onigokko.Event.PlayerDamage;
import com.github.onigokko.Event.PlayerExit;
import com.github.onigokko.Event.PlayerJoin;
import com.github.onigokko.commands.*;
import com.github.onigokko.games.GameManager;
import com.github.onigokko.games.StartPointManager;
import com.github.onigokko.score.ScoreboardManager;
import com.github.onigokko.score.TeamManager;
import com.github.onigokko.score.Timer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Onigokko extends JavaPlugin {
    //Bukkit　プラグインマネージャー(イベント登録)
    private PluginManager plManager;

    //プラグインコンストラクタ
    private ScoreboardManager sbManager;
    private TeamManager teamManager;
    private StartPointManager spManager;
    private GameManager gameManager;
    private Timer timer;

    @Override
    public void onEnable() {
        // Plugin startup logic

        //BukkitAPI
        this.plManager = getServer().getPluginManager();

        //コンストラクタ生成(クラス)
        this.sbManager = new ScoreboardManager();
        this.teamManager = new TeamManager(sbManager);
        this.spManager = new StartPointManager();
        this.gameManager = new GameManager(teamManager,sbManager);
        this.timer = new Timer(this,sbManager,gameManager);

        //イベント登録
        plManager.registerEvents(new PlayerJoin(sbManager,teamManager), this);
        plManager.registerEvents(new PlayerExit(sbManager,teamManager), this);

        plManager.registerEvents(new PlayerDamage(teamManager,gameManager), this);


        //コマンド登録
        getCommand("setgametime").setExecutor(new setGameTime(timer));
        getCommand("setgamemode").setExecutor(new setGameMode(gameManager));
        getCommand("startgame").setExecutor(new startGame(gameManager,timer,spManager));
        getCommand("stopgame").setExecutor(new stopGame(gameManager,timer));
        getCommand("setteam").setExecutor(new setTeamToPlayer(gameManager,teamManager));
        getCommand("setstart").setExecutor(new setStartPoint(spManager));

        //起動通知
        Bukkit.getLogger().info("鬼ごっこプラグインが起動しました / Onigokko Plugin has started.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        //プラグインで作成したスコアボード及びチームの削除
        teamManager.removeTeams();
        sbManager.removeScoreboard();
        //プラグインで作成したアーマースタンドの削除
        spManager.removeStartPoint();

        //停止通知
        Bukkit.getLogger().info("鬼ごっこプラグインが停止しました / Onigokko Plugin has finished.");
    }

}
