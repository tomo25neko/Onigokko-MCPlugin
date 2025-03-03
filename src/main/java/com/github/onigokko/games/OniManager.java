package com.github.onigokko.games;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class OniManager {

    /**
     * 鬼専用の装備（赤色の革の胸当て）をプレイヤーに装備させる。
     * Curse of Binding(束縛の呪)を付与して、誤って捨てないようにする。
     */
    public void applyOniEquipment(Player player) {
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);

        LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();//LeatherArmorMeta型に変換
        if (meta != null) {
            meta.setColor(Color.RED); // 赤色に染色
            meta.setDisplayName("§c鬼の胸当て"); // カスタムネームで識別可能に
            // Curse of Binding を付与して装備が外せなくなる（プレイヤーが意図的にドロップできない）
            meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
            chestplate.setItemMeta(meta);
        }
        player.getInventory().setChestplate(chestplate);
    }

    /**
     * ゲーム終了時などに、鬼専用の装備を解除する。
     */
    public void removeOniEquipment(Player player) {
        // ここではシンプルにチェストプレートを外す処理
        player.getInventory().clear();
    }

    /**
     * タイマー更新（毎秒呼び出し）などから呼べる、鬼プレイヤーのパーティクル表示処理。
     */
    public void displayOniParticles(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        player.getWorld().spawnParticle(
                Particle.REDSTONE,
                player.getLocation().add(0, 1, 0), // 頭上
                5,         // パーティクルの数（少なめ）
                0.5, 0.5, 0.5, // 拡散範囲（控えめ）
                new Particle.DustOptions(Color.RED, 1.0f) // 赤色、サイズ1.0
        );
    }

}
