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

package dev.xcodiq.vysteriarankedspawners.rank.api;

import com.bgsoftware.superiorskyblock.api.island.Island;
import dev.xcodiq.vysteriarankedspawners.RankedSpawners;
import dev.xcodiq.vysteriarankedspawners.rank.Rank;
import dev.xcodiq.vysteriarankedspawners.rank.RankStatistic;
import dev.xcodiq.vysteriarankedspawners.rank.api.events.RankChangeEvent;
import dev.xcodiq.vysteriarankedspawners.rank.api.events.RankStatisticChangeEvent;
import dev.xcodiq.vysteriarankedspawners.rank.api.impl.API;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public class RankAPI implements API {

    private final FileConfiguration data = RankedSpawners.getInstance().getDataFile();

    @Override
    public void setRank(Island island, Rank rank) {
        this.data.set(island.getOwner().getUniqueId().toString() + ".rank", rank.getName());

        Bukkit.getServer().getPluginManager().callEvent(new RankChangeEvent(island, rank));
    }

    @Override
    public Rank getRank(Island island) {
//        if (this.data == null) throw new NullPointerException("Data is null");
//        try {
//            this.data.getString(island.getOwner().getUniqueId().toString() + ".rank");
//        } catch (Exception e) {
//            System.out.println("[ERROR] data.getString is null");
//        }
//        try {
//            Rank.getByName(this.data.getString(island.getOwner().getUniqueId().toString() + ".rank"));
//        } catch (Exception e) {
//            System.out.println("[ERROR] getByName is null");
//        }
        return Rank.getByName(this.data.getString(island.getOwner().getUniqueId().toString() + ".rank", "null"));
    }

    public int getRankInt(Island island) {
        return Integer.parseInt(this.data.getString(island.getOwner().getUniqueId().toString() + ".rank", "0"));
    }

    @Override
    public void setStatistic(Island island, RankStatistic statistic, int value) {
        this.data.set(island.getOwner().getUniqueId().toString() + "." + statistic.getName(), value);

        Rank rank = Rank.getByName(this.data.getString(island.getOwner().getUniqueId().toString() + ".rank"));
        Bukkit.getServer().getPluginManager().callEvent(new RankStatisticChangeEvent(island, rank, statistic));
    }

    @Override
    public int getStatistic(Island island, RankStatistic statistic) {
        return this.data.getInt(island.getOwner().getUniqueId().toString() + "." + statistic.getName(), 0);
    }

    @Override
    public void resetStatitics(Island island) {
        Arrays.stream(RankStatistic.values()).forEach(statistic -> setStatistic(island, statistic, statistic.getDefaultValue()));
    }

    @Override
    public void reset(Island island) {
        this.data.set(island.getOwner().getUniqueId().toString(), null);
    }

    @Override
    public void setup(Island island) {
        this.setRank(island, Rank.getByName("0"));
        this.resetStatitics(island);
    }

    @Override
    public void setBlockStatistic(Island island, int blocks) {
        this.data.set(island.getOwner().getUniqueId().toString() + ".blocks", blocks);
    }

    @Override
    public void setMobStatistic(Island island, int mobs) {
        this.data.set(island.getOwner().getUniqueId().toString() + ".mobs", mobs);
    }

    @Override
    public void setFishStatistic(Island island, int fish) {
        this.data.set(island.getOwner().getUniqueId().toString() + ".fish", fish);
    }

    @Override
    public int getBlockStatistic(Island island) {
        return this.data.getInt(island.getOwner().getUniqueId().toString() + ".blocks");
    }

    @Override
    public int getMobStatistic(Island island) {
        return this.data.getInt(island.getOwner().getUniqueId().toString() + ".mobs");
    }

    @Override
    public int getFishStatistic(Island island) {
        return this.data.getInt(island.getOwner().getUniqueId().toString() + ".fish");
    }

    @Override
    public void saveItems() {
        RankedSpawners.getInstance().getData().saveConfig();
    }
}