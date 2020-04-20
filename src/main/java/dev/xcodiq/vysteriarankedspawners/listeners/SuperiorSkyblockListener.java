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

package dev.xcodiq.vysteriarankedspawners.listeners;

import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandTransferEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import dev.xcodiq.vysteriarankedspawners.RankedSpawners;
import dev.xcodiq.vysteriarankedspawners.rank.Rank;
import dev.xcodiq.vysteriarankedspawners.rank.RankStatistic;
import dev.xcodiq.vysteriarankedspawners.rank.api.impl.API;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SuperiorSkyblockListener implements Listener {

    private final API api = RankedSpawners.getAPI();

    @EventHandler(priority = EventPriority.HIGH)
    public void onIslandCreate(IslandCreateEvent event) {
        api.setup(event.getIsland());
        api.saveItems();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onIslandTransfer(IslandTransferEvent event) {
        Island island = event.getIsland();
        Island newIsland = event.getNewOwner().getIsland();

        Rank rank = api.getRank(island);
        int blockCount = api.getStatistic(island, RankStatistic.BLOCKS);
        int mobCount = api.getStatistic(island, RankStatistic.MOBS);
        int fishCount = api.getStatistic(island, RankStatistic.FISH);

        api.reset(event.getOldOwner().getIsland());
        api.setRank(newIsland, rank);
        api.setStatistic(newIsland, RankStatistic.BLOCKS, blockCount);
        api.setStatistic(newIsland, RankStatistic.MOBS, mobCount);
        api.setStatistic(newIsland, RankStatistic.FISH, fishCount);
        api.saveItems();
    }

    @EventHandler
    public void onIslandDisband(IslandDisbandEvent event) {
        api.reset(event.getIsland());
        api.saveItems();
    }
}
