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

import com.bgsoftware.superiorskyblock.api.island.Island;
import dev.xcodiq.vysteriarankedspawners.RankedSpawners;
import dev.xcodiq.vysteriarankedspawners.rank.api.impl.API;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Rank {

    private static API api = RankedSpawners.getAPI();

    private final String id, name;
    private final int blocks, mobs, fish;
    private final List<String> commands, rewards;

    /**
     * Instantiates a new Rank.
     *
     * @param id       the id
     * @param name     the name
     * @param blocks   the blocks
     * @param mobs     the mobs
     * @param fish     the fish
     * @param commands the commands
     * @param rewards  the rewards
     */
    public Rank(String id, String name, int blocks, int mobs, int fish, List<String> commands, List<String> rewards) {
        this.id = id;
        this.name = name;
        this.blocks = blocks;
        this.mobs = mobs;
        this.fish = fish;
        this.commands = commands;
        this.rewards = rewards;
    }

    /**
     * Gets a specific rank by its name.
     *
     * @param name the name
     * @return the rank, null if the rank doesn't exists
     */
    public static Rank getByName(String name) {
        for (Rank rank : RankLoader.getRanks()) {
            if (rank.getName().equalsIgnoreCase(name)) return rank;
        }
        return null;
    }

    //TODO: getById method!!!!!!

    /**
     * Gets a the next rank in order
     *
     * @param island the island
     * @return the rank, null if there is no next rank
     */
    public static Rank getNext(Island island) {
        return RankLoader.getRanks().stream().filter(rank -> Integer.parseInt(rank.getId()) > Integer.parseInt(api.getRank(island).getName())).findFirst().orElse(null);
//        for (Rank rank : RankLoader.getRanks()) {
//            int keyRank = Integer.parseInt(rank.getId());
//            if (keyRank > Integer.parseInt(api.getRank(island).getName())) return getByName(String.valueOf(keyRank));
//        }
//        return null;
    }

    public static int getNextAsInt(Island island) {
        if (island == null) throw new IllegalStateException("Island cannot be null");

        for (String key : RankedSpawners.getInstance().getRanksFile().getConfigurationSection("ranks").getKeys(false)) {
            int keyRank = Integer.parseInt(key);
            if (keyRank > api.getRankInt(island)) return keyRank;
        }
        return -1;
    }
}
