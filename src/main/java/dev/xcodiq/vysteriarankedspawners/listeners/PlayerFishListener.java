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

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import dev.xcodiq.vysteriarankedspawners.RankedSpawners;
import dev.xcodiq.vysteriarankedspawners.rank.Rank;
import dev.xcodiq.vysteriarankedspawners.rank.RankStatistic;
import dev.xcodiq.vysteriarankedspawners.rank.api.impl.API;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class PlayerFishListener implements Listener {

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();

        FileConfiguration config = RankedSpawners.getInstance().getRanksFile();
        API api = RankedSpawners.getAPI();

        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();
        if (event.isCancelled() || island == null) return;

        if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH) || event.getCaught() == null) return;

        int rank = Rank.getNextAsInt(island);
        int currentFishCount = api.getStatistic(island, RankStatistic.FISH);

        if (config.getInt("ranks." + rank + ".fish") > currentFishCount) {
            api.setStatistic(island, RankStatistic.FISH, currentFishCount + 1);
            api.saveItems();
        }
    }
}
