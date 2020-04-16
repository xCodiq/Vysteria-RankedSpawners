package dev.xcodiq.vysteriarankedspawners;

import dev.xcodiq.vysterialibrary.library.launcher.VysteriaPlugin;

public class RankedSpawners extends VysteriaPlugin {

    private static RankedSpawners instance = null;

    public RankedSpawners() {
        if (instance != null) throw new IllegalStateException("Only one instance can run");
        instance = this;
    }

    public static RankedSpawners getInstance() {
        if (instance == null) throw new IllegalStateException("Instance cannot be null");
        return instance;
    }

    @Override
    public String getPluginName() {
        return "Vysteria-RankedSpawners";
    }

    @Override
    public String getPackageName() {
        return "dev.xcodiq.vysteriarankedspawners";
    }

    @Override
    public void onStartup() {
        if (!this.getServer().getPluginManager().isPluginEnabled("Vysteria-Library")) {
            this.getLogger().info("*** VYSTERIA-LIBRARY NOT INSTALLED! DISABLING PLUGIN ***");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        info("Enabling Vysteria-RankedSpawners by xCodiq");
        this.saveDefaultConfig();
    }

    @Override
    public void onShutdown() {

    }
}
