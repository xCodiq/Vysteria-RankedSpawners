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

import dev.xcodiq.vysterialibrary.library.commons.menu.InventoryClickType;
import dev.xcodiq.vysterialibrary.library.commons.menu.Menu;
import dev.xcodiq.vysterialibrary.library.commons.menu.MenuAPI;
import dev.xcodiq.vysterialibrary.library.commons.menu.MenuItem;
import dev.xcodiq.vysterialibrary.library.utilities.ChatUtils;
import dev.xcodiq.vysterialibrary.library.utilities.item.ItemBuilder;
import dev.xcodiq.vysteriarankedspawners.RankedSpawners;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RankTiersMenu {

    public RankTiersMenu(final Player player, final Menu previousMenu) {
        RankedSpawners plugin = RankedSpawners.getInstance();
        FileConfiguration menuFile = plugin.getRankTiersMenuFIle();

        Menu menu = MenuAPI.getInstance().createMenu(
                ChatUtils.format(menuFile.getString("title")),
                menuFile.getInt("rows")
        );

        List<Integer> pageSlots = Arrays.asList(11, 12, 13, 14, 15, 20, 21, 22, 23, 24);
        List<MenuItem> menuItems = new ArrayList<>();

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

        // RANK TIER ITEMS
        for (String key : plugin.getRanksFile().getConfigurationSection("ranks").getKeys(false)) {
            final ConfigurationSection section = plugin.getRanksFile().getConfigurationSection("ranks." + key);
            if (key.equals("0")) continue;
            menuItems.add(new MenuItem() {
                @Override
                public void onClick(Player player, InventoryClickType type) {
                    previousMenu.openMenu(player);
                    player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
                }

                @Override
                public ItemStack getItemStack() {
                    ItemBuilder itemBuilder = new ItemBuilder(
                            Material.getMaterial(menuFile.getString("rank-tier-format.material")), 1,
                            (short) menuFile.getInt("rank-tier-format.data")
                    );
                    itemBuilder.setName(ChatUtils.format(menuFile.getString("rank-tier-format.displayname").replace("{RANK}", section.getString("name"))));

                    List<String> itemLore = new ArrayList<>(menuFile.getStringList("rank-tier-format.lore"));
                    List<String> newLore = new ArrayList<>();


                    itemLore.forEach(string -> {
                        if (string.equalsIgnoreCase("{REWARDS}")) {
                            for (String reward : section.getStringList("rewards")) newLore.add("  &5• &7" + reward);
                            return;
                        }
                        newLore.add(string);
                    });

                    itemBuilder.setLore(ChatUtils.format(newLore));
                    return itemBuilder.toItemStack();
                }
            });
        }

        menu.setupPages(menuItems, pageSlots);

        menu.setMenuCloseBehaviour((player1, menu1, b) -> {
            if (b || previousMenu == null) return;

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                previousMenu.openMenu(player1);
                player1.playSound(player1.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
            }, 2L);
        });

        menu.openMenu(player);
    }
}
