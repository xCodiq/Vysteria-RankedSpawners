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

package dev.xcodiq.vysteriarankedspawners.menus;

import com.bgsoftware.superiorskyblock.api.island.Island;
import dev.xcodiq.vysterialibrary.library.commons.menu.InventoryClickType;
import dev.xcodiq.vysterialibrary.library.commons.menu.Menu;
import dev.xcodiq.vysterialibrary.library.commons.menu.MenuAPI;
import dev.xcodiq.vysterialibrary.library.commons.menu.MenuItem;
import dev.xcodiq.vysterialibrary.library.utilities.ChatUtils;
import dev.xcodiq.vysterialibrary.library.utilities.item.ItemBuilder;
import dev.xcodiq.vysteriarankedspawners.RankedSpawners;
import dev.xcodiq.vysteriarankedspawners.rank.RankStatistic;
import dev.xcodiq.vysteriarankedspawners.rank.api.impl.API;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public class RankStatsMenu {

    public RankStatsMenu(final Player player, final Island island, final Menu previousMenu) {
        if (island == null) return;
        RankedSpawners plugin = RankedSpawners.getInstance();
        FileConfiguration config = plugin.getConfig();
        FileConfiguration menuFile = plugin.getRankStatsMenuFile();

        API api = RankedSpawners.getAPI();

        Menu menu = MenuAPI.getInstance().createMenu(
                ChatUtils.format(menuFile.getString("title")),
                menuFile.getInt("rows")
        );

        // GLASS ITEMS
        for (int i = 0; i < menu.getInventory().getSize(); i++) {
            menu.addMenuItem(new MenuItem() {
                @Override
                public void onClick(Player player, InventoryClickType type) {
                }

                @Override
                public ItemStack getItemStack() {
                    return new ItemBuilder(
                            Material.getMaterial(menuFile.getString("glass-item.material")), 1,
                            (short) menuFile.getInt("glass-item.data")
                    )
                            .setName(ChatUtils.format(menuFile.getString("glass-item.displayname")))
                            .setLore(ChatUtils.format(menuFile.getStringList("glass-item.lore")))
                            .toItemStack();
                }
            }, i);
        }

        // BLOCKS
        menu.addMenuItem(new MenuItem() {
            @Override
            public void onClick(Player player, InventoryClickType type) {
                if (previousMenu != null) previousMenu.openMenu(player);
                player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
            }

            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(
                        Material.getMaterial(menuFile.getString("blocks.material")), 1,
                        (short) menuFile.getInt("blocks.data")
                )
                        .setName(ChatUtils.format(menuFile.getString("blocks.displayname")))
                        .setLore(ChatUtils.format(menuFile.getStringList("blocks.lore").stream()
                                .map(string -> string
                                        .replace("{BLOCKS_CURRENT}", String.valueOf(api.getStatistic(island, RankStatistic.BLOCKS)))
                                ).collect(Collectors.toList())))
                        .toItemStack();
            }
        }, menuFile.getInt("blocks.slot"));

        // MOBS
        menu.addMenuItem(new MenuItem() {
            @Override
            public void onClick(Player player, InventoryClickType type) {
                if (previousMenu != null) previousMenu.openMenu(player);
                player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
            }

            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(
                        Material.getMaterial(menuFile.getString("mobs.material")), 1,
                        (short) menuFile.getInt("mobs.data")
                )
                        .setName(ChatUtils.format(menuFile.getString("mobs.displayname")))
                        .setLore(ChatUtils.format(menuFile.getStringList("mobs.lore").stream()
                                .map(string -> string
                                        .replace("{MOBS_CURRENT}", String.valueOf(api.getStatistic(island, RankStatistic.MOBS)))
                                ).collect(Collectors.toList())))
                        .toItemStack();
            }
        }, menuFile.getInt("mobs.slot"));

        // FISH
        menu.addMenuItem(new MenuItem() {
            @Override
            public void onClick(Player player, InventoryClickType type) {
                if (previousMenu != null) previousMenu.openMenu(player);
                player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);

            }

            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(
                        Material.getMaterial(menuFile.getString("fish.material")), 1,
                        (short) menuFile.getInt("fish.data")
                )
                        .setName(ChatUtils.format(menuFile.getString("fish.displayname")))
                        .setLore(ChatUtils.format(menuFile.getStringList("fish.lore").stream()
                                .map(string -> string
                                        .replace("{FISH_CURRENT}", String.valueOf(api.getStatistic(island, RankStatistic.FISH)))
                                ).collect(Collectors.toList())))
                        .toItemStack();
            }
        }, menuFile.getInt("fish.slot"));

        // ADD CLOSE BEHAVEIOUR
        menu.setMenuCloseBehaviour((player1, menu1, b) -> {
            if (b || previousMenu == null) return;

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                previousMenu.openMenu(player1);
                player1.playSound(player1.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
            }, 2L);
        });

        // OPEN MENU FOR PLAYER
        menu.openMenu(player);
    }
}
