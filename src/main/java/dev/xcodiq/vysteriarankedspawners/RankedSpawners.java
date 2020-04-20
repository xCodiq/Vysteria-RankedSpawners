/*
 *   ~
 *   ~ Copyright 2020 NeverEndingPvP. All rights reserved.
 *   ~
 *   ~ Licensed under the NeverEndingPvP License, Version 1.0 (the "License");
 *   ~ you may not use this file except in compliance with the License.
 *   ~
 *   ~ You are not allowed to edit the source.
 *   ~ You are not allowed to edit this text.
 *   ~ You are not allowed to sell this source.
 *   ~ You are not allowed to distribute this source in any way.
 *   ~ You are not allowed to claim this as yours.
 *   ~ You are not allowed to distribute.
 *   ~ You are not allowed to make own terms.
 *   ~ You are not allowed to place own warranty.
 *   ~ You are not allowed to make any sublicense.
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software
 *   ~ distributed under the License is distributed on an "AS IS" BASIS.
 *   ~
 *   ~ Author: xCodiq (Discord: Codiq#3662)
 *   ~
 */

package dev.xcodiq.vysteriarankedspawners;

import dev.xcodiq.vysterialibrary.library.commons.configuration.ConfigurationManager;
import dev.xcodiq.vysterialibrary.library.commons.menu.MenuAPI;
import dev.xcodiq.vysterialibrary.library.launcher.VysteriaPlugin;
import dev.xcodiq.vysteriarankedspawners.commands.RankCommand;
import dev.xcodiq.vysteriarankedspawners.commands.SpawnersCommand;
import dev.xcodiq.vysteriarankedspawners.hooks.Placeholders;
import dev.xcodiq.vysteriarankedspawners.listeners.*;
import dev.xcodiq.vysteriarankedspawners.rank.RankLoader;
import dev.xcodiq.vysteriarankedspawners.rank.api.RankAPI;
import dev.xcodiq.vysteriarankedspawners.rank.api.impl.API;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.stream.Stream;

public class RankedSpawners extends VysteriaPlugin {

    private static RankedSpawners instance = null;
    private static API api;

    private final ConfigurationManager data = new ConfigurationManager(this, "data/data.yml");
    private final ConfigurationManager ranks = new ConfigurationManager(this, "ranks.yml");
    private final ConfigurationManager islandRankMenu = new ConfigurationManager(this, "menus/island-rank-menu.yml");
    private final ConfigurationManager rankStatsMenu = new ConfigurationManager(this, "menus/rank-stats-menu.yml");
    private final ConfigurationManager rankTiersMenu = new ConfigurationManager(this, "menus/rank-tiers-menu.yml");
    private final ConfigurationManager spawnerPurchaseMenu = new ConfigurationManager(this, "menus/spawner-purchase-menu.yml");
    private final ConfigurationManager spawnerPurchaseAmountMenu = new ConfigurationManager(this, "menus/spawner-purchase-amount-menu.yml");
    private final ConfigurationManager spawnerShopMenu = new ConfigurationManager(this, "menus/spawner-shop-menu.yml");

    private Economy economy;

    public RankedSpawners() {
        if (instance != null) throw new IllegalStateException("Only one instance can run");
        instance = this;
    }

    public static RankedSpawners getInstance() {
        if (instance == null) throw new IllegalStateException("Instance cannot be null");
        return instance;
    }

    public static API getAPI() {
        if (api == null) api = new RankAPI();
        return api;
    }

    @Override
    public String getPluginName() {
        return "Vysteria-RankedSpawners";
    }

    @Override
    public void onStartup() {
        // Check if the library is not installed.
        if (!this.getServer().getPluginManager().isPluginEnabled("Vysteria-Library")) {
            this.getLogger().info("*** VYSTERIA-LIBRARY NOT INSTALLED! DISABLING PLUGIN ***");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Load the vault library
        boolean useVault = this.setupEconomy();

        this.getLogger().info("Enabling Vysteria-RankedSpawners by xCodiq");

        // Load all the configuration files
        this.saveDefaultConfig();
        this.data.saveDefaultConfig();
        this.ranks.saveDefaultConfig();
        this.islandRankMenu.saveDefaultConfig();
        this.rankStatsMenu.saveDefaultConfig();
        this.rankTiersMenu.saveDefaultConfig();
        this.spawnerPurchaseMenu.saveDefaultConfig();
        this.spawnerPurchaseAmountMenu.saveDefaultConfig();
        this.spawnerShopMenu.saveDefaultConfig();

        // Register the ranks from the ranks configuration file;
        new RankLoader(this);

        // Setup the API
        api = new RankAPI();
        api.saveItems();

        // Register the commands
        new RankCommand();
        new SpawnersCommand();

        // Register the listeners
        Stream.of(
                new SuperiorSkyblockListener(),
                new BlockBreakListener(),
                new AsyncPlayerChatListener(),
                new EntityDeathListener(),
                new PlayerFishListener(),
                new PlayerPreprocessCommandListener()
        ).forEach(this::registerListener);

        HandlerList.unregisterAll(MenuAPI.getInstance());

        // Register the placeholderapi hook
        if (this.usePAPI()) new Placeholders();
    }

    @Override
    public void onShutdown() {
        try {
            this.data.reloadConfig();
            this.ranks.reloadConfig();
            this.data.saveConfig();
            this.ranks.saveConfig();
        } catch (Exception ignored) {
        }
    }

    public FileConfiguration getRanksFile() {
        return this.ranks.getConfig();
    }

    public FileConfiguration getDataFile() {
        return this.data.getConfig();
    }

    public FileConfiguration getIslandRankMenuFile() {
        return this.islandRankMenu.getConfig();
    }

    public FileConfiguration getRankStatsMenuFile() {
        return this.rankStatsMenu.getConfig();
    }

    public FileConfiguration getRankTiersMenuFIle() {
        return this.rankTiersMenu.getConfig();
    }

    public FileConfiguration getSpawnerPurchaseMenuFile() {
        return this.spawnerPurchaseMenu.getConfig();
    }

    public FileConfiguration getSpawnerPurchaseAmountMenuFile() {
        return this.spawnerPurchaseAmountMenu.getConfig();
    }

    public FileConfiguration getSpawnerShopMenuFile() {
        return this.spawnerShopMenu.getConfig();
    }


    public ConfigurationManager getData() {
        return data;
    }

    public ConfigurationManager getRanks() {
        return ranks;
    }

    public ConfigurationManager getIslandRankMenu() {
        return islandRankMenu;
    }

    public ConfigurationManager getRankStatsMenu() {
        return rankStatsMenu;
    }

    public ConfigurationManager getRankTiersMenu() {
        return rankTiersMenu;
    }

    public ConfigurationManager getSpawnerPurchaseMenu() {
        return spawnerPurchaseMenu;
    }

    public ConfigurationManager getSpawnerPurchaseAmountMenu() {
        return spawnerPurchaseAmountMenu;
    }

    public ConfigurationManager getSpawnerShopMenu() {
        return spawnerShopMenu;
    }

    public boolean usePAPI() {
        return this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    private boolean setupEconomy() {
        if (!Bukkit.getPluginManager().isPluginEnabled("Vault")) return false;

        RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;

        this.economy = rsp.getProvider();

        return this.economy != null;
    }

    public Economy getEconomy() {
        return this.economy;
    }
}
