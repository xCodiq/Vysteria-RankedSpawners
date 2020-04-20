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

package dev.xcodiq.vysteriarankedspawners.rank.api.impl;

import com.bgsoftware.superiorskyblock.api.island.Island;
import dev.xcodiq.vysteriarankedspawners.rank.Rank;
import dev.xcodiq.vysteriarankedspawners.rank.RankStatistic;

public interface API {

    /**
     * Sets the rank of an island.
     *
     * @param island the island
     * @param rank   the rank
     */
    void setRank(Island island, Rank rank);

    /**
     * Gets the rank of an island.
     *
     * @param island the island
     * @return the rank
     */
    Rank getRank(Island island);

    int getRankInt(Island island);

    /**
     * Sets a specific statistic of an island.
     *
     * @param island    the island
     * @param statistic the statistic
     * @param value     the value
     */
    void setStatistic(Island island, RankStatistic statistic, int value);

    /**
     * Gets a specific statistic of an island.
     *
     * @param island    the island
     * @param statistic the statistic
     * @return the statistic
     */
    int getStatistic(Island island, RankStatistic statistic);

    /**
     * Reset the statitics of an island.
     *
     * @param island the island
     */
    void resetStatitics(Island island);

    /**
     * Reset a specifc island data
     *
     * @param island the island
     */
    void reset(Island island);

    /**
     * Sets all the default values.
     *
     * @param island the island
     */
    void setup(Island island);

    /**
     * Sets the block statistic of an island.
     *
     * @param island the island
     * @param blocks the blocks
     * @deprecated Method isn't support if occurred any issues. Please use {@link API#setStatistic(Island, RankStatistic, int)}
     */
    @Deprecated
    void setBlockStatistic(Island island, int blocks);

    /**
     * Sets the mob statistic of an island.
     *
     * @param island the island
     * @param mobs   the mobs
     * @deprecated Method isn't support if occurred any issues. Please use {@link API#setStatistic(Island, RankStatistic, int)}
     */
    @Deprecated
    void setMobStatistic(Island island, int mobs);

    /**
     * Sets the fish statistic of an island.
     *
     * @param island the island
     * @param fish   the fish
     * @deprecated Method isn't support if occurred any issues. Please use {@link API#setStatistic(Island, RankStatistic, int)}
     */
    @Deprecated
    void setFishStatistic(Island island, int fish);

    /**
     * Gets the block statistic of an island.
     *
     * @param island the island
     * @return the block statistic
     * @deprecated Method isn't support if occurred any issues. Please use {@link API#setStatistic(Island, RankStatistic, int)}
     */
    @Deprecated
    int getBlockStatistic(Island island);

    /**
     * Gets the mob statistic of an island.
     *
     * @param island the island
     * @return the mob statistic
     * @deprecated Method isn't support if occurred any issues. Please use {@link API#setStatistic(Island, RankStatistic, int)}
     */
    @Deprecated
    int getMobStatistic(Island island);

    /**
     * Gets the fish statistic of an island.
     *
     * @param island the island
     * @return the fish statistic
     * @deprecated Method isn't support if occurred any issues. Please use {@link API#setStatistic(Island, RankStatistic, int)}
     */
    @Deprecated
    int getFishStatistic(Island island);

    /**
     * Save all the items in the data configuration.
     */
    void saveItems();
}
