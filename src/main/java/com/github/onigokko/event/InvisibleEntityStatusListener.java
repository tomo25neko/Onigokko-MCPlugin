package com.github.onigokko.event;

import com.github.onigokko.games.HideballManager;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityStatus;
import org.bukkit.entity.Player;

/**
 * 透明化中のプレイヤーのエンティティステータスを制御
 * - 矢が刺さっている表示の非表示
 */
public class InvisibleEntityStatusListener extends PacketListenerAbstract {

    private final HideballManager hideballManager;

    public InvisibleEntityStatusListener(HideballManager hideballManager) {
        this.hideballManager = hideballManager;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.ENTITY_STATUS) {
            return;
        }

        WrapperPlayServerEntityStatus statusPacket = new WrapperPlayServerEntityStatus(event);
        int entityId = statusPacket.getEntityId();
        int status = statusPacket.getStatus();

        // エンティティIDからプレイヤーを取得
        Player target = null;
        for (Player player : ((Player) event.getPlayer()).getServer().getOnlinePlayers()) {
            if (player.getEntityId() == entityId) {
                target = player;
                break;
            }
        }

        if (target == null) {
            return;
        }

        // 影玉効果中のプレイヤーの場合
        if (hideballManager.isInvisible(target)) {
            // 自分自身には通常のステータスを送信
            if (event.getPlayer().equals(target)) {
                return;
            }

            // 矢が刺さっているステータス（status = 0）をブロック
            if (status == 0) {
                event.setCancelled(true);
            }
        }
    }
}
