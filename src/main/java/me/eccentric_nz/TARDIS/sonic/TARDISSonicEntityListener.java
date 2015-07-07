/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.sonic;

import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSonicEntityListener implements Listener {

    private final TARDIS plugin;
    private final Material sonic;
    private final HashMap<String, Long> timeout = new HashMap<String, Long>();

    public TARDISSonicEntityListener(TARDIS plugin) {
        this.plugin = plugin;
        String[] split = plugin.getRecipesConfig().getString("shaped.Sonic Screwdriver.result").split(":");
        this.sonic = Material.valueOf(split[0]);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        long now = System.currentTimeMillis();
        final ItemStack is = player.getItemInHand();
        if (is.getType().equals(sonic) && is.hasItemMeta()) {
            ItemMeta im = player.getItemInHand().getItemMeta();
            if (ChatColor.stripColor(im.getDisplayName()).equals("Sonic Screwdriver")) {
                List<String> lore = im.getLore();
                Entity ent = event.getRightClicked();
                if (ent instanceof Player) {
                    final Player scanned = (Player) ent;
                    plugin.getGeneralKeeper().getSonicListener().playSonicSound(player, now, 3050L, "sonic_screwdriver");
                    if (player.hasPermission("tardis.sonic.admin") && lore != null && lore.contains("Admin Upgrade") && player.isSneaking()) {
                        TARDISMessage.send(player, "SONIC_INV");
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Inventory pinv = scanned.getInventory();
                                ItemStack[] items = pinv.getContents();
                                Inventory menu = plugin.getServer().createInventory(player, pinv.getSize(), "§4" + scanned.getName() + "'s Inventory");
                                menu.setContents(items);
                                player.openInventory(menu);
                            }
                        }, 40L);
                    } else if (player.hasPermission("tardis.sonic.bio") && lore != null && lore.contains("Bio-scanner Upgrade")) {
                        TARDISMessage.send(player, "SONIC_PLAYER");
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                // getHealth() / getMaxHealth() * getHealthScale()
                                double health = scanned.getHealth() / scanned.getMaxHealth() * scanned.getHealthScale();
                                float hunger = (scanned.getFoodLevel() / 20F) * 100;
                                TARDISMessage.send(player, "SONIC_NAME", scanned.getName());
                                TARDISMessage.send(player, true, "SONIC_AGE", convertTicksToTime(scanned.getTicksLived()));
                                TARDISMessage.send(player, true, "SONIC_HEALTH", String.format("%f", health));
                                TARDISMessage.send(player, true, "SONIC_HUNGER", String.format("%.2f", hunger));
                            }
                        }, 40L);
                    }
                }
            }
        }
    }

    private String convertTicksToTime(int time) {
        // convert to seconds
        int seconds = time / 20;
        int h = seconds / 3600;
        int remainder = seconds - (h * 3600);
        int m = remainder / 60;
        int s = remainder - (m * 60);
        String gh = (h > 1 || h == 0) ? " hours " : " hour ";
        String gm = (m > 1 || m == 0) ? " minutes " : " minute ";
        String gs = (s > 1 || s == 0) ? " seconds" : " second";
        return h + gh + m + gm + s + gs;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInventoryViewClick(InventoryClickEvent event) {
        String title = event.getInventory().getTitle();
        if (title.startsWith("§4") && title.endsWith("'s Inventory")) {
            event.setCancelled(true);
        }
    }
}
