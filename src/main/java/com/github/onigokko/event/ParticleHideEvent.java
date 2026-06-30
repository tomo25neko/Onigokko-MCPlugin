package com.github.onigokko.event;

import com.github.onigokko.games.HideballManager;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerParticle;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * 透明化中のプレイヤーのパーティクルを他プレイヤーに送信しないようにする
 */
public class ParticleHideEvent extends PacketListenerAbstract {

    private final HideballManager hideballManager;

    public ParticleHideEvent(HideballManager hideballManager) {
        this.hideballManager = hideballManager;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.PARTICLE) {
            return;
        }

        Player receiver = event.getPlayer();
        WrapperPlayServerParticle particlePacket = new WrapperPlayServerParticle(event);

        // パーティクルの座標を取得
        double packetX = particlePacket.getPosition().getX();
        double packetY = particlePacket.getPosition().getY();
        double packetZ = particlePacket.getPosition().getZ();

        // 全プレイヤーをチェック
        for (Player player : receiver.getServer().getOnlinePlayers()) {
            // 影玉効果中のプレイヤーの場合、そのプレイヤーから発するパーティクルをブロック
            if (hideballManager.isInvisible(player)) {
                // プレイヤーの位置とパーティクルの位置が近い場合、そのプレイヤーのパーティクルと判断
                double distance = player.getLocation().distanceSquared(
                        new Location(player.getWorld(), packetX, packetY, packetZ));

                // 2ブロック以内ならそのプレイヤーのパーティクルとみなしてブロック
                if (distance < 4.0) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
