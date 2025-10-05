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

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import dev.dragonstb.trpgnarrator.client.AMAccessor;
import dev.dragonstb.trpgnarrator.client.Globals;
import java.util.Map;
import lombok.NonNull;

/** The root node of the visual representation of the game board
 *
 * @author Dragonstb
 * @since 0.0.1
 */
final class BoardNode extends Node{

    BoardNode(@NonNull BoardData data) {
        super(Globals.BOARD_NODE_NAME);
        init(data);
    }

    private void init(BoardData data) {
        Map<Integer, FieldData> map = data.getFields();
        map.values().forEach(field -> {
            Geometry geom = FieldGeometryFactory.makeFieldGeometry(field, AMAccessor.get());
            geom.setLocalTranslation(field.getLocation());
            this.attachChild(geom);
        });
    }

}
