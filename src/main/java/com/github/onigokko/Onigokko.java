package com.github.onigokko;

import com.github.onigokko.Event.BreakBlock;
import com.github.onigokko.Event.PlayerConnection;
import com.github.onigokko.Event.PlayerDamage;
import com.github.onigokko.commands.*;
import com.github.onigokko.games.GameManager;
import com.github.onigokko.games.OniManager;
import com.github.onigokko.games.StartPointManager;
import com.github.onigokko.score.ScoreboardManager;
import com.github.onigokko.score.TeamManager;
import com.github.onigokko.score.Timer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
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
    private OniManager oniManager;

    private static Onigokko instance;

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        //BukkitAPI
        this.plManager = getServer().getPluginManager();

        //コンストラクタ生成(クラス)
        this.sbManager = new ScoreboardManager();
        this.oniManager = new OniManager();
        this.teamManager = new TeamManager(sbManager, oniManager);
        this.spManager = new StartPointManager();
        this.gameManager = new GameManager(teamManager, sbManager, spManager);
        this.timer = new Timer(this, gameManager, spManager, oniManager, teamManager);


        //イベント登録
        plManager.registerEvents(new PlayerConnection(sbManager, teamManager, timer),this);
        plManager.registerEvents(new PlayerDamage(teamManager, gameManager), this);
        plManager.registerEvents(new BreakBlock(), this);


        //コマンド登録
        getCommand("setgametime").setExecutor(new setGameTime(timer));
        getCommand("setgamemode").setExecutor(new setGameMode(gameManager));
        getCommand("startgame").setExecutor(new startGame(gameManager, timer, spManager, teamManager));
        getCommand("stopgame").setExecutor(new stopGame(timer));
        getCommand("setteam").setExecutor(new setTeamToPlayer(gameManager, teamManager));
        getCommand("setstart").setExecutor(new setStartPoint(spManager));

        //起動通知
        Bukkit.getLogger().info("============================================");
        Bukkit.getLogger().info("[Onigokko-info]  鬼ごっこプラグインが起動しました。");
        Bukkit.getLogger().info("[Onigokko-info] Onigokko Plugin has started.");
        Bukkit.getLogger().info("============================================");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        //プラグインで作成したスコアボード及びチームの削除
        teamManager.removeTeams();
        sbManager.removeScoreboard();
        //プラグインで作成したアーマースタンドの削除
        spManager.removeStartPoint();
        //プラグインで作成したボスバーの削除
        timer.removeBossbar();

        //停止通知
        Bukkit.getLogger().info("[Onigokko-info]  鬼ごっこプラグインが停止しました /\n" +
                " Onigokko Plugin has finished.");
    }

    public static Plugin getInstance() {
        return instance;
    }
}
