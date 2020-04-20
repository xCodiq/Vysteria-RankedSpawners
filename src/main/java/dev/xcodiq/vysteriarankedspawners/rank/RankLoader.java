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

package dev.xcodiq.vysteriarankedspawners.rank;

import com.google.common.collect.Lists;
import dev.xcodiq.vysteriarankedspawners.RankedSpawners;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class RankLoader {

    private static final List<Rank> ranks = Lists.newArrayList();

    public RankLoader(RankedSpawners plugin) {
        FileConfiguration config = plugin.getRanksFile();

        ConfigurationSection section = config.getConfigurationSection("ranks");

        for (String key : section.getKeys(false)) {
            section = config.getConfigurationSection("ranks." + key);
            Rank rank;

            String name = section.getString("name");

            int blocks = section.getInt("blocks");
            int mobs = section.getInt("mobs");
            int fish = section.getInt("fish");

            List<String> commands = section.getStringList("commands");
            List<String> rewards = section.getStringList("rewards");

            rank = new Rank(key, name, blocks, mobs, fish, commands, rewards);
            ranks.add(rank);
        }
    }

    public static List<Rank> getRanks() {
        return ranks;
    }
}
