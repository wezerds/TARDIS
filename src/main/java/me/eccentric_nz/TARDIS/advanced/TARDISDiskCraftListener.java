/*
 * Copyright (C) 2013 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.advanced;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.BIOME_LOOKUP;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISDiskCraftListener implements Listener {

    private final TARDIS plugin;

    public TARDISDiskCraftListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraftBiomePresetDisk(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        String playerNameStr = player.getName();
        Inventory inv = event.getInventory();
        int slot = event.getRawSlot();
        if (inv.getType().equals(InventoryType.WORKBENCH) && slot == 0 && checkSlots(inv)) {
            // get the other ingredients
            List<ItemStack> items = getOtherItems(inv);
            ItemStack disk;
            if (inv.contains(Material.GREEN_RECORD)) {
                // check it is a Biome Storage Disk
                ItemStack is = inv.getItem(inv.first(Material.GREEN_RECORD));
                if (is != null && is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    if (im.hasDisplayName() && im.getDisplayName().equals("Biome Storage Disk") && im.hasLore()) {
                        List<String> lore = im.getLore();
                        if (lore.get(0).equals("Blank")) {
                            List<String> disk_lore = new ArrayList<String>();
                            // biome disk
                            int ladder = inv.first(Material.LADDER);
                            if (items.size() > 1 && ladder > 0) {
                                // mega biome
                                items.remove(inv.getItem(ladder));
                                String lookup = items.get(0).getType().toString() + "_B" + items.get(0).getData().getData();
                                try {
                                    String biome = BIOME_LOOKUP.valueOf(lookup).getUpper();
                                    disk_lore.add(biome);
                                } catch (IllegalArgumentException e) {
                                    plugin.debug("Could not get biome from craft item! " + e);
                                }
                            } else {
                                // regular biome
                                String lookup = items.get(0).getType().toString() + "_B" + items.get(0).getData().getData();
                                try {
                                    String biome = BIOME_LOOKUP.valueOf(lookup).getRegular();
                                    disk_lore.add(biome);
                                } catch (IllegalArgumentException e) {
                                    plugin.debug("Could not get biome from craft item! " + e);
                                }
                            }
                            if (disk_lore.size() > 0) {
                                disk = new ItemStack(Material.GREEN_RECORD, 1);
                                ItemMeta dim = disk.getItemMeta();
                                dim.setDisplayName("Biome Storage Disk");
                                dim.setLore(disk_lore);
                                disk.setItemMeta(dim);
                                inv.clear();
                                inv.setItem(0, disk);
                            }
                        } else {
                            player.sendMessage(plugin.pluginName + "You must use a blank Biome Storage Disk!");
                        }
                    }
                }
            } else {
                // check it is a Biome Storage Disk
                ItemStack is = inv.getItem(inv.first(Material.RECORD_6));
                if (is != null && is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    if (im.hasDisplayName() && im.getDisplayName().equals("Preset Storage Disk") && im.hasLore()) {
                        List<String> lore = im.getLore();
                        if (lore.get(0).equals("Blank")) {
                            // preset disk
                            if (items.size() > 0) {
                                Material m = items.get(0).getType();
                                String preset = "";
                                if (PRESET.getPreset(m) != null) {
                                    preset = PRESET.getPreset(m).toString();
                                }
                                if (!preset.isEmpty()) {
                                    List<String> disk_lore = Arrays.asList(new String[]{preset});
                                    disk = new ItemStack(Material.RECORD_6, 1);
                                    ItemMeta dim = disk.getItemMeta();
                                    dim.setDisplayName("Preset Storage Disk");
                                    dim.setLore(disk_lore);
                                    disk.setItemMeta(dim);
                                    inv.clear();
                                    inv.setItem(0, disk);
                                }
                            }
                        } else {
                            player.sendMessage(plugin.pluginName + "You must use a blank Preset Storage Disk!");
                        }
                    }
                }
            }
        }
    }

    private boolean checkSlots(Inventory inv) {
        boolean check = false;
        int count = 0;
        for (int i = 1; i < 10; i++) {
            if (inv.getItem(i) != null && !inv.getItem(i).getType().equals(Material.AIR)) {
                count++;
            }
        }
        if ((inv.contains(Material.GREEN_RECORD) || inv.contains(Material.RECORD_6)) && count > 1) {
            check = true;
        }
        return check;
    }

    private List<ItemStack> getOtherItems(Inventory inv) {
        List<ItemStack> items = new ArrayList<ItemStack>();
        for (ItemStack is : inv.getContents()) {
            if (is != null) {
                Material m = is.getType();
                if (!m.equals(Material.GREEN_RECORD) && !m.equals(Material.RECORD_6) && !m.equals(Material.AIR)) {
                    items.add(is);
                }
            }
        }
        return items;
    }
}
