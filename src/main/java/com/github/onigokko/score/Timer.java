package com.github.onigokko.score;

import com.github.onigokko.games.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer {

    private final Plugin plugin;
    private final ScoreboardManager sbManager;
    private final GameManager gameManager;

    private BukkitRunnable timerTask;
    private int time = 0;
    private int timeCount;

    public Timer(Plugin plugin,ScoreboardManager sbManager, GameManager gameManager) {
        this.plugin = plugin;
        this.sbManager = sbManager;
        this.gameManager = gameManager;
        sbManager.setScore(ChatColor.GREEN + "ゲーム時間: 未設定",0,9);
    }
    //時間の設定
    public void setTime(int time) {
        this.time = time;
        sbManager.setScore(ChatColor.GREEN + "ゲーム時間: %d秒", time, 9);
    }
    public int getTime() {
        return time;
    }

    public void startTimer() {
        if (timerTask != null) {
            return; //すでにスタートしている場合は開始しない
        }
        sbManager.removeScore(ChatColor.GREEN + "ゲーム時間: %d秒");//ゲームスタート時に非ゲーム時表記の削除
        timeCount = time;//カウントダウンに設定時間をセット
        timer();//タイマー起動
        gameManager.setGameStart(true);//ゲームを進行中にする
    }
    //タイマー本体
    private void timer() {

        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (timeCount <= 0) {
                    // 時間切れ処理

                    stopTimer();

                    //以下ゲームの終了処理
                    gameManager.getGameModeManager().endGame();//現在のゲームモードクラスのend処理呼び出し
                } else {
                    sbManager.setScore(ChatColor.GREEN + "残り時間: %d秒", timeCount, 9);
                    timeCount--;
                }
            }
        };

        timerTask.runTaskTimer(plugin, 0L, 20L);
    }

    public void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
            sbManager.removeScore(ChatColor.GREEN + "残り時間: %d秒");//ゲーム時の時間表記の削除
            setTime(time);//ゲーム時間を設定値に戻す
            gameManager.setGameStart(false);//ゲームが進行中ではなくする
        }
    }

}
