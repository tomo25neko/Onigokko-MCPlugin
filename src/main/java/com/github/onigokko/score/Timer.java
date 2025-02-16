package com.github.onigokko.score;

import com.github.onigokko.games.GameManager;
import com.github.onigokko.games.StartPointManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

public class Timer {

    private final Plugin plugin;
    private final GameManager gameManager;
    private final StartPointManager spManager;
    private final TeamManager teamManager;

    private BukkitRunnable mainTimerTask, escapeTimerTask;
    private int escapeTime;
    private int time = 0;
    private int timeCount;

    private final BossBar timerBossBar;

    public Timer(Plugin plugin, GameManager gameManager, StartPointManager spManager, TeamManager teamManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
        this.spManager = spManager;
        this.teamManager = teamManager;


        //ボスバーの初期化
        timerBossBar = Bukkit.createBossBar(ChatColor.GREEN + "ゲーム時間：  未設定", BarColor.GREEN, BarStyle.SOLID);
    }
    //時間の設定
    public void setTime(int time) {
        this.time = time;
        timerBossBar.setTitle(ChatColor.GREEN + "ゲーム時間： " + time + "秒");
    }
    public int getTime() {
        return time;
    }

    //プレイヤーにボスバーを設定
    public void showBossbar(Player player) {
        timerBossBar.addPlayer(player);
    }
    //ボスバー削除
    public void removeBossbar() {
        timerBossBar.removeAll();
    }

    public void startTimer(Team team) {
        if (mainTimerTask != null || escapeTimerTask != null) {
            return; //すでにスタートしている場合は開始しない
        }
        timeCount = time;//カウントダウンに設定時間をセット
        escapeTime = 30;//逃げるための時間をリセット
        //鬼が出現するまでのタイマーを起動
        startEscapeCountdown(team);//
        gameManager.setGameStart(true);//ゲームを進行中にする
    }

    /*
     * 逃げるためのカウントダウン処理
     * @param escapeDelaySeconds 逃げる猶予時間（秒）
     */
    private void startEscapeCountdown(Team team) {
        escapeTimerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (escapeTime > 0) {
                    // 逃げるための残り時間を表示
                    timerBossBar.setTitle(ChatColor.YELLOW + "鬼の出現まで："+ escapeTime + "秒");
                    escapeTime--;
                } else {
                    // 逃げる時間終了時の処理
                    spManager.teleportTeam(team); // 鬼チームのプレイヤーをスタート地点へ転送
                    gameManager.getGameModeManager().releaseHunter(); //ゲームモードの鬼がスタートしたときのメッセージを送信

                    stopTimer();//escapeTimerの初期化(null)
                    // 逃げる時間終了後にメインのゲームカウントダウンを開始
                    startMainTimer();
                    teamManager.setOniAttack(true);//逃げチームに攻撃可能にする
                }
            }
        };

        escapeTimerTask.runTaskTimer(plugin, 0L, 20L);
    }

    //タイマー本体
    private void startMainTimer() {

        mainTimerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (timeCount <= 0) {
                    // 時間切れ処理

                    stopTimer();

                    //以下ゲームの終了処理
                    gameManager.getGameModeManager().endGame();//現在のゲームモードクラスのend処理呼び出し
                    teamManager.setOniAttack(false);
                } else {
                    timerBossBar.setTitle(ChatColor.GREEN + "残り時間： " + timeCount +"秒");
                    timeCount--;
                }
            }
        };

        mainTimerTask.runTaskTimer(plugin, 0L, 20L);
    }

    public void stopTimer() {

        if (mainTimerTask != null) {
            mainTimerTask.cancel();
            mainTimerTask = null;
            setTime(time);//ゲーム時間を設定値に戻す
        }

        if (escapeTimerTask != null) {
            escapeTimerTask.cancel();
            escapeTimerTask = null;
        }

        gameManager.setGameStart(false);//ゲームが進行中ではなくする
    }

}
