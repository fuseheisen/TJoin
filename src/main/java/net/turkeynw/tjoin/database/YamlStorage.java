package net.turkeynw.tjoin.database;

import net.turkeynw.tjoin.TJoin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class YamlStorage implements StorageProvider {

    private final TJoin plugin;
    private File file;
    private FileConfiguration config;

    public YamlStorage(TJoin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void connect() {
        file = new File(plugin.getDataFolder(), "cooldowns.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getComponentLogger().error("cooldowns.yml dosyasi olusturulamadi!", e);
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void disconnect() {
        save();
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getComponentLogger().error("cooldowns.yml dosyasi kaydedilemedi!", e);
        }
    }

    @Override
    public CompletableFuture<Long> getCooldown(UUID uuid, String dungeonId) {

        return CompletableFuture.supplyAsync(() -> {
            String path = uuid.toString() + "." + dungeonId;
            return config.getLong(path, 0L);
        });
    }

    @Override
    public CompletableFuture<Void> setCooldown(UUID uuid, String dungeonId, long expireTime) {
        return CompletableFuture.runAsync(() -> {
            String path = uuid.toString() + "." + dungeonId;
            config.set(path, expireTime);
            save();
        });
    }
}