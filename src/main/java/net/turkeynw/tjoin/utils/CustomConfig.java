package net.turkeynw.tjoin.utils;

import net.turkeynw.tjoin.TJoin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomConfig {

    private final TJoin plugin;
    private final String fileName;
    private File file;
    private FileConfiguration config;

    public CustomConfig(TJoin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        setup();
    }

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            try {
                plugin.saveResource(fileName, false);
            } catch (IllegalArgumentException e) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    plugin.getComponentLogger().error("Dosya olusturulamadi: " + fileName, ex);
                }
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }


    public void reloadConfig() {
        if (file == null) {
            file = new File(plugin.getDataFolder(), fileName);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        if (config == null || file == null) return;
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getComponentLogger().error(fileName + " dosyasi kaydedilemedi!", e);
        }
    }
}