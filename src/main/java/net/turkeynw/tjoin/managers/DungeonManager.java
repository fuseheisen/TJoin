package net.turkeynw.tjoin.managers;

import net.turkeynw.tjoin.TJoin;
import net.turkeynw.tjoin.models.Dungeon;
import net.turkeynw.tjoin.models.RequirementType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DungeonManager {

    private final TJoin plugin;
    private final Map<String, Dungeon> dungeons;

    public DungeonManager(TJoin plugin) {
        this.plugin = plugin;
        this.dungeons = new HashMap<>();
        loadDungeons();
    }

    public void loadDungeons() {
        dungeons.clear();


        FileConfiguration config = plugin.getFileManager().getDungeonsConfig().getConfig();

        if (config == null) {
            plugin.getComponentLogger().error("dungeons.yml config dosyasi okunamadi!");
            return;
        }

        ConfigurationSection section = config.getConfigurationSection("dungeons");
        if (section == null) {
            plugin.getComponentLogger().warn("dungeons.yml icinde hic zindan bulunamadi!");
            return;
        }

        for (String id : section.getKeys(false)) {
            Dungeon dungeon = new Dungeon(id);
            String path = "dungeons." + id + ".";

            dungeon.setDisplayName(config.getString(path + "display_name", "&cIsimsiz Zindan"));
            dungeon.setCooldownSeconds(config.getInt(path + "cooldown", 0));

            String reqStr = config.getString(path + "requirement.type", "FREE").toUpperCase();
            try {
                RequirementType type = RequirementType.valueOf(reqStr);
                dungeon.setRequirementType(type);

                if (type == RequirementType.MONEY) {
                    dungeon.setMoneyCost(config.getDouble(path + "requirement.amount", 0.0));
                } else if (type == RequirementType.KEY) {
                    dungeon.setKeyId(config.getString(path + "requirement.key_id", ""));
                }
            } catch (IllegalArgumentException e) {

                plugin.getComponentLogger().error("Gecersiz gereksinim turu zindan: {}", id);
                dungeon.setRequirementType(RequirementType.FREE);
            }

            if (config.contains(path + "teleport.world")) {
                String worldName = config.getString(path + "teleport.world");


                if (worldName != null) {
                    World world = Bukkit.getWorld(worldName);

                    if (world == null) {

                        plugin.getComponentLogger().warn("Zindan ({}) dunya yuklu degil veya bulunamadi: {}", id, worldName);
                    } else {
                        double x = config.getDouble(path + "teleport.x");
                        double y = config.getDouble(path + "teleport.y");
                        double z = config.getDouble(path + "teleport.z");
                        float yaw = (float) config.getDouble(path + "teleport.yaw", 0.0);
                        float pitch = (float) config.getDouble(path + "teleport.pitch", 0.0);

                        dungeon.setLocation(new Location(world, x, y, z, yaw, pitch));
                    }
                }
            }

            dungeons.put(id, dungeon);
        }


        plugin.getComponentLogger().info("Toplam {} zindan basariyla yuklendi.", dungeons.size());
    }

    public Dungeon getDungeon(String id) {
        return dungeons.get(id);
    }

    public Collection<Dungeon> getAllDungeons() {
        return dungeons.values();
    }
}