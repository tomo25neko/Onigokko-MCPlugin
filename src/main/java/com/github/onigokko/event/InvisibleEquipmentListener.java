package com.github.onigokko.event;

import com.github.onigokko.games.HideballManager;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * 透明化中のプレイヤーの装備を他プレイヤーにはAIRとして送信する
 */
public class InvisibleEquipmentListener extends PacketListenerAbstract {

    private final HideballManager hideballManager;

    public InvisibleEquipmentListener(HideballManager hideballManager) {
        this.hideballManager = hideballManager;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.ENTITY_EQUIPMENT) {
            return;
        }

        Player viewer = event.getPlayer();
        WrapperPlayServerEntityEquipment equipmentPacket = new WrapperPlayServerEntityEquipment(event);
        int entityId = equipmentPacket.getEntityId();

        // エンティティIDからプレイヤーを取得
        Player target = null;
        for (Player player : viewer.getServer().getOnlinePlayers()) {
            if (player.getEntityId() == entityId) {
                target = player;
                break;
            }
        }

        if (target == null) {
            return;
        }

        // 自分自身には見せる
        if (viewer.equals(target)) {
            return;
        }

        // 影玉効果中（透明化中）でなければ何もしない
        if (!hideballManager.isInvisible(target)) {
            return;
        }

        // 装備をAIRに置換
        List<Equipment> equipmentList = equipmentPacket.getEquipment();
        for (Equipment equipment : equipmentList) {
            EquipmentSlot slot = equipment.getSlot();
            if (slot == EquipmentSlot.MAIN_HAND || slot == EquipmentSlot.OFF_HAND ||
                    slot == EquipmentSlot.HELMET || slot == EquipmentSlot.CHEST_PLATE ||
                    slot == EquipmentSlot.LEGGINGS || slot == EquipmentSlot.BOOTS) {
                equipment.setItem(ItemStack.builder().type(ItemTypes.AIR).amount(1).build());
            }
        }

        equipmentPacket.setEquipment(equipmentList);
    }
}
