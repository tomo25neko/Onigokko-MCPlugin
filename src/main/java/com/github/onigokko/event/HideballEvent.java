package com.github.onigokko.event;

import com.github.onigokko.games.GameManager;
import com.github.onigokko.games.HideballManager;
import com.github.onigokko.score.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public record HideballEvent(HideballManager hideballManager, TeamManager teamManager) implements Listener {

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        // ゲーム中でなければ無視
        if (!GameManager.isGameStart()) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        ItemStack oldItem = player.getInventory().getItem(event.getPreviousSlot());

        // 影玉を手に持った場合（逃げプレイヤーのみ使用可能）
        if (isHideballItem(newItem)) {
            if (isNigePlayer(player)) {
                hideballManager.applyHideballEffect(player);
            }
        }

        // 影玉を離した場合
        if (isHideballItem(oldItem)) {
            hideballManager.pauseHideballEffect(player);
        }
    }

    /**
     * プレイヤーが逃げチームかどうかを判定
     */
    private boolean isNigePlayer(Player player) {
        return teamManager.getNige().hasEntry(player.getName());
    }

    /**
     * アイテムが影玉かどうかを判定
     */
    private boolean isHideballItem(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            String displayName = item.getItemMeta().getDisplayName();
            return displayName.equals("§8影玉");
        }
        return false;
    }
}
