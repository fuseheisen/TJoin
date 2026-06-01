package net.turkeynw.tjoin.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.turkeynw.tjoin.TJoin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class KeyManager {

    private final TJoin plugin;
    public final NamespacedKey keyTag;

    public KeyManager(TJoin plugin) {
        this.plugin = plugin;
        this.keyTag = new NamespacedKey(plugin, "dungeon_key_id");
    }

    public ItemStack createKey(String dungeonId) {
        FileConfiguration config = plugin.getFileManager().getKeysConfig().getConfig();
        String path = "keys." + dungeonId + ".";

        if (!config.contains("keys." + dungeonId)) {

            ItemStack defaultKey = new ItemStack(Material.TRIPWIRE_HOOK);
            ItemMeta meta = defaultKey.getItemMeta();
            meta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize("&e" + dungeonId + " Anahtarı"));
            meta.getPersistentDataContainer().set(keyTag, PersistentDataType.STRING, dungeonId);
            defaultKey.setItemMeta(meta);
            return defaultKey;
        }


        String materialName = config.getString(path + "material", "TRIPWIRE_HOOK").toUpperCase();
        Material material = Material.matchMaterial(materialName);
        if (material == null) material = Material.TRIPWIRE_HOOK;

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            // İsim
            String name = config.getString(path + "name", "&eZindan Anahtarı");
            meta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(name));

            // Lore
            List<String> loreList = config.getStringList(path + "lore");
            List<Component> componentLore = new ArrayList<>();
            for (String line : loreList) {
                componentLore.add(LegacyComponentSerializer.legacyAmpersand().deserialize(line));
            }
            meta.lore(componentLore);


            if (config.contains(path + "custom_model_data")) {
                meta.setCustomModelData(config.getInt(path + "custom_model_data"));
            }


            if (config.getBoolean(path + "glow", false)) {
                meta.addEnchant(Enchantment.UNBREAKING, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }


            meta.getPersistentDataContainer().set(keyTag, PersistentDataType.STRING, dungeonId);

            item.setItemMeta(meta);
        }

        return item;
    }
}