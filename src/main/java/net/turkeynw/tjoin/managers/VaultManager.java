package net.turkeynw.tjoin.managers;

import net.milkbowl.vault.economy.Economy;
import net.turkeynw.tjoin.TJoin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultManager {

    private Economy econ = null;
    private final TJoin plugin;

    public VaultManager(TJoin plugin) {
        this.plugin = plugin;
    }

    public boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }
}