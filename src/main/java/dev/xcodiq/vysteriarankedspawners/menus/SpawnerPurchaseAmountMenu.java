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

import java.util.stream.Collectors;

public class SpawnerPurchaseAmountMenu {

    public SpawnerPurchaseAmountMenu(final Player player, final Menu previousMenu, final ItemStack itemToBuy, final String spawner, final int cost) {
        RankedSpawners plugin = RankedSpawners.getInstance();
        FileConfiguration config = plugin.getConfig();
        FileConfiguration menuFile = plugin.getSpawnerPurchaseAmountMenuFile();

        Menu menu = MenuAPI.getInstance().createMenu(
                ChatUtils.format(menuFile.getString("title").replace("{NAME}", spawner)),
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
                            .setName(ChatUtils.format(menuFile.getString("glass-item.displayname").replace("{NAME}", spawner)))
                            .setLore(ChatUtils.format(menuFile.getStringList("glass-item.lore")))
                            .toItemStack();
                }
            }, i);
        }

//        // BUY ITEMS
        int slot = 9;
        for (int i = 0; i < 9; i++) {
            int finalSlot = slot;
            menu.addMenuItem(new MenuItem() {
                @Override
                public void onClick(Player player, InventoryClickType type) {
                    Economy eco = RankedSpawners.getInstance().getEconomy();

                    int totalCost = getAmountFromSlot(finalSlot) * cost;

                    if (eco.getBalance(player) >= totalCost) {
                        eco.withdrawPlayer(player, totalCost);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stacker give -s " + player.getName() + " spawner " + StringUtils.toStackName(spawner) + " " + getAmountFromSlot(finalSlot));
                        player.sendMessage(ChatUtils.format(config.getString("messages.bought-spawner")
                                .replace("{NAME}", spawner)
                                .replace("{AMOUNT}", String.valueOf(getAmountFromSlot(finalSlot)))));
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                    } else {
                        player.sendMessage(ChatUtils.format(config.getString("messages.cannot-buy")));
                        player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1.0F, 1.0F);
                    }
                }

                @Override
                public ItemStack getItemStack() {
                    ItemBuilder itemBuilder = new ItemBuilder(
                            Material.getMaterial(menuFile.getString("spawner-item-format.material")), getAmountFromSlot(finalSlot),
                            (short) menuFile.getInt("spawner-item-format.data")
                    );

                    itemBuilder.setName(ChatUtils.format(menuFile.getString("spawner-item-format.displayname")
                            .replace("{MOB}", spawner)
                            .replace("{AMOUNT}", String.valueOf(getAmountFromSlot(finalSlot)))));

                    int totalCost = getAmountFromSlot(finalSlot) * cost;

                    itemBuilder.setLore(ChatUtils.format(menuFile.getStringList("spawner-item-format.lore")
                            .stream().map(string -> string
                                    .replace("{COST}", NumberUtils.formatInt(totalCost))
                                    .replace("{AMOUNT}", String.valueOf(getAmountFromSlot(finalSlot)))
                            ).collect(Collectors.toList())));

                    return itemBuilder.toItemStack();
                }

            }, slot);
            slot++;
            if (slot == 18) break;
        }

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

    private int getAmountFromSlot(int slot) {
        switch (slot) {
            case 9:
                return 1;
            case 10:
                return 2;
            case 11:
                return 4;
            case 12:
                return 8;
            case 13:
                return 16;
            case 14:
                return 24;
            case 15:
                return 32;
            case 16:
                return 48;
            case 17:
                return 64;
        }
        return 1;
    }
}
