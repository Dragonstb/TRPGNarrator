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

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import dev.dragonstb.trpgnarrator.client.AMAccessor;
import dev.dragonstb.trpgnarrator.client.Globals;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;

/** The root node of the visual representation of the game board
 *
 * @author Dragonstb
 * @since 0.0.1
 */
final class BoardNode extends Node{

    private Geometry currentlyHighlighted;
    private final Map<Integer, Geometry> geometries = new HashMap<>();

    BoardNode(@NonNull BoardData data) {
        super(Globals.BOARD_NODE_NAME);
        init(data);
    }

    private void init(BoardData data) {
        Map<Integer, FieldData> map = data.getFields();
        map.values().forEach(field -> {
            Geometry geom = FieldGeometryFactory.makeFieldGeometry(field, AMAccessor.get());
            geom.setLocalTranslation(field.getLocation());

            // TODO: with the user data no in use, better write a class extending geometry that natively owns such a field 'id'
            int id = field.getId();
            geom.setUserData(Globals.FIELD_ID, id);
            geometries.put(id, geom);
            this.attachChild(geom);
        });
    }

    /** Returns an optional with the geometry of the field with the given id. The optional is empty if and only if there is no field with
     * such an id.
     *
     * @author Dragonstb
     * @since 0.0.1
     * @param id Id of the field.
     * @return Optional with the field if the id exists or empty if the id does not exists.
     */
    Optional<Geometry> getFieldGeometry(int id) {
        return Optional.ofNullable(geometries.get(id));
    }

    /** Hightlights the given field (if not null) and unhighlights the currently hightlighted field (if not null).
     *
     * @author Dragonstb
     * @since 0.0.1
     * @param geom Field to be highlighted. Pass {@code null} and no field becomes highlighted.
     */
    void highlightField(Geometry geom) {
        if(currentlyHighlighted!=null) {
            currentlyHighlighted.getMaterial().setColor("Color", ColorRGBA.Green.mult(.5f));
        }
        currentlyHighlighted = geom;
        if(currentlyHighlighted!=null) {
            currentlyHighlighted.getMaterial().setColor("Color", ColorRGBA.Green.mult(1.5f));
        }
    }

}
