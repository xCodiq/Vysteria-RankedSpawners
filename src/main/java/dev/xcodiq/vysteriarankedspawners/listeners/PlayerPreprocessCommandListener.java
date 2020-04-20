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
import dev.xcodiq.vysterialibrary.library.utilities.ChatUtils;
import dev.xcodiq.vysteriarankedspawners.RankedSpawners;
import dev.xcodiq.vysteriarankedspawners.menus.IslandRankMenu;
import dev.xcodiq.vysteriarankedspawners.menus.SpawnerShopMenu;
import dev.xcodiq.vysteriarankedspawners.rank.api.impl.API;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerPreprocessCommandListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();
        API api = RankedSpawners.getAPI();
        FileConfiguration config = RankedSpawners.getInstance().getConfig();

        if (command.equalsIgnoreCase("/is rank") || command.equalsIgnoreCase("/island rankup")
                || command.equalsIgnoreCase("/is rankup") || command.equalsIgnoreCase("/island rank")) {
            Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

            if (island == null || api.getRank(island) == null) {
                player.sendMessage(ChatUtils.format(config.getString("messages.no-island")));
                return;
            }

            new IslandRankMenu(player, island);
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1.0F, 1.0F);
            event.setCancelled(true);
        } else if (command.equalsIgnoreCase("/is spawner") || command.equalsIgnoreCase("/is spawners")
                || command.equalsIgnoreCase("/is spawnershop") || command.equalsIgnoreCase("/island spawner")
                || command.equalsIgnoreCase("/island spawners") || command.equalsIgnoreCase("/island spawnershop")) {
            Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();

            if (island == null || api.getRank(island) == null) {
                player.sendMessage(ChatUtils.format(config.getString("messages.no-island")));
                return;
            }

            new SpawnerShopMenu(player);
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            event.setCancelled(true);
        }
    }
}
