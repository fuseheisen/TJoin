package net.turkeynw.tjoin.database;

import net.turkeynw.tjoin.TJoin;

public class DatabaseManager {

    private final StorageProvider storage;

    public DatabaseManager(TJoin plugin) {


        String type = plugin.getConfig().getString("database.type", "YAML").toUpperCase();

        switch (type) {
            /* made by
            fuseheisen
            fuseheisen
                fuseheisen
                fuseheisen
            discord: "fuseteas."
                turkeynw.com
                discord.gg/turkeynw
            */
            default:
                plugin.getComponentLogger().info("Veritabani Turu: YAML (Yerel Dosya) secildi.");
                storage = new YamlStorage(plugin);
                break;
        }


        storage.connect();
    }

    public StorageProvider getStorage() {
        return storage;
    }

    public void close() {
        if (storage != null) {
            storage.disconnect();
        }
    }
}