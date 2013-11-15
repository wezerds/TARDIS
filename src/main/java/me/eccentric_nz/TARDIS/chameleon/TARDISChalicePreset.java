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
package me.eccentric_nz.TARDIS.chameleon;

import java.util.EnumMap;
import me.eccentric_nz.TARDIS.TARDISConstants;

/**
 * A chameleon conversion is a repair procedure that technicians perform on
 * TARDIS chameleon circuits. The Fourth Doctor once said that the reason the
 * TARDIS' chameleon circuit was stuck was because he had "borrowed" it from
 * Gallifrey before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class TARDISChalicePreset {

    private final String chalice_id = "[[44,0,156,155],[44,0,156,155],[44,0,156,155],[44,0,156,155],[44,0,156,155],[44,0,156,155],[44,0,156,155],[71,71,156,155],[155,155,155,9],[0,0,0,0]]";
    private final String chalice_data = "[[7,0,6,0],[7,0,6,0],[7,0,5,0],[7,0,5,0],[7,0,7,0],[7,0,7,0],[7,0,7,0],[2,8,4,0],[0,0,0,0],[0,0,0,0]]";
    private final String ice_id = "[[79,0,79,79],[79,0,79,79],[79,0,79,79],[79,0,79,79],[79,0,79,79],[79,0,79,79],[79,0,79,79],[71,71,79,79],[79,79,79,79],[0,0,0,0]]";
    private final String ice_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[2,8,0,0],[0,0,0,0],[0,0,0,0]]";
    private final String glass_id = "[[20,0,20,20],[20,0,20,20],[20,0,20,20],[20,0,20,20],[20,0,20,20],[20,0,20,20],[20,0,20,20],[71,71,20,20],[20,20,20,20],[0,0,0,0]]";
    private final String glass_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[2,8,0,0],[0,0,0,0],[0,0,0,0]]";
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> chalice = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> ice = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> glass = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);

    public TARDISChalicePreset() {
    }

    public void makePresets() {
        TARDISChameleonPreset tcp = new TARDISChameleonPreset();
        for (TARDISConstants.COMPASS d : TARDISConstants.COMPASS.values()) {
            chalice.put(d, tcp.buildTARDISChameleonColumn(d, chalice_id, chalice_data, false));
            glass.put(d, tcp.buildTARDISChameleonColumn(d, glass_id, glass_data, false));
            ice.put(d, tcp.buildTARDISChameleonColumn(d, ice_id, ice_data, false));
        }
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getChalice() {
        return chalice;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getIce() {
        return ice;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getGlass() {
        return glass;
    }
}