package com.github.onigokko.games;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class StartPointManager {

    private final String START_POINT_NAME = "スタート地点";


    /*
      スタート地点を設定する
      @param player 実行したプレイヤー
     */
    public void setStartPoint(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();

        //既存のスタート地点を削除
        removeStartPoint();

        //アーマースタンドの設置
        ArmorStand stand = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
        //名前設定
        stand.setCustomName(START_POINT_NAME);
        //スタンドが見えないようにする
        stand.setInvisible(true);
        //名前は見える
        stand.setCustomNameVisible(true);
        //重力の影響を受ける(空中に設置した場合に備えて)
        stand.setGravity(true);
        //無敵
        stand.setInvulnerable(true);

    }

    /*
     * スタート地点の座標を取得
     * @param world スタート地点を探すワールド
     * @return スタート地点の座標、存在しない場合は null
     */
    public Location getStartPoint(World world) {
        for (Entity entity : world.getEntities()) {
            //Entityがアーマースタンドかつ"スタート地点"の名前なら
            if (entity instanceof ArmorStand stand) {
                if (START_POINT_NAME.equals(stand.getCustomName())) {
                    return stand.getLocation();//スタンドの位置を返す
                }
            }
        }
        return null; // 見つからない場合
    }

    /*
     * スタート地点を削除
     * @param world 削除するワールド
     */
    public void removeStartPoint() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof ArmorStand stand) {
                    if (START_POINT_NAME.equals(stand.getCustomName())) {
                        stand.remove();
                    }
                }
            }
        }

    }

    /*
     * プレイヤーをスタート地点へtp
     *  @param team テレポート対象のチーム
     */

    public void teleportTeam(Team team) {
        for (String playerName : team.getEntries()) {
            //StringからPlayerへ型変換
            Player player = Bukkit.getPlayer(playerName);
            //プレイヤーを現在のワールドのスタートポイントにテレポート
            player.teleport(getStartPoint(player.getWorld()));
        }
    }

    public void teleportPlayer(Player player) {
        //プレイヤーを現在のワールドのスタートポイントにテレポート
        player.teleport(getStartPoint(player.getWorld()));

    }
}
