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

package dev.xcodiq.vysteriarankedspawners.commands;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import dev.xcodiq.vysterialibrary.library.commands.VysteriaCommand;
import dev.xcodiq.vysterialibrary.library.utilities.ChatUtils;
import dev.xcodiq.vysteriarankedspawners.RankedSpawners;
import dev.xcodiq.vysteriarankedspawners.menus.IslandRankMenu;
import dev.xcodiq.vysteriarankedspawners.rank.api.impl.API;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class RankCommand extends VysteriaCommand {

    @Override
    public String getName() {
        return "rank";
    }

    @Override
    public String getDescription() {
        return "Open your island rank menu";
    }

    @Override
    public void onConstruction() {
        this.addAliases("rankup", "islandrank", "islandrankup");
    }

    @Override
    public void execute(CommandSender sender, String[] args, String label) {
        if (!(sender instanceof Player)) return;

        FileConfiguration config = RankedSpawners.getInstance().getConfig();

        Player player = (Player) sender;
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();
        API api = RankedSpawners.getAPI();

        if (island == null || api.getRank(island) == null) {
            player.sendMessage(ChatUtils.format(config.getString("messages.no-island")));
            return;
        }

        new IslandRankMenu(player, island);
        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1.0F, 1.0F);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
