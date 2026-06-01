package net.turkeynw.tjoin.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.turkeynw.tjoin.TJoin;
import net.turkeynw.tjoin.menus.AdminMenu;
import net.turkeynw.tjoin.menus.EditMenu;
import net.turkeynw.tjoin.models.Dungeon;
import net.turkeynw.tjoin.models.RequirementType;
import net.turkeynw.tjoin.utils.FormatUtils;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AdminListener implements Listener {

    private final TJoin plugin = TJoin.getInstance();
    private final NamespacedKey adminActionKey = new NamespacedKey(plugin, "admin_action");
    private final NamespacedKey editActionKey = new NamespacedKey(plugin, "edit_action");
    private final NamespacedKey dungeonKey = new NamespacedKey(plugin, "dungeon_id");
    private final NamespacedKey setterKey = new NamespacedKey(plugin, "loc_setter");
    private final Map<UUID, SessionData> activeSessions = new HashMap<>();

    private static class SessionData {
        String action;
        String dungeonId;
        SessionData(String action, String dungeonId) {
            this.action = action;
            this.dungeonId = dungeonId;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof AdminMenu) {
            event.setCancelled(true);
            if (!(event.getWhoClicked() instanceof Player player)) return;
            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

            ItemMeta meta = event.getCurrentItem().getItemMeta();
            if (meta.getPersistentDataContainer().has(adminActionKey, PersistentDataType.STRING)) {
                player.closeInventory();
                activeSessions.put(player.getUniqueId(), new SessionData("CREATE", null));
                player.sendMessage(FormatUtils.format("§a[ᴛᴊᴏiɴ] ʏᴇɴi ᴢiɴᴅᴀɴ ɪᴅ'ꜱiɴi ꜱᴏʜʙᴇᴛᴇ ʏᴀᴢɪɴ (iᴘᴛᴀʟ iᴄiɴ 'iᴘᴛᴀʟ' ʏᴀᴢɪɴ):"));
                return;
            }
            String dungeonId = meta.getPersistentDataContainer().get(dungeonKey, PersistentDataType.STRING);
            if (dungeonId != null) {
                Dungeon dungeon = plugin.getDungeonManager().getDungeon(dungeonId);
                if (dungeon != null) player.openInventory(new EditMenu(player, dungeon).getInventory());
            }
        }
        else if (event.getInventory().getHolder() instanceof EditMenu) {
            event.setCancelled(true);
            if (!(event.getWhoClicked() instanceof Player player)) return;
            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

            ItemMeta meta = event.getCurrentItem().getItemMeta();
            String action = meta.getPersistentDataContainer().get(editActionKey, PersistentDataType.STRING);
            String dungeonId = meta.getPersistentDataContainer().get(dungeonKey, PersistentDataType.STRING);

            if (action == null || dungeonId == null) return;
            Dungeon dungeon = plugin.getDungeonManager().getDungeon(dungeonId);
            if (dungeon == null) return;

            if (action.equals("edit_name")) {
                player.closeInventory();
                activeSessions.put(player.getUniqueId(), new SessionData("EDIT_NAME", dungeonId));
                player.sendMessage(FormatUtils.format("§a[ᴛᴊᴏiɴ] ᴢiɴᴅᴀɴɪɴ ʏᴇɴi ɢöʀüɴᴇɴ iꜱᴍiɴi ꜱᴏʜʙᴇᴛᴇ ʏᴀᴢɪɴ:"));
            }
            else if (action.equals("edit_cooldown")) {
                player.closeInventory();
                activeSessions.put(player.getUniqueId(), new SessionData("EDIT_COOLDOWN", dungeonId));
                player.sendMessage(FormatUtils.format("§a[ᴛᴊᴏiɴ] ʏᴇɴi ʙᴇᴋʟᴇᴍᴇ ꜱüʀᴇꜱiɴi ꜱᴀɴiʏᴇ ᴄiɴꜱiɴᴅᴇɴ ꜱᴏʜʙᴇᴛᴇ ʏᴀᴢɪɴ:"));
            }
            else if (action.equals("edit_req")) {
                RequirementType current = dungeon.getRequirementType();
                RequirementType next = current == RequirementType.FREE ? RequirementType.MONEY : (current == RequirementType.MONEY ? RequirementType.KEY : RequirementType.FREE);
                dungeon.setRequirementType(next);
                saveDungeonField(dungeonId, "requirement.type", next.name());
                plugin.getDungeonManager().loadDungeons();
                player.openInventory(new EditMenu(player, dungeon).getInventory());
            }
            else if (action.equals("edit_loc")) {
                player.closeInventory();
                ItemStack setter = new ItemStack(org.bukkit.Material.ENDER_PEARL);
                ItemMeta smeta = setter.getItemMeta();
                smeta.displayName(FormatUtils.format("§d§lʟᴏᴋᴀꜱʏᴏɴ iɴᴄiꜱi §7(" + dungeonId + ")"));
                smeta.getPersistentDataContainer().set(setterKey, PersistentDataType.STRING, dungeonId);
                setter.setItemMeta(smeta);
                player.getInventory().addItem(setter);
                player.sendMessage(FormatUtils.format("§a[ᴛᴊᴏiɴ] ᴇʟiɴᴇ ᴠᴇʀiʟᴇɴ iɴᴄiʏᴇ ꜱᴀɢ ᴛɪᴋʟᴀʏᴀʀᴀᴋ ʙᴜʟᴜɴᴅᴜɢᴜɴ ᴋᴏɴᴜᴍᴜ ᴢiɴᴅᴀɴ ʙᴀꜱʟᴀɴɢɪᴄɪ ʏᴀᴘᴀʙiʟiʀꜱiɴ."));
            }
            else if (action.equals("edit_delete")) {
                player.closeInventory();
                plugin.getFileManager().getDungeonsConfig().getConfig().set("dungeons." + dungeonId, null);
                plugin.getFileManager().getDungeonsConfig().save();
                plugin.getDungeonManager().loadDungeons();
                String prefix = plugin.getConfig().getString("messages.prefix", "");
                String msg = plugin.getConfig().getString("messages.dungeon_deleted", "&aᴢiɴᴅᴀɴ ᴋᴀʟɪᴄɪ ᴏʟᴀʀᴀᴋ ʙᴀꜱᴀʀɪʏʟᴀ ꜱiʟiɴᴅi!");
                player.sendMessage(FormatUtils.format(prefix + msg));
            }
            else if (action.equals("edit_back")) {
                player.openInventory(new AdminMenu(player).getInventory());
            }
        }
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (!activeSessions.containsKey(player.getUniqueId())) return;
        event.setCancelled(true);

        SessionData session = activeSessions.remove(player.getUniqueId());
        String input = PlainTextComponentSerializer.plainText().serialize(event.message());

        player.getScheduler().run(plugin, task -> {
            if (input.equalsIgnoreCase("iptal")) {
                player.sendMessage(FormatUtils.format("§ciꜱʟᴇᴍ iᴘᴛᴀʟ ᴇᴅiʟᴅi."));
                player.openInventory(new AdminMenu(player).getInventory());
                return;
            }

            if (session.action.equals("CREATE")) {
                String safeId = input.replace(" ", "_").toLowerCase();
                if (plugin.getDungeonManager().getDungeon(safeId) != null) {
                    player.sendMessage(FormatUtils.format("§cʙᴜ ɪᴅ iʟᴇ ᴢᴀᴛᴇɴ ʙiʀ ᴢiɴᴅᴀɴ ᴠᴀʀ!"));
                    player.openInventory(new AdminMenu(player).getInventory());
                    return;
                }
                saveDungeonField(safeId, "display_name", "&c" + safeId);
                saveDungeonField(safeId, "cooldown", 0);
                saveDungeonField(safeId, "requirement.type", "FREE");
                plugin.getDungeonManager().loadDungeons();
                player.sendMessage(FormatUtils.format("§aᴢiɴᴅᴀɴ ʙᴀꜱᴀʀɪʏʟᴀ ᴏʟᴜꜱᴛᴜʀᴜʟᴅᴜ!"));

                Dungeon d = plugin.getDungeonManager().getDungeon(safeId);
                if (d != null) player.openInventory(new EditMenu(player, d).getInventory());
            }
            else if (session.action.equals("EDIT_NAME")) {
                saveDungeonField(session.dungeonId, "display_name", input);
                plugin.getDungeonManager().loadDungeons();
                player.sendMessage(FormatUtils.format("§aiꜱiᴍ ʙᴀꜱᴀʀɪʏʟᴀ ɢüɴᴄᴇʟʟᴇɴᴅi."));

                Dungeon d = plugin.getDungeonManager().getDungeon(session.dungeonId);
                if (d != null) player.openInventory(new EditMenu(player, d).getInventory());
            }
            else if (session.action.equals("EDIT_COOLDOWN")) {
                try {
                    int cd = Integer.parseInt(input);
                    saveDungeonField(session.dungeonId, "cooldown", cd);
                    plugin.getDungeonManager().loadDungeons();
                    player.sendMessage(FormatUtils.format("§aʙᴇᴋʟᴇᴍᴇ ꜱüʀᴇꜱi ɢüɴᴄᴇʟʟᴇɴᴅi."));
                } catch (NumberFormatException e) {
                    player.sendMessage(FormatUtils.format("§cʟüᴛꜰᴇɴ ꜱᴀᴅᴇᴄᴇ ꜱᴀʏɪ ɢiʀiɴ! iꜱʟᴇᴍ iᴘᴛᴀʟ ᴇᴅiʟᴅi."));
                }
                Dungeon d = plugin.getDungeonManager().getDungeon(session.dungeonId);
                if (d != null) player.openInventory(new EditMenu(player, d).getInventory());
            }
        }, null);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == org.bukkit.Material.AIR || !item.hasItemMeta()) return;

        String dungeonId = item.getItemMeta().getPersistentDataContainer().get(setterKey, PersistentDataType.STRING);
        if (dungeonId != null) {
            event.setCancelled(true);
            Location loc = player.getLocation();
            saveDungeonField(dungeonId, "teleport.world", loc.getWorld().getName());
            saveDungeonField(dungeonId, "teleport.x", loc.getX());
            saveDungeonField(dungeonId, "teleport.y", loc.getY());
            saveDungeonField(dungeonId, "teleport.z", loc.getZ());
            saveDungeonField(dungeonId, "teleport.yaw", loc.getYaw());
            saveDungeonField(dungeonId, "teleport.pitch", loc.getPitch());

            plugin.getDungeonManager().loadDungeons();
            item.setAmount(item.getAmount() - 1);
            player.sendMessage(FormatUtils.format("§aᴢiɴᴅᴀɴ ʟᴏᴋᴀꜱʏᴏɴᴜ ᴋᴀʏᴅᴇᴅiʟᴅi!"));
        }
    }

    private void saveDungeonField(String id, String field, Object value) {
        FileConfiguration config = plugin.getFileManager().getDungeonsConfig().getConfig();
        config.set("dungeons." + id + "." + field, value);
        plugin.getFileManager().getDungeonsConfig().save();
    }
}