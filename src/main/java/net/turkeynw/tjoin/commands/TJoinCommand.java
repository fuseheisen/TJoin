package net.turkeynw.tjoin.commands;

import net.turkeynw.tjoin.TJoin;
import net.turkeynw.tjoin.menus.AdminMenu;
import net.turkeynw.tjoin.menus.MainMenu;
import net.turkeynw.tjoin.models.Dungeon;
import net.turkeynw.tjoin.utils.FormatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TJoinCommand extends Command {

    private final TJoin plugin = TJoin.getInstance();

    public TJoinCommand() {
        super("tjoin", "TJoin ana komutu.", "/tjoin", new ArrayList<>());
    }

    private void sendConfigMessage(CommandSender sender, String path) {
        String prefix = plugin.getConfig().getString("messages.prefix", "");
        String message = plugin.getConfig().getString("messages." + path, "");
        sender.sendMessage(FormatUtils.format(prefix + message));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sendConfigMessage(sender, "not_player");
            return true;
        }

        if (args.length == 0) {
            MainMenu menu = new MainMenu(player);
            player.openInventory(menu.getInventory());
            return true;
        }

        if (player.hasPermission("tjoin.admin")) {
            if (args[0].equalsIgnoreCase("admin")) {
                AdminMenu adminMenu = new AdminMenu(player);
                player.openInventory(adminMenu.getInventory());
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                plugin.getFileManager().reloadAll();
                plugin.getDungeonManager().loadDungeons();
                sendConfigMessage(player, "reload_success");
                return true;
            }

            if (args[0].equalsIgnoreCase("getkey")) {
                if (args.length < 2) {
                    player.sendMessage(FormatUtils.format("§cᴋᴜʟʟᴀɴɪᴍ: /ᴛᴊᴏiɴ ɢᴇᴛᴋᴇʏ <ᴢiɴᴅᴀɴ_ɪᴅ>"));
                    return true;
                }

                String dungeonId = args[1];
                if (plugin.getDungeonManager().getDungeon(dungeonId) == null) {
                    player.sendMessage(FormatUtils.format("§cʙᴜ ɪᴅ'ʏᴇ ꜱᴀʜiᴘ ʙiʀ ᴢiɴᴅᴀɴ ʙᴜʟᴜɴᴀᴍᴀᴅɪ!"));
                    return true;
                }

                ItemStack keyItem = plugin.getKeyManager().createKey(dungeonId);
                player.getInventory().addItem(keyItem);
                player.sendMessage(FormatUtils.format("§aᴀɴᴀʜᴛᴀʀ ʙᴀꜱᴀʀɪʏʟᴀ ᴇɴᴠᴀɴᴛᴇʀiɴᴇ ᴇᴋʟᴇɴᴅi!"));
                return true;
            }
        } else {
            sendConfigMessage(player, "no_permission");
            return true;
        }

        player.sendMessage(FormatUtils.format("§cʜᴀᴛᴀʟɪ ᴋᴜʟʟᴀɴɪᴍ! §7/ᴛᴊᴏiɴ"));
        return true;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (sender.hasPermission("tjoin.admin")) {
            if (args.length == 1) {
                completions.add("reload");
                completions.add("admin");
                completions.add("getkey");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("getkey")) {
                for (Dungeon dungeon : plugin.getDungeonManager().getAllDungeons()) completions.add(dungeon.getId());
            }
        }
        return completions;
    }
}