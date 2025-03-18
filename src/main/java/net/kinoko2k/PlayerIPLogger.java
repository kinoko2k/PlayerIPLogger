package net.kinoko2k;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlayerIPLogger extends JavaPlugin implements Listener, CommandExecutor {
    private File configFile;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        loadConfig();
    }

    private void loadConfig() {
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String ip = event.getPlayer().getAddress().getAddress().getHostAddress();
        String mcid = event.getPlayer().getName();
        String uuid = event.getPlayer().getUniqueId().toString();

        String entry = ip + ", " + mcid + ", " + uuid;

        List<String> datalist = config.getStringList("datalist");

        if (!datalist.contains(entry)) {
            datalist.add(entry);
            config.set("datalist", datalist);
            saveConfig();
        }
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}