package com.github.onigokko.event;

import com.github.onigokko.games.HideballManager;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * 透明化中のプレイヤーのエンティティメタデータを制御
 * - パーティクル非表示フラグ
 * - 発光状態制御
 */
public class InvisibleEntityMetadataListener extends PacketListenerAbstract {

    private final HideballManager hideballManager;

    public InvisibleEntityMetadataListener(HideballManager hideballManager) {
        this.hideballManager = hideballManager;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.ENTITY_METADATA) {
            return;
        }

        WrapperPlayServerEntityMetadata metadataPacket = new WrapperPlayServerEntityMetadata(event);
        int entityId = metadataPacket.getEntityId();

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
            // 自分自身には通常のメタデータを送信
            if (event.getPlayer().equals(target)) {
                return;
            }

            try {
                List<EntityData<?>> entityDataList = metadataPacket.getEntityMetadata();
                for (EntityData<?> entityData : entityDataList) {
                    // インデックス0のバイトフラグ（パーティクル非表示、発光状態など）
                    if (entityData.getIndex() == 0 && entityData.getType() == EntityDataTypes.BYTE) {
                        byte currentFlags = (byte) entityData.getValue();
                        // パーティクル非表示フラグを設定（Byteの0x04ビット）
                        byte newFlags = (byte) (currentFlags | 0x04);
                        // 発光状態を解除（Glowingフラグをクリア）
                        newFlags = (byte) (newFlags & ~0x40);
                        ((EntityData<Byte>) entityData).setValue(newFlags);
                    }
                }
                metadataPacket.setEntityMetadata(entityDataList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
