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
package me.eccentric_nz.TARDIS.ARS;

import java.util.Map;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;

/**
 *
 * @author eccentric_nz
 */
public class TARDISARSProcessor {

    private final TARDIS plugin;
    private final int id;
    private String error;
    private HashMap<TARDISARSSlot, TARDISARS> changed;
    private HashMap<TARDISARSJettison, TARDISARS> jettison;

    public TARDISARSProcessor(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
    }

    public boolean compare3DArray(int[][][] start, int[][][] end) {
        changed = new HashMap<TARDISARSSlot, TARDISARS>();
        jettison = new HashMap<TARDISARSJettison, TARDISARS>();
        for (int l = 0; l < 3; l++) {
            for (int x = 0; x < 9; x++) {
                for (int z = 0; z < 9; z++) {
                    if (start[l][x][z] != end[l][x][z]) {
                        if (end[l][x][z] == 46) {
                            TARDISARSJettison slot = new TARDISARSJettison();
                            slot.setL(l);
                            slot.setX(x);
                            slot.setZ(z);
                            slot.setPrior(start[l][x][z]);
                            jettison.put(slot, TARDISARS.getARS(end[l][x][z]));
                        } else {
                            TARDISARSSlot slot = new TARDISARSSlot();
                            slot.setL(l);
                            slot.setX(x);
                            slot.setZ(z);
                            changed.put(slot, TARDISARS.getARS(end[l][x][z]));
                        }
                    }
                }
            }
        }
        if (jettison.size() > 0 || changed.size() > 0) {
            return true;
        }
        return false;
    }

    public boolean checkCosts(HashMap<TARDISARSSlot, TARDISARS> changed, HashMap<TARDISARSJettison, TARDISARS> jettison) {
        int totalcost = 0;
        int recoveredcost = 0;
        // calculate energy gained by jettisons
        for (Map.Entry<TARDISARSJettison, TARDISARS> c : jettison.entrySet()) {
            recoveredcost += Math.round((plugin.getArtronConfig().getInt("jettison") / 100F) * plugin.getRoomsConfig().getInt("rooms." + c.getKey().getPrior() + ".cost"));
        }
        for (Map.Entry<TARDISARSSlot, TARDISARS> c : changed.entrySet()) {
            totalcost += plugin.getRoomsConfig().getInt("rooms." + c.getValue().toString() + ".cost");
        }
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            int energy = rs.getArtron_level();
            // check available energy vs cost
            if (totalcost - recoveredcost > energy) {
                this.error = "Insufficient Artron Energy";
                return false;
            }
        }
        return true;
    }

    public HashMap<TARDISARSSlot, TARDISARS> getChanged() {
        return changed;
    }

    public HashMap<TARDISARSJettison, TARDISARS> getJettison() {
        return jettison;
    }

    public String getError() {
        return error;
    }
}
