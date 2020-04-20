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
import dev.xcodiq.vysteriarankedspawners.utilities.NumberUtils;
import dev.xcodiq.vysteriarankedspawners.utilities.StringUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpawnerPurchaseMenu {

    public SpawnerPurchaseMenu(final Player player, final String name, final List<String> spawners, final Map<String, Integer> cost, final Map<String, List<String>> drops) {
        RankedSpawners plugin = RankedSpawners.getInstance();
        FileConfiguration config = plugin.getConfig();
        FileConfiguration menuFile = plugin.getSpawnerPurchaseMenuFile();
        Menu menu = MenuAPI.getInstance().createMenu(
                ChatUtils.format(menuFile.getString("title").replace("{NAME}", name)),
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
                            .setName(ChatUtils.format(menuFile.getString("glass-item.displayname").replace("{NAME}", name)))
                            .setLore(ChatUtils.format(menuFile.getStringList("glass-item.lore")))
                            .toItemStack();
                }
            }, i);
        }

        int slot = 0;
        for (String spawner : spawners) {
            menu.addMenuItem(new MenuItem() {
                @Override
                public void onClick(Player player, InventoryClickType type) {
                    Economy eco = RankedSpawners.getInstance().getEconomy();

                    if (type == InventoryClickType.RIGHT) {
                        player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
                        menu.closeMenu(player);
                        new SpawnerPurchaseAmountMenu(player, menu, getItemStack(), spawner, cost.get(spawner));
                        return;
                    }

                    if (eco.getBalance(player) >= cost.get(spawner)) {
                        eco.withdrawPlayer(player, cost.get(spawner));
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stacker give -s " + player.getName() + " spawner " + StringUtils.toStackName(spawner) + " 1");
                        player.sendMessage(ChatUtils.format(config.getString("messages.bought-spawner")
                                .replace("{NAME}", spawner)
                                .replace("{AMOUNT}", "1")));
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                        return;
                    }
                    player.sendMessage(ChatUtils.format(config.getString("messages.cannot-buy")));
                    player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1.0F, 1.0F);

                }

                @Override
                public ItemStack getItemStack() {
                    ItemBuilder itemBuilder = new ItemBuilder(
                            Material.getMaterial(menuFile.getString("spawner-item-format.material")), 1,
                            (short) menuFile.getInt("spawner-item-format.data")
                    );
                    itemBuilder.setName(ChatUtils.format(menuFile.getString("spawner-item-format.displayname").replace("{MOB}", spawner)));

                    itemBuilder.setLore(ChatUtils.format(menuFile.getStringList("spawner-item-format.lore")
                            .stream().map(string -> string
                                    .replace("{DROPS}", String.join(", ", drops.get(spawner)))
                                    .replace("{COST}", NumberUtils.formatInt(cost.get(spawner)))
                            ).collect(Collectors.toList())))
                            .toItemStack();

                    return itemBuilder.toItemStack();
                }
            }, getSlot(slot));
            slot++;
            if (slot == 2) slot = 0;
        }

        // OPEN MENU FOR PLAYER
        menu.openMenu(player);
    }

    private int getSlot(int integer) {
        switch (integer) {
            case 0:
                return 12;
            case 1:
                return 14;
        }
        return 0;
    }
}
