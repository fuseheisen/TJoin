package net.turkeynw.tjoin;

import net.turkeynw.tjoin.commands.TJoinCommand;
import net.turkeynw.tjoin.database.DatabaseManager;
import net.turkeynw.tjoin.listeners.AdminListener;
import net.turkeynw.tjoin.listeners.MenuListener;
import net.turkeynw.tjoin.managers.DungeonManager;
import net.turkeynw.tjoin.managers.KeyManager;
import net.turkeynw.tjoin.managers.VaultManager;
import net.turkeynw.tjoin.utils.FileManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TJoin extends JavaPlugin {

    private static TJoin instance;
    private FileManager fileManager;
    private DatabaseManager databaseManager;
    private DungeonManager dungeonManager;
    private VaultManager vaultManager;
    private KeyManager keyManager;

    @Override
    public void onEnable() {
        instance = this;

        getComponentLogger().info("TJoin eklentisi basariyla aktif ediliyor...");

        this.fileManager = new FileManager(this);
        this.databaseManager = new DatabaseManager(this);
        this.dungeonManager = new DungeonManager(this);
        this.keyManager = new KeyManager(this);

        this.vaultManager = new VaultManager(this);
        if (!this.vaultManager.setupEconomy()) {
            getComponentLogger().error("Vault bulunamadi! Para ile giris yapilan zindanlar calismayacak.");
        }


        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new AdminListener(), this);


        getServer().getCommandMap().register("tjoin", new TJoinCommand());

        getComponentLogger().info("TJoin butun modulleriyle (Folia Uyumlu) basariyla yuklendi.");
    }

    @Override
    public void onDisable() {
        if (this.databaseManager != null) {
            this.databaseManager.close();
        }
    }

    public static TJoin getInstance() { return instance; }
    public FileManager getFileManager() { return fileManager; }
    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public DungeonManager getDungeonManager() { return dungeonManager; }
    public VaultManager getVaultManager() { return vaultManager; }
    public KeyManager getKeyManager() { return keyManager; }
}