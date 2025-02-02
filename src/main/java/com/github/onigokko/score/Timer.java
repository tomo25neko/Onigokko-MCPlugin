package com.github.onigokko.score;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer {

    private final Plugin plugin;
    private final ScoreboardManager sbManager;

    private BukkitRunnable timerTask;
    private int time = 0;

    public Timer(Plugin plugin,ScoreboardManager sbManager) {
        this.plugin = plugin;
        this.sbManager = sbManager;
        sbManager.setScore("ゲーム時間:未設定",0,9);
    }
    //時間の設定
    public void setTime(int time) {
        this.time = time;
        sbManager.setScore("ゲーム時間: %d秒", time, 9);
    }

    public boolean startTimer() {
        if (timerTask != null) {
            return false;//すでにスタートしている場合は開始しない
        }

        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (time <= 0) {
                    // 時間切れ処理

                    sbManager.setScore("残り時間: 0秒", 0, 9);//確定、変更しない
                    stopTimer();
                    //ここにゲームの終了処理の記述予定。
                } else {
                    sbManager.setScore("残り時間: %d秒", time, 9);
                    time--;
                }
            }
        };

        timerTask.runTaskTimer(plugin, 0L, 20L);

        return true;
    }

    public void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

}
