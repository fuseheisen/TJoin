package net.turkeynw.tjoin.utils;

import net.turkeynw.tjoin.TJoin;

public class FileManager {

    private CustomConfig dungeonsConfig;
    private CustomConfig keysConfig;
    private CustomConfig menusConfig;

    public FileManager(TJoin plugin) {

        plugin.saveDefaultConfig();


        this.dungeonsConfig = new CustomConfig(plugin, "dungeons.yml");
        this.keysConfig = new CustomConfig(plugin, "keys.yml");
        this.menusConfig = new CustomConfig(plugin, "menus.yml");
    }

    public CustomConfig getDungeonsConfig() { return dungeonsConfig; }
    public CustomConfig getKeysConfig() { return keysConfig; }
    public CustomConfig getMenusConfig() { return menusConfig; }

    public void reloadAll() {
        TJoin.getInstance().reloadConfig();
        dungeonsConfig.reloadConfig();
        keysConfig.reloadConfig();
        menusConfig.reloadConfig();
    }
}