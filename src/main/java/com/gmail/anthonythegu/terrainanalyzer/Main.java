package com.gmail.anthonythegu.terrainanalyzer;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register each of the following commands (set an instance of each command's
        // class as executor)
        getCommand("terrainanalyzerconfigreload").setExecutor(new CommandTerrainAnalyzerConfigReload());

        PluginCommand analyzeterrain = getCommand("analyzeterrain");
        CommandAnalyzeTerrain executor = new CommandAnalyzeTerrain();
        analyzeterrain.setExecutor(executor);

        // Register the command within the scanner
        GlobalTerrainScanner globalTerrainScanner = new GlobalTerrainScanner(this.getServer().getWorlds().get(0));
        globalTerrainScanner.registerCommand(executor);

        // Register the scanner within the listener
        ChunkLoadListener listener = new ChunkLoadListener();
        listener.registerScanner(globalTerrainScanner);

        // Register the listener
        getServer().getPluginManager().registerEvents(listener, this);

        // Create config file from jar config file
        saveDefaultConfig();
        // This thread is really useful
        // https://www.spigotmc.org/threads/whats-the-difference-between-savedefaultconfig-copydefaults.301865/

        // Commodore https://github.com/lucko/commodore
        // check if brigadier is supported
        if (CommodoreProvider.isSupported()) {

            // get a commodore instance
            Commodore commodore = CommodoreProvider.getCommodore(this);

            // register your completions.
            CommodoreRegister.registerCompletions(commodore, analyzeterrain);
        }
    }
}
