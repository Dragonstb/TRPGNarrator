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

package dev.dragonstb.trpgnarrator.client;

import com.jme3.math.Vector3f;

/** Holds some constants on a global scope.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public final class Globals {

    public static final Vector3f WORLD_UP = Vector3f.UNIT_Y;

    // ingame-relayed contstants

    /** Name of the root node of the {@link dev.dragonstb.trpgnarrator.client.ingame.IngameAppState IngameAppState}. */
    public static final String INGAME_ROOTNODE_NAME = "ingameRootNode";



    // board-related constants

    /* Radius of a hex field, in WU. Two oppsing corners of a hex field will be twice this distance apart. */
    public static final float FIELD_RADIUS = .5f;
    /** Name of the node that is the board. */
    public static final String BOARD_NODE_NAME = "boardNode";
    /** Base name of the board field geometries. Become appended by their field ids. */
    public static final String FIELD_GEOM_NAME = "fieldGeometry_";
    /** User data key relating the field id on field geometries. */
    public static final String FIELD_ID = "fieldID";



    // figurine-related constants

    /** Base name of the root node of a figurine. Usually becomes appended by the figurine's id in an actual instance. */
    public static final String FIGURINE_NODE_NAME = "figurineNode_";
}
