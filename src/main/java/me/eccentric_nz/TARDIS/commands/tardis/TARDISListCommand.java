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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.utility.TARDISLister;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISListCommand {

    private final TARDIS plugin;

    public TARDISListCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean doList(Player player, String[] args) {
        if (player.hasPermission("tardis.list")) {
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("owner", player.getName());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                player.sendMessage(plugin.pluginName + MESSAGE.NO_TARDIS.getText());
                return false;
            }
            if (args.length < 2 || (!args[1].equalsIgnoreCase("saves") && !args[1].equalsIgnoreCase("companions") && !args[1].equalsIgnoreCase("areas") && !args[1].equalsIgnoreCase("rechargers"))) {
                player.sendMessage(plugin.pluginName + "You need to specify which TARDIS list you want to view! [saves|companions|areas|rechargers]");
                return false;
            }
            TARDISLister.list(player, args[1]);
            return true;
        } else {
            player.sendMessage(plugin.pluginName + MESSAGE.NO_PERMS.getText());
            return false;
        }
    }
}
