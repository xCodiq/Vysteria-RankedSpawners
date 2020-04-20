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

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import dev.xcodiq.vysterialibrary.library.commons.menu.InventoryClickType;
import dev.xcodiq.vysterialibrary.library.commons.menu.Menu;
import dev.xcodiq.vysterialibrary.library.commons.menu.MenuAPI;
import dev.xcodiq.vysterialibrary.library.commons.menu.MenuItem;
import dev.xcodiq.vysterialibrary.library.utilities.ChatUtils;
import dev.xcodiq.vysterialibrary.library.utilities.item.ItemBuilder;
import dev.xcodiq.vysteriarankedspawners.RankedSpawners;
import dev.xcodiq.vysteriarankedspawners.rank.Rank;
import dev.xcodiq.vysteriarankedspawners.rank.RankStatistic;
import dev.xcodiq.vysteriarankedspawners.rank.api.impl.API;
import dev.xcodiq.vysteriarankedspawners.utilities.ProgressUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IslandRankMenu {

    public IslandRankMenu(final Player player, Island island) {
        RankedSpawners plugin = RankedSpawners.getInstance();
        FileConfiguration config = plugin.getConfig();
        FileConfiguration menuFile = plugin.getIslandRankMenuFile();

        API api = RankedSpawners.getAPI();

        Menu menu = MenuAPI.getInstance().createMenu(
                ChatUtils.format(menuFile.getString("title")),
                menuFile.getInt("rows")
        );

        int nextRank = Rank.getNextAsInt(island);
        ConfigurationSection section = plugin.getRanksFile().getConfigurationSection("ranks." + nextRank);

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

        // RANKUP INFORMATION
        menu.addMenuItem(new MenuItem() {
                             @Override
                             public void onClick(Player player, InventoryClickType type) {
                                 if (nextRank == -1) {
                                     player.sendMessage(ChatUtils.format(config.getString("messages.rank.no-rankup")));
                                     player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
                                     player.closeInventory();
                                     return;
                                 }

                                 if (SuperiorSkyblockAPI.getPlayer(player).getPlayerRole().getWeight() < 2) {
                                     player.sendMessage(ChatUtils.format(config.getString("messages.rank.only-owners-admins")));
                                     player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1.0F, 1.0F);
                                     player.closeInventory();
                                     return;
                                 }

                                 if (api.getStatistic(island, RankStatistic.BLOCKS) < section.getInt("blocks") ||
                                         api.getStatistic(island, RankStatistic.MOBS) < section.getInt("mobs") ||
                                         api.getStatistic(island, RankStatistic.FISH) < section.getInt("fish")) {
                                     player.sendMessage(ChatUtils.format(config.getString("messages.rank.cannot-rankup-yet")));
                                     player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1.0F, 1.0F);
                                     player.closeInventory();
                                     return;
                                 }

                                 api.setRank(island, Rank.getByName(String.valueOf(nextRank)));
                                 api.resetStatitics(island);
                                 api.saveItems();

                                 player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                                 player.closeInventory();

                                 section.getStringList("commands").forEach(command -> {
                                     command = command.replace("{PLAYER}", player.getName());
                                     Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                 });
                             }

                             @Override
                             public ItemStack getItemStack() {
                                 if (nextRank == -1) {
                                     return new ItemBuilder(
                                             Material.getMaterial(menuFile.getString("rankup-information-item.completed.material")), 1,
                                             (short) menuFile.getInt("rankup-informatino-item.completed.data"))

                                             .setName(ChatUtils.format(menuFile.getString("rankup-information-item.completed.displayname")))
                                             .setLore(ChatUtils.format(menuFile.getStringList("rankup-information-item.completed.lore")))
                                             .toItemStack();
                                 }
                                 int max = section.getInt("blocks") + section.getInt("fish") + section.getInt("mobs");
                                 int current = api.getStatistic(island, RankStatistic.BLOCKS) + api.getStatistic(island, RankStatistic.MOBS) + api.getStatistic(island, RankStatistic.FISH);
                                 String progressBar = IslandRankMenu.this.getProgressBar(current, max);
                                 String percentage = String.valueOf(ProgressUtils.getPercentage(current, max));


                                 return new ItemBuilder(
                                         Material.getMaterial(menuFile.getString("rankup-information-item.not-completed.material")), 1,
                                         (short) menuFile.getInt("rankup-informatino-item.not-completed.data"))

                                         .setName(ChatUtils.format(menuFile.getString("rankup-information-item.not-completed.displayname")
                                                 .replace("{CURRENT_RANK}", api.getRank(island).getName())))
                                         .setLore(ChatUtils.format(menuFile.getStringList("rankup-information-item.not-completed.lore")
                                                 .stream().map(string -> string
                                                         .replace("{PROGRESS_BAR}", progressBar)
                                                         .replace("{PROGRESS_PERCENTAGE}", percentage)
                                                         .replace("{BLOCKS_CURRENT}", "" + api.getStatistic(island, RankStatistic.BLOCKS))
                                                         .replace("{MOBS_CURRENT}", "" + api.getStatistic(island, RankStatistic.MOBS))
                                                         .replace("{FISH_CURRENT}", "" + api.getStatistic(island, RankStatistic.FISH))
                                                         .replace("{BLOCKS_MAX}", "" + section.getInt("blocks"))
                                                         .replace("{MOBS_MAX}", "" + section.getInt("mobs"))
                                                         .replace("{FISH_MAX}", "" + section.getInt("fish"))
                                                         .replace("{CURRENT_RANK}", api.getRank(island).getName())
                                                 ).collect(Collectors.toList())))
                                         .toItemStack();
                             }
                         }
                , menuFile.getInt("rankup-information-item.slot"));

        // RANK REWARDS
        menu.addMenuItem(new MenuItem() {
            @Override
            public void onClick(Player player, InventoryClickType type) {
                player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
                player.closeInventory();
            }

            @Override
            public ItemStack getItemStack() {
                ItemBuilder itemBuilder = new ItemBuilder(
                        Material.getMaterial(menuFile.getString("rankup-rewards.material")), 1,
                        (short) menuFile.getInt("rankup-rewards.data")
                );

                itemBuilder.setName(ChatUtils.format(menuFile.getString("rankup-rewards.displayname")));
                List<String> itemLore = new ArrayList<>(menuFile.getStringList("rankup-rewards.lore"));
                List<String> newLore = new ArrayList<>();

                itemLore.forEach(string -> {
                    if (string.equalsIgnoreCase("{REWARDS}")) {
                        if (nextRank == -1) {
                            newLore.add("  &5• &7None, already max ranked!");
                        } else {
                            for (String reward : section.getStringList("rewards")) newLore.add("  &5• &7" + reward);
                            return;
                        }
                    }
                    newLore.add(string);
                });

                itemBuilder.setLore(ChatUtils.format(newLore));
                return itemBuilder.toItemStack();
            }
        }, menuFile.getInt("rankup-rewards.slot"));

        // RANK STATISTICS
        menu.addMenuItem(new MenuItem() {
            @Override
            public void onClick(Player player, InventoryClickType type) {
                player.sendMessage("TEST");
                player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
                menu.closeMenu(player);
                new RankStatsMenu(player, island, menu);
            }

            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(
                        Material.getMaterial(menuFile.getString("rankup-statistics.material")), 1,
                        (short) menuFile.getInt("rankup-statistics.data"))

                        .setName(ChatUtils.format(menuFile.getString("rankup-statistics.displayname")))
                        .setLore(ChatUtils.format(menuFile.getStringList("rankup-statistics.lore")))
                        .toItemStack();
            }
        }, menuFile.getInt("rankup-statistics.slot"));

        // RANK LIST
        menu.addMenuItem(new MenuItem() {
            @Override
            public void onClick(Player player, InventoryClickType type) {
                player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
                menu.closeMenu(player);
                new RankTiersMenu(player, menu);
            }

            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(
                        Material.getMaterial(menuFile.getString("rankup-list.material")), 1,
                        (short) menuFile.getInt("rankup-list.data"))

                        .setName(ChatUtils.format(menuFile.getString("rankup-list.displayname")))
                        .setLore(ChatUtils.format(menuFile.getStringList("rankup-list.lore")))
                        .toItemStack();
            }
        }, menuFile.getInt("rankup-list.slot"));

        // OPEN MENU FOR PLAYER
        menu.openMenu(player);

    }

    private String getProgressBar(int current, int max) {
        return ProgressUtils.getProgressBar(current, max, 36, '|', "&a", "&c");
    }
}
