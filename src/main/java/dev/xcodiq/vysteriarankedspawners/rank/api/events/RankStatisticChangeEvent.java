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

package dev.xcodiq.vysteriarankedspawners.rank.api.events;

import com.bgsoftware.superiorskyblock.api.island.Island;
import dev.xcodiq.vysteriarankedspawners.rank.Rank;
import dev.xcodiq.vysteriarankedspawners.rank.RankStatistic;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RankStatisticChangeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Island island;
    private final Rank rank;
    private final RankStatistic statistic;
    private boolean cancel;

    public RankStatisticChangeEvent(Island island, Rank rank, RankStatistic statistic) {
        this.island = island;
        this.rank = rank;
        this.statistic = statistic;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Island getIsland() {
        return island;
    }

    public Rank getRank() {
        return rank;
    }

    public RankStatistic getStatistic() {
        return statistic;
    }
}
