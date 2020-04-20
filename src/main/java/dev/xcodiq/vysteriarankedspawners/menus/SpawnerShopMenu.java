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
import dev.xcodiq.vysteriarankedspawners.rank.api.impl.API;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpawnerShopMenu {

    private final RankedSpawners plugin = RankedSpawners.getInstance();
    private final FileConfiguration menuFile = plugin.getSpawnerShopMenuFile();

    public SpawnerShopMenu(final Player player) {

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

        // SPAWNER ITEMS
        for (String key : menuFile.getConfigurationSection("categories").getKeys(false)) {
            ConfigurationSection section = menuFile.getConfigurationSection("categories." + key);

            ConfigurationSection spawnerSection = section.getConfigurationSection("mob-spawners");

            List<String> spawners = new ArrayList<>();
            Map<String, Integer> costMap = new HashMap<>();
            Map<String, List<String>> dropsMap = new HashMap<>();

            for (String spawner : spawnerSection.getKeys(false)) {
                spawners.add(spawner);
                costMap.put(spawner, spawnerSection.getInt(spawner + ".cost"));
                dropsMap.put(spawner, spawnerSection.getStringList(spawner + ".drops"));
            }

            // Add the actual item
            addSpawnerShopItem(
                    section.getInt("slot"),
                    key,
                    section.getInt("rank-required"),
                    player,
                    menu,
                    spawners,
                    costMap,
                    dropsMap
            );
        }


        // OPEN MENU FOR PLAYER
        menu.openMenu(player);
    }

    private void addSpawnerShopItem(int slot, String name, int rankRequired, Player player, Menu menu, List<String> spawners, Map<String, Integer> costMap, Map<String, List<String>> dropsMap) {
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();
        API api = RankedSpawners.getAPI();

        menu.addMenuItem(new MenuItem() {
            @Override
            public void onClick(Player player, InventoryClickType type) {
                if (api.getRankInt(island) >= rankRequired) {
                    player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
                    menu.closeMenu(player);
                    new SpawnerPurchaseMenu(player, name, spawners, costMap, dropsMap);
                    return;
                }
                player.sendMessage(ChatUtils.format(RankedSpawners.getInstance().getConfig().getString("messages.rank.cannot-open-shop-yet")));
                player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1.0F, 1.0F);
            }

            @Override
            public ItemStack getItemStack() {
                if (island == null) return new ItemStack(Material.DIRT);

                ItemBuilder itemBuilder = new ItemBuilder(
                        Material.getMaterial(menuFile.getString("spawner-item-format.material")), 1,
                        (short) menuFile.getInt("spawner-item-format.data")
                );

                itemBuilder.setName(ChatUtils.format(menuFile.getString("spawner-item-format.displayname")
                        .replace("{NAME}", name)));

                itemBuilder.setLore(ChatUtils.format(menuFile.getStringList("spawner-item-format.lore")
                        .stream().map(string -> string
                                .replace("{STATUS}", api.getRankInt(island) < rankRequired ? "&cLocked" : "&aUnlocked")
                                .replace("{RANK_REQUIRED}", String.valueOf(rankRequired))
                                .replace("{SPAWNERS}", String.join(", ", spawners))
                        ).collect(Collectors.toList())));

                if (api.getRankInt(island) >= rankRequired) {
                    itemBuilder.addEnchantment(Enchantment.DURABILITY, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
                }
                return itemBuilder.toItemStack();
            }
        }, slot);
    }
}
