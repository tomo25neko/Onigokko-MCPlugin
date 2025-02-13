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
    private final ScoreboardManager sbManager;
    private final GameManager gameManager;
    private final StartPointManager spManager;

    private BukkitRunnable mainTimerTask, escapeTimerTask;
    private int escapeTime;
    private int time = 0;
    private int timeCount;

    private final BossBar timerBossBar;

    public Timer(Plugin plugin, ScoreboardManager sbManager, GameManager gameManager, StartPointManager spManager) {
        this.plugin = plugin;
        this.sbManager = sbManager;
        this.gameManager = gameManager;
        this.spManager = spManager;
//        setEmptyLine(2);//空行２行作成
//        sbManager.setScore(ChatColor.GREEN + "ゲーム時間：  未設定",0,1);
        //ボスバーの初期化
        timerBossBar = Bukkit.createBossBar(ChatColor.GREEN + "ゲーム時間：  未設定", BarColor.GREEN, BarStyle.SOLID);
    }
    //時間の設定
    public void setTime(int time) {
        this.time = time;
//        sbManager.setScore(ChatColor.GREEN + "ゲーム時間：  %d秒", time, 1);
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
//        sbManager.removeScore(ChatColor.GREEN + "ゲーム時間：  ");//ゲームスタート時に非ゲーム時表記の削除
        timeCount = time;//カウントダウンに設定時間をセット
        escapeTime = 30;//逃げるための時間をリセット
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
//                    sbManager.setScore(ChatColor.YELLOW + "鬼の出現まで： %d秒", escapeTime, 2);
                    timerBossBar.setTitle(ChatColor.YELLOW + "鬼の出現まで："+ escapeTime + "秒");
                    escapeTime--;
                } else {
                    // 逃げる時間終了時の処理
                    spManager.teleportTeam(team); // 鬼チームのプレイヤーをスタート地点へ転送
//                    //いらないスコアの削除
//                    sbManager.removeScore(ChatColor.YELLOW + "逃げる時間： %d秒");
                    stopTimer();//escapeTimerの初期化(null)
                    // 逃げる時間終了後にメインのゲームカウントダウンを開始
                    startMainTimer();
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
                } else {
//                    sbManager.setScore(ChatColor.GREEN + "残り時間：  %d秒", timeCount, 1);//空白全角１
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
//            sbManager.removeScore(ChatColor.GREEN + "残り時間:  ");//ゲーム時の時間表記の削除
            setTime(time);//ゲーム時間を設定値に戻す
        }

        if (escapeTimerTask != null) {
            escapeTimerTask.cancel();
            escapeTimerTask = null;
        }

        gameManager.setGameStart(false);//ゲームが進行中ではなくする
    }

//    //指定した値の数だけ
//    private void setEmptyLine(int set) {
//        for (int i = 1; i <= set; i++) {
//            sbManager.setScore("",0, i);
//        }
//    }


}
