package net.turkeynw.tjoin.menus;

import net.kyori.adventure.text.Component;
import net.turkeynw.tjoin.TJoin;
import net.turkeynw.tjoin.models.Dungeon;
import net.turkeynw.tjoin.models.RequirementType;
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

public class MainMenu implements InventoryHolder {

    private final Inventory inventory;
    private final TJoin plugin = TJoin.getInstance();

    public MainMenu(Player player) {
        FileConfiguration config = plugin.getFileManager().getMenusConfig().getConfig();
        String titleStr = config.getString("menus.main_menu.title", "&8ᴢiɴᴅᴀɴ ꜱᴇᴄiᴍi");
        int size = config.getInt("menus.main_menu.size", 27);
        this.inventory = Bukkit.createInventory(this, size, FormatUtils.format(titleStr));

        setupFiller(config);
        setupMenu(config);
    }

    private void setupFiller(FileConfiguration config) {
        if (config.getBoolean("menus.main_menu.filler.enabled", false)) {
            Material mat = Material.matchMaterial(config.getString("menus.main_menu.filler.material", "BLACK_STAINED_GLASS_PANE"));
            if (mat == null) mat = Material.BLACK_STAINED_GLASS_PANE;

            ItemStack filler = new ItemStack(mat);
            ItemMeta meta = filler.getItemMeta();
            if (meta != null) {
                meta.displayName(FormatUtils.format(config.getString("menus.main_menu.filler.name", " ")));
                filler.setItemMeta(meta);
            }
            for (int i = 0; i < inventory.getSize(); i++) inventory.setItem(i, filler);
        }
    }

    private void setupMenu(FileConfiguration config) {
        int slot = 0;
        NamespacedKey dungeonKey = new NamespacedKey(plugin, "dungeon_id");
        Material defMaterial = Material.matchMaterial(config.getString("menus.main_menu.dungeon_item.default_material", "SPAWNER"));
        if (defMaterial == null) defMaterial = Material.SPAWNER;

        for (Dungeon dungeon : plugin.getDungeonManager().getAllDungeons()) {
            if (slot >= inventory.getSize()) break;

            ItemStack item = new ItemStack(defMaterial);
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                String nameFormat = config.getString("menus.main_menu.dungeon_item.name_format", "%dungeon_name%")
                        .replace("%dungeon_name%", dungeon.getDisplayName());
                meta.displayName(FormatUtils.format(nameFormat));

                String reqText = "";
                if (dungeon.getRequirementType() == RequirementType.FREE) {
                    reqText = config.getString("menus.main_menu.placeholders.req_free", "&aüᴄʀᴇᴛꜱiᴢ");
                } else if (dungeon.getRequirementType() == RequirementType.MONEY) {
                    reqText = config.getString("menus.main_menu.placeholders.req_money", "&e%cost% ᴛʟ").replace("%cost%", String.valueOf(dungeon.getMoneyCost()));
                } else if (dungeon.getRequirementType() == RequirementType.KEY) {
                    reqText = config.getString("menus.main_menu.placeholders.req_key", "&6ᴀɴᴀʜᴛᴀʀ ɢᴇʀᴇᴋiʏᴏʀ");
                }

                String cooldownText = dungeon.getCooldownSeconds() > 0
                        ? config.getString("menus.main_menu.placeholders.cooldown_active", "&fʙᴇᴋʟᴇᴍᴇ: &c%time% ꜱᴀɴiʏᴇ").replace("%time%", String.valueOf(dungeon.getCooldownSeconds()))
                        : "";

                List<String> loreFormat = config.getStringList("menus.main_menu.dungeon_item.lore_format");
                List<Component> finalLore = new ArrayList<>();
                for (String line : loreFormat) {
                    if (line.contains("%cooldown_text%") && cooldownText.isEmpty()) continue;
                    finalLore.add(FormatUtils.format(line.replace("%req_text%", reqText).replace("%cooldown_text%", cooldownText)));
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