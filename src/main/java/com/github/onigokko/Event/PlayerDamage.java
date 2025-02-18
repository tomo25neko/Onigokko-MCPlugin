package com.github.onigokko.Event;

import com.github.onigokko.games.GameManager;
import com.github.onigokko.games.GameModeManager;
import com.github.onigokko.score.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamage implements Listener{

    private final TeamManager teamManager;
    private final GameManager gameManager;

    public PlayerDamage(TeamManager teamManager, GameManager gameManager) {
        this.teamManager = teamManager;
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerDamageFromEntity(EntityDamageByEntityEvent event) {
        // ダメージを受けたのがプレイヤーか確認
        if(!(event.getEntity() instanceof Player damagedPlayer)) {
            return;
        }
        // 攻撃したのがプレイヤーか確認
        if (!(event.getDamager() instanceof Player attacker)) {
            return;
        }
        //今がゲーム中か確認
        if (!(gameManager.isGameStart())) {
            return;
        }

        //攻撃者が逃げなら キャンセル
        if (teamManager.getNige().hasEntry(attacker.getName())) {
            event.setCancelled(true);
        }

        // 鬼チームのプレイヤーが、逃げチームのプレイヤーを攻撃した場合
        if (teamManager.getOni().hasEntry(attacker.getName()) && teamManager.getNige().hasEntry(damagedPlayer.getName())) {
            GameModeManager gm = gameManager.getGameModeManager();//使い回すための代入
            //ゲームモード固有の処理を呼び出す
            gm.caughtPlayer(attacker,damagedPlayer);

            if ((teamManager.getNige().getSize() == 0)) gm.endGame();//もし逃げチームが全滅なら終了
        }

    }

    //プレイヤー以外からのダメージ
    @EventHandler
    public void onPlayerGetDamage(EntityDamageEvent event) {
        //落下ダメージ無効・溺死ダメージ無効
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL ||
                event.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
            event.setCancelled(true);//落下ダメージ無効
        }
    }

}
