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

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import dev.dragonstb.trpgnarrator.client.Globals;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author Dragonstb
 * @since 0.0.1
 */
class FieldGeometry extends Geometry{

    /** Id of the field visualized by this geometry. */
    @Getter(AccessLevel.PACKAGE) private final int id;

    /** Generates. The name of the geometry is the
     * {@link dev.dragonstb.trpgnarrator.client.Globals#FIELD_GEOM_NAME FIELD_GEOM_NAME} defined in Globals appended by the field id.
     *
     * @param data The data of the field represented.
     * @param assetManager An asset manager.
     */
    FieldGeometry(@NonNull FieldData data, @NonNull AssetManager assetManager) {
        super(
            Globals.FIELD_GEOM_NAME+String.valueOf(data.getId()),
            FieldGeometryFactory.makeFieldMesh()
        );
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green.mult(.5f));
        this.setMaterial(mat);
        this.updateModelBound();
        this.id = data.getId();
    }

    /** Highlights or unhighlights the geometry.
     *
     * @since 0.0.1
     * @author Dragonstb
     * @param highlighted Shall the geometry be highlighted?
     */
    void setHighlighted(boolean highlighted) {
        // TODO: define and use a specialized material with the highlight effect being baked in
        this.getMaterial().setColor("Color", ColorRGBA.Green.mult( highlighted ? 1.5f : 0.5f ));
    }
}
