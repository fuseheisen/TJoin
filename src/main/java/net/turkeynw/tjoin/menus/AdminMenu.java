package net.turkeynw.tjoin.menus;

import net.kyori.adventure.text.Component;
import net.turkeynw.tjoin.TJoin;
import net.turkeynw.tjoin.models.Dungeon;
import net.turkeynw.tjoin.utils.FormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdminMenu implements InventoryHolder {

    private final Inventory inventory;
    private final TJoin plugin = TJoin.getInstance();

    public AdminMenu(Player player) {
        FileConfiguration config = plugin.getFileManager().getMenusConfig().getConfig();
        this.inventory = Bukkit.createInventory(this, config.getInt("menus.admin_menu.size", 54), FormatUtils.format(config.getString("menus.admin_menu.title", "&4⚙ ᴢiɴᴅᴀɴ ʏöɴᴇᴛiᴍi")));
        setupFiller(config);
        setupCreateButton(config);
        setupMenu(config);
    }

    private void setupFiller(FileConfiguration config) {
        if (config.getBoolean("menus.admin_menu.filler.enabled", false)) {
            Material mat = Material.matchMaterial(config.getString("menus.admin_menu.filler.material", "RED_STAINED_GLASS_PANE"));
            if (mat == null) mat = Material.RED_STAINED_GLASS_PANE;
            ItemStack filler = new ItemStack(mat);
            ItemMeta meta = filler.getItemMeta();
            if (meta != null) {
                meta.displayName(FormatUtils.format(config.getString("menus.admin_menu.filler.name", " ")));
                filler.setItemMeta(meta);
            }
            for (int i = 0; i < inventory.getSize(); i++) inventory.setItem(i, filler);
        }
    }

    private void setupCreateButton(FileConfiguration config) {
        String path = "menus.admin_menu.create_button.";
        Material mat = Material.matchMaterial(config.getString(path + "material", "NETHER_STAR"));
        if (mat == null) mat = Material.NETHER_STAR;

        ItemStack createBtn = new ItemStack(mat);
        ItemMeta meta = createBtn.getItemMeta();
        if (meta != null) {
            meta.displayName(FormatUtils.format(config.getString(path + "name", "&a+ ʏᴇɴi ᴢiɴᴅᴀɴ ᴏʟᴜꜱᴛᴜʀ")));
            List<Component> finalLore = new ArrayList<>();
            for (String line : config.getStringList(path + "lore")) finalLore.add(FormatUtils.format(line));
            meta.lore(finalLore);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "admin_action"), PersistentDataType.STRING, "create_new");
            createBtn.setItemMeta(meta);
        }
        inventory.setItem(config.getInt(path + "slot", 49), createBtn);
    }

    private void setupMenu(FileConfiguration config) {
        int slot = 0;
        NamespacedKey dungeonKey = new NamespacedKey(plugin, "dungeon_id");
        String path = "menus.admin_menu.dungeon_item.";

        for (Dungeon dungeon : plugin.getDungeonManager().getAllDungeons()) {
            if (slot == config.getInt("menus.admin_menu.create_button.slot", 49)) slot++;
            if (slot >= inventory.getSize()) break;

            ItemStack item = new ItemStack(Material.SPAWNER);
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                meta.displayName(FormatUtils.format(config.getString(path + "name_format", "%dungeon_name%").replace("%dungeon_name%", dungeon.getDisplayName())));
                String reqText = dungeon.getRequirementType().name();
                String locText = (dungeon.getLocation() != null) ? "&aᴀʏᴀʀʟᴀɴᴍɪꜱ" : "&cᴀʏᴀʀʟᴀɴᴍᴀᴍɪꜱ";

                List<Component> finalLore = new ArrayList<>();
                for (String line : config.getStringList(path + "lore_format")) {
                    finalLore.add(FormatUtils.format(line.replace("%dungeon_id%", dungeon.getId()).replace("%req_text%", reqText).replace("%loc_text%", locText)));
                }
                meta.lore(finalLore);
                meta.getPersistentDataContainer().set(dungeonKey, PersistentDataType.STRING, dungeon.getId());
                item.setItemMeta(meta);
            }
            inventory.setItem(slot, item);
            slot++;
        }
    }

    @Override
    public @NotNull Inventory getInventory() { return inventory; }
}