package com.github.onigokko;

import com.github.onigokko.commands.*;
import com.github.onigokko.event.*;
import com.github.onigokko.games.*;
import com.github.onigokko.score.ScoreboardManager;
import com.github.onigokko.score.TeamManager;
import com.github.onigokko.score.Timer;
import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Onigokko extends JavaPlugin {

    private static Onigokko instance;
    //プラグインコンストラクタ
    private ScoreboardManager sbManager;
    private TeamManager teamManager;
    private StartPointManager spManager;
    private Timer timer;

    public static Plugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        // PacketEventsの初期化
        PacketEvents.getAPI().load();
        PacketEvents.getAPI().init();

        //BukkitAPI
        //Bukkit　プラグインマネージャー(イベント登録)
        PluginManager plManager = getServer().getPluginManager();

        //コンストラクタ生成(クラス)
        this.sbManager = new ScoreboardManager();
        OniManager oniManager = new OniManager();
        this.teamManager = new TeamManager(sbManager, oniManager);
        this.spManager = new StartPointManager();
        HideballManager hideballManager = new HideballManager();

        // Factoryの生成
        GameModeFactory gameModeFactory = new GameModeFactory(teamManager, sbManager, spManager);
        GameManager gameManager = new GameManager(gameModeFactory);
        this.timer = new Timer(this, gameManager, spManager, oniManager, teamManager);


        //イベント登録
        plManager.registerEvents(new PlayerConnection(sbManager, teamManager, timer), this);
        plManager.registerEvents(new PlayerDamage(teamManager, gameManager), this);
        plManager.registerEvents(new BreakBlock(), this);
        plManager.registerEvents(new HideballEvent(hideballManager, teamManager), this);

        // PacketEventsリスナー登録
        PacketEvents.getAPI().getEventManager().registerListener(new InvisibleEquipmentListener(hideballManager));
        PacketEvents.getAPI().getEventManager().registerListener(new InvisibleEntityMetadataListener(hideballManager));
        PacketEvents.getAPI().getEventManager().registerListener(new InvisibleEntityStatusListener(hideballManager));
        PacketEvents.getAPI().getEventManager().registerListener(new ParticleHideEvent(hideballManager));


        //コマンド登録
        getCommand("setgametime").setExecutor(new setGameTime(timer));
        getCommand("setgamemode").setExecutor(new setGameMode(gameManager));
        getCommand("startgame").setExecutor(new startGame(gameManager, timer, spManager, teamManager, hideballManager));
        getCommand("stopgame").setExecutor(new stopGame(timer, hideballManager));
        getCommand("setteam").setExecutor(new setTeamToPlayer(teamManager));
        getCommand("setstart").setExecutor(new setStartPoint(spManager));
        getCommand("help").setExecutor(new help(gameManager, timer, spManager, teamManager));
        getCommand("returntostart").setExecutor(new returnToStart(spManager, teamManager));
        getCommand("sethideballduration").setExecutor(new setHideballDuration(hideballManager));

        //起動通知
        Bukkit.getLogger().info("============================================");
        Bukkit.getLogger().info("[Onigokko-info]  鬼ごっこプラグインが起動しました。");
        Bukkit.getLogger().info("[Onigokko-info] Onigokko Plugin has started.");
        Bukkit.getLogger().info("============================================");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // PacketEventsの終了処理
        PacketEvents.getAPI().terminate();

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
}
