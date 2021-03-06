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
package me.eccentric_nz.TARDIS.advanced;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.ARS.TARDISARSInventory;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonInventory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.travel.TARDISSaveSignInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTemporalLocatorInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eccentric_nz
 */
public class TARDISConsoleSwitchListener implements Listener {

    private final TARDIS plugin;

    public TARDISConsoleSwitchListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("Deprecation")
    @EventHandler
    public void onConsoleInventoryClick(final InventoryClickEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Inventory inv = event.getInventory();
        if (inv.getTitle().equals("§4TARDIS Console")) {
            if (event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                event.setCancelled(true);
                final ItemStack item = inv.getItem(event.getRawSlot());
                if (item != null && item.getType().equals(Material.MAP)) {
                    final Player p = (Player) event.getWhoClicked();
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("owner", p.getName());
                    final ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (rs.resultSet()) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                ItemStack[] stack;
                                Inventory new_inv;
                                switch (item.getData().getData()) {
                                    case (byte) 1966: // Chameleon circuit
                                        new_inv = plugin.getServer().createInventory(p, 54, "§4Chameleon Circuit");
                                        stack = new TARDISChameleonInventory(rs.isChamele_on(), rs.isAdapti_on()).getTerminal();
                                        break;
                                    case (byte) 1973: // ARS circuit
                                        new_inv = plugin.getServer().createInventory(p, 54, "§4Architectural Reconfiguration");
                                        stack = new TARDISARSInventory().getTerminal();
                                        break;
                                    case (byte) 1974: // Temporal circuit
                                        new_inv = plugin.getServer().createInventory(p, 27, "§4Temporal Locator");
                                        stack = new TARDISTemporalLocatorInventory().getTerminal();
                                        break;
                                    case (byte) 1975: // Memory circuit (saves/areas)
                                        new_inv = plugin.getServer().createInventory(p, 54, "§4TARDIS saves");
                                        stack = new TARDISSaveSignInventory(plugin, rs.getTardis_id()).getTerminal();
                                        break;
                                    default: // Input circuit (terminal)
                                        new_inv = plugin.getServer().createInventory(p, 54, "§4Destination Terminal");
                                        stack = new TARDISTerminalInventory().getTerminal();
                                        break;
                                }
                                // close inventory
                                p.closeInventory();
                                // open new inventory
                                new_inv.setContents(stack);
                                p.openInventory(new_inv);
                            }
                        }, 1L);
                    } else {
                        p.sendMessage(plugin.pluginName + MESSAGE.NO_TARDIS.getText());
                    }
                }
            }
        }
    }
}
