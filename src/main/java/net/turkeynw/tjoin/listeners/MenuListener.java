package net.turkeynw.tjoin.listeners;

import net.turkeynw.tjoin.TJoin;
import net.turkeynw.tjoin.menus.MainMenu;
import net.turkeynw.tjoin.models.Dungeon;
import net.turkeynw.tjoin.models.RequirementType;
import net.turkeynw.tjoin.utils.FormatUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class MenuListener implements Listener {

    private final TJoin plugin = TJoin.getInstance();
    private final NamespacedKey menuDungeonKey = new NamespacedKey(plugin, "dungeon_id");

    private void sendMessage(Player player, String path) {
        String prefix = plugin.getConfig().getString("messages.prefix", "");
        String message = plugin.getConfig().getString("messages." + path, "");
        player.sendMessage(FormatUtils.format(prefix + message));
    }

    private void sendMessageReplaced(Player player, String path, String target, String replacement) {
        String prefix = plugin.getConfig().getString("messages.prefix", "");
        String message = plugin.getConfig().getString("messages." + path, "").replace(target, replacement);
        player.sendMessage(FormatUtils.format(prefix + message));
    }

    private void playSound(Player player, String soundType) {
        if (!plugin.getConfig().getBoolean("settings.sounds.enabled", true)) return;
        try {
            String soundName = plugin.getConfig().getString("settings.sounds." + soundType);
            if (soundName != null) {
                Sound sound = Registry.SOUNDS.get(NamespacedKey.minecraft(soundName.toLowerCase()));
                if (sound != null) {
                    player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
                }
            }
        } catch (Exception ignored) {}
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof MainMenu) {
            event.setCancelled(true);

            if (!(event.getWhoClicked() instanceof Player player)) return;
            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

            String dungeonId = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(menuDungeonKey, PersistentDataType.STRING);
            if (dungeonId == null) return;

            Dungeon dungeon = plugin.getDungeonManager().getDungeon(dungeonId);
            if (dungeon == null) return;

            if (dungeon.getLocation() == null) {
                sendMessage(player, "dungeon_no_location");
                playSound(player, "error");
                player.closeInventory();
                return;
            }

            player.closeInventory();
            sendMessage(player, "checking_data");

            plugin.getDatabaseManager().getStorage().getCooldown(player.getUniqueId(), dungeonId).thenAccept(expireTime -> {
                long currentTime = System.currentTimeMillis();

                if (!player.hasPermission("tjoin.bypass.cooldown") && currentTime < expireTime) {
                    long timeLeft = (expireTime - currentTime) / 1000;
                    long minutes = (timeLeft % 3600) / 60;
                    long seconds = timeLeft % 60;

                    String timeStr = minutes + " ᴅᴀᴋɪᴋᴀ " + seconds + " ꜱᴀɴiʏᴇ";
                    player.getScheduler().run(plugin, task -> {
                        sendMessageReplaced(player, "dungeon_cooldown", "%time%", timeStr);
                        playSound(player, "error");
                    }, null);
                    return;
                }

                player.getScheduler().run(plugin, task -> processDungeonEntry(player, dungeon), null);
            });
        }
    }

    private void processDungeonEntry(Player player, Dungeon dungeon) {
        if (dungeon.getRequirementType() == RequirementType.MONEY) {
            double cost = dungeon.getMoneyCost();


            if (plugin.getVaultManager().getEconomy() == null || plugin.getVaultManager().getEconomy().getBalance(player.getName()) < cost) {
                sendMessageReplaced(player, "money_not_enough", "%cost%", String.valueOf(cost));
                playSound(player, "error");
                return;
            }
            plugin.getVaultManager().getEconomy().withdrawPlayer(player.getName(), cost);
            sendMessageReplaced(player, "money_paid", "%cost%", String.valueOf(cost));
        }
        else if (dungeon.getRequirementType() == RequirementType.KEY) {
            String requiredKeyId = dungeon.getKeyId();
            if (requiredKeyId == null || requiredKeyId.isEmpty()) requiredKeyId = dungeon.getId();

            boolean foundKey = false;
            ItemStack[] contents = player.getInventory().getContents();

            for (int i = 0; i < contents.length; i++) {
                ItemStack item = contents[i];
                if (item == null || !item.hasItemMeta()) continue;

                ItemMeta meta = item.getItemMeta();
                String itemKeyId = meta.getPersistentDataContainer().get(plugin.getKeyManager().keyTag, PersistentDataType.STRING);

                if (itemKeyId != null && itemKeyId.equals(requiredKeyId)) {
                    foundKey = true;
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                    } else {
                        player.getInventory().setItem(i, null);
                    }
                    break;
                }
            }

            if (!foundKey) {
                sendMessage(player, "key_not_found");
                playSound(player, "error");
                return;
            }
            sendMessage(player, "key_used");
        }

        sendMessage(player, "teleporting");
        playSound(player, "success");

        player.teleportAsync(dungeon.getLocation()).thenAccept(success -> {
            if (success) {
                playSound(player, "teleport");
                if (dungeon.getCooldownSeconds() > 0) {
                    long newExpireTime = System.currentTimeMillis() + (dungeon.getCooldownSeconds() * 1000L);
                    plugin.getDatabaseManager().getStorage().setCooldown(player.getUniqueId(), dungeon.getId(), newExpireTime);
                }
            } else {
                player.getScheduler().run(plugin, task -> {
                    sendMessage(player, "teleport_failed");
                    playSound(player, "error");
                }, null);
            }
        });
    }
}