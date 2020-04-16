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
