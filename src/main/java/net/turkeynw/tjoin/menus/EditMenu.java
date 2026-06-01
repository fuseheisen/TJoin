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

public class EditMenu implements InventoryHolder {

    private final Inventory inventory;
    private final TJoin plugin = TJoin.getInstance();

    public EditMenu(Player player, Dungeon dungeon) {
        FileConfiguration config = plugin.getFileManager().getMenusConfig().getConfig();
        String titleStr = config.getString("menus.edit_menu.title", "&4⚙ ᴅüᴢᴇɴʟᴇ: %dungeon_name%").replace("%dungeon_name%", dungeon.getDisplayName());
        this.inventory = Bukkit.createInventory(this, config.getInt("menus.edit_menu.size", 36), FormatUtils.format(titleStr));

        setupFiller(config);
        setupButtons(config, dungeon);
    }

    private void setupFiller(FileConfiguration config) {
        if (config.getBoolean("menus.edit_menu.filler.enabled", false)) {
            Material mat = Material.matchMaterial(config.getString("menus.edit_menu.filler.material", "GRAY_STAINED_GLASS_PANE"));
            if (mat == null) mat = Material.GRAY_STAINED_GLASS_PANE;
            ItemStack filler = new ItemStack(mat);
            ItemMeta meta = filler.getItemMeta();
            if (meta != null) {
                meta.displayName(FormatUtils.format(config.getString("menus.edit_menu.filler.name", " ")));
                filler.setItemMeta(meta);
            }
            for (int i = 0; i < inventory.getSize(); i++) inventory.setItem(i, filler);
        }
    }

    private void setupButtons(FileConfiguration config, Dungeon dungeon) {
        String basePath = "menus.edit_menu.buttons.";
        NamespacedKey actionKey = new NamespacedKey(plugin, "edit_action");
        NamespacedKey dungeonKey = new NamespacedKey(plugin, "dungeon_id");

        setButton(config, basePath + "edit_name", "edit_name", actionKey, dungeonKey, dungeon);
        setButton(config, basePath + "edit_req", "edit_req", actionKey, dungeonKey, dungeon);
        setButton(config, basePath + "edit_loc", "edit_loc", actionKey, dungeonKey, dungeon);
        setButton(config, basePath + "edit_cooldown", "edit_cooldown", actionKey, dungeonKey, dungeon);
        setButton(config, basePath + "edit_delete", "edit_delete", actionKey, dungeonKey, dungeon);
        setButton(config, basePath + "edit_back", "edit_back", actionKey, dungeonKey, dungeon);
    }

    private void setButton(FileConfiguration config, String path, String action, NamespacedKey actionKey, NamespacedKey dungeonKey, Dungeon dungeon) {
        Material mat = Material.matchMaterial(config.getString(path + ".material", "STONE"));
        if (mat == null) mat = Material.STONE;

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(FormatUtils.format(config.getString(path + ".name", "&cButon")));
            String locText = (dungeon.getLocation() != null) ? "&aᴀʏᴀʀʟᴀɴᴍɪꜱ" : "&cᴀʏᴀʀʟᴀɴᴍᴀᴍɪꜱ";
            String reqText = dungeon.getRequirementType().name();

            List<Component> finalLore = new ArrayList<>();
            for (String line : config.getStringList(path + ".lore")) {
                finalLore.add(FormatUtils.format(line.replace("%dungeon_name%", dungeon.getDisplayName()).replace("%req_text%", reqText).replace("%loc_text%", locText).replace("%cooldown%", String.valueOf(dungeon.getCooldownSeconds()))));
            }
            meta.lore(finalLore);

            meta.getPersistentDataContainer().set(actionKey, PersistentDataType.STRING, action);
            meta.getPersistentDataContainer().set(dungeonKey, PersistentDataType.STRING, dungeon.getId());
            item.setItemMeta(meta);
        }
        inventory.setItem(config.getInt(path + ".slot"), item);
    }

    @Override
    public @NotNull Inventory getInventory() { return inventory; }
}