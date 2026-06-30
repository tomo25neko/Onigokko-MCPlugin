package com.github.onigokko.games;

import com.github.onigokko.Onigokko;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HideballManager {

    private final Map<UUID, BukkitRunnableTask> effectTasks = new HashMap<>(); // プレイヤーごとの効果タスク
    private int duration; // 持続時間（秒）

    /**
     * コンストラクタ
     * デフォルト持続時間（秒）
     */
    public HideballManager() {
        this.duration = 10;
    }


    /**
     * 影玉アイテム（マグマボール）を作成
     */
    public ItemStack createHideballItem() {
        ItemStack item = new ItemStack(Material.MAGMA_CREAM);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_GRAY + "影玉");
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * 影玉の持続時間を取得（秒）
     */
    public int getDuration() {
        return duration;
    }

    /**
     * 影玉の持続時間を設定（秒）
     */
    public void setDuration(int seconds) {
        this.duration = seconds;
    }

    /**
     * プレイヤーの影玉時間を初期化（ゲーム開始時）
     * 手持ちにマグマボールをduration分追加する
     */
    public void initializePlayerTime(Player player) {
        // 既存のマグマボールをクリア
        player.getInventory().remove(createHideballItem());
        // 指定された数だけマグマボールを追加
        ItemStack hideball = createHideballItem();
        hideball.setAmount(duration);
        player.getInventory().addItem(hideball);
    }


    /**
     * プレイヤーに影玉効果を付与
     */
    public void applyHideballEffect(Player player) {
        UUID uuid = player.getUniqueId();

        // 手持ちにマグマボールがない場合は何もしない
        if (!hasRemainingTime(player)) {
            return;
        }

        // 既にタスクが実行中の場合は何もしない（持ち替えても効果は継続）
        if (effectTasks.containsKey(uuid)) {
            return;
        }

        // ポーション効果を付与（透明化 + 移動速度上昇1、パーティクル非表示）
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false, true));
        // カウントダウンタスクを開始
        BukkitRunnableTask task = new BukkitRunnableTask(player, uuid);
        effectTasks.put(uuid, task);
        task.runTaskTimer(Onigokko.getInstance(), 0L, 20L);
    }

    /**
     * プレイヤーの影玉効果を一時停止（アイテムを離したとき）
     */
    public void pauseHideballEffect(Player player) {
        UUID uuid = player.getUniqueId();

        // タスクをキャンセル（時間消費を停止）
        if (effectTasks.containsKey(uuid)) {
            effectTasks.get(uuid).cancel();
            effectTasks.remove(uuid);
        }

        // ポーション効果を解除
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.removePotionEffect(PotionEffectType.SPEED);
    }

    /**
     * プレイヤーの影玉効果を完全解除（ゲーム終了時など）
     */
    public void removeHideballEffect(Player player) {
        UUID uuid = player.getUniqueId();

        // タスクをキャンセル
        if (effectTasks.containsKey(uuid)) {
            effectTasks.get(uuid).cancel();
            effectTasks.remove(uuid);
        }

        // ポーション効果を解除
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.removePotionEffect(PotionEffectType.SPEED);
    }

    /**
     * 全プレイヤーの影玉効果を完全解除（ゲーム終了時）
     */
    public void removeAllEffects() {
        for (UUID uuid : new HashMap<>(effectTasks).keySet()) {
            Player player = org.bukkit.Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                removeHideballEffect(player);
            } else {
                // オフラインプレイヤーの場合はデータのみ削除
                effectTasks.remove(uuid);
            }
        }
    }

    /**
     * 全プレイヤーから影玉アイテムを回収
     */
    public void removeAllHideballItems() {
        for (Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
            player.getInventory().remove(createHideballItem());
        }
    }

    /**
     * プレイヤーが影玉効果中かどうか（タスク実行中）
     */
    public boolean hasHideballEffect(Player player) {
        return effectTasks.containsKey(player.getUniqueId());
    }

    /**
     * プレイヤーが透明化中かどうか（影玉効果中と同じ）
     * 外部からの呼び出し用
     */
    public boolean isInvisible(Player player) {
        return effectTasks.containsKey(player.getUniqueId());
    }

    /**
     * プレイヤーが影玉の残り時間を持っているかどうか
     * 手持ちのマグマボール数で判定
     */
    public boolean hasRemainingTime(Player player) {
        int count = 0;
        ItemStack hideball = createHideballItem();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.isSimilar(hideball)) {
                count += item.getAmount();
            }
        }
        return count > 0;
    }

    /**
     * カウントダウンタスクの内部クラス
     */
    private class BukkitRunnableTask extends org.bukkit.scheduler.BukkitRunnable {
        private final Player player;
        private final UUID uuid;

        public BukkitRunnableTask(Player player, UUID uuid) {
            this.player = player;
            this.uuid = uuid;
        }

        @Override
        public void run() {
            if (!player.isOnline()) {
                cancel();
                return;
            }

            // 手持ちのマグマボールを1つ消費
            ItemStack hideball = createHideballItem();
            boolean consumed = false;
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.isSimilar(hideball)) {
                    item.setAmount(item.getAmount() - 1);
                    if (item.getAmount() <= 0) {
                        player.getInventory().clear(player.getInventory().first(item));
                    }
                    consumed = true;
                    break;
                }
            }

            // マグマボールがなくなった場合は効果を完全解除
            if (!consumed) {
                removeHideballEffect(player);
            }
        }
    }
}
