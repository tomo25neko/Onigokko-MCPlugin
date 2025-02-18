package com.github.onigokko.Event;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlock implements Listener {

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        // プレイヤーがOPでなければ、ブロック破壊イベントをキャンセルする
        if (!(event.getPlayer().isOp())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.YELLOW + "ブロックを壊さないでね");
        }
    }
}
