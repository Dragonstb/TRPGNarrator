/*
 * Copyright (C) 2025 Dragonstb
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * See <http://www.gnu.org/licenses/gpl-2.0.html>
 */

package dev.dragonstb.trpgnarrator.client.ingame.board;

import com.jme3.math.Vector3f;
import java.util.HashMap;
import java.util.Map;

/** Collection of all {@link FieldData FieldData} and some stuff for dealing with them.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
final class BoardData {

    /** All fields of the board. */
    private final Map<Integer, FieldData> fields = new HashMap<>();

    BoardData() {
        init();
    }

    /** A mocking method used in early development.
     * @since 0.0.1
     */
    private void init() {
        // TODO: stop using this simple, stupid initialization and derive the objects from some sort of data
        float d = .5f;
        float dz = d * (float)Math.sqrt(2); // this is also the distanc between the centers of two adjacent fields
        float dx = 1.5f * d;

        int rangeZ = 5;
        int rangeX = 5;

        float x, z;
        float y = 0f;
        int nextId = 0;
        for (int tileZ = -rangeZ; tileZ <= rangeZ; tileZ++) {
            for (int tileX = -rangeX; tileX < rangeX; tileX++) {
                x = tileX * dx;
                z = tileZ * dz;
                Vector3f location = new Vector3f(x, y, z);
                FieldData data = new FieldData(nextId++, location);
                fields.put(data.getId(), data); // TODO: In real-use methods, check if id is unique and throw exception if it is not
            }
        }

        fields.values().forEach(field -> {
            fields.values().stream().filter(otherField -> otherField.getId() < field.getId()).forEach(otherField -> {
                // for each pair of adjacent fields, create a link once
                float dist = field.getLocation().subtract( otherField.getLocation() ).length();
                if( Math.abs(dist-dz) < .01f ) {
                    FieldLink link = new FieldLink(field, otherField);
                    field.addLink(link);
                    otherField.addLink(link);
                }
            });
        });

    }
}
