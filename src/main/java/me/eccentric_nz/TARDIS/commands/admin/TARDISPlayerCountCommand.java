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
package me.eccentric_nz.TARDIS.commands.admin;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCount;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISPlayerCountCommand {

    private final TARDIS plugin;

    public TARDISPlayerCountCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean countPlayers(CommandSender sender, String[] args) {
        int max_count = plugin.getConfig().getInt("creation.count");
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("player", args[1]);
        ResultSetCount rsc = new ResultSetCount(plugin, where, false);
        if (rsc.resultSet()) {
            if (args.length == 3) {
                // set count
                int count = plugin.utils.parseNum(args[2]);
                HashMap<String, Object> setc = new HashMap<String, Object>();
                setc.put("count", count);
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("player", args[1]);
                QueryFactory qf = new QueryFactory(plugin);
                qf.doUpdate("t_count", setc, wherec);
                sender.sendMessage(plugin.pluginName + args[1] + "'s TARDIS count was set to: " + args[2] + " of " + max_count);
            } else {
                // display count
                sender.sendMessage(plugin.pluginName + args[1] + "'s TARDIS count is: " + rsc.getCount() + " of " + max_count);
            }
        } else {
            sender.sendMessage(plugin.pluginName + "That player doesn't have a TARDIS count.");
        }
        return true;
    }
}
