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
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import dev.dragonstb.trpgnarrator.client.Globals;
import lombok.NonNull;

/** Creates geometries of board fields from {@link FieldData FieldData}.
 *
 * @author Dragonstb
 * @since 0.0.1;
 */
final class FieldGeometryFactory {

    /** Makes the geometry of a field of the board. The name of the geometry is the
     * {@link dev.dragonstb.trpgnarrator.client.Globals#FIELD_GEOM_NAME FIELD_GEOM_NAME} defined in Globals appended by the field id.
     * @author Dragonstb
     * @since 0.0.1
     * @param data Data of the field.
     * @param assetManager The asset manager of the game.
     * @return The geometry of the field.
     */
    static Geometry makeFieldGeometry(@NonNull FieldData data, @NonNull AssetManager assetManager) {
        Mesh mesh = makeFieldMesh();
        String name = "fieldGeometry_"+String.valueOf(data.getId());
        Geometry geom = new Geometry(name, mesh);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green.mult(.5f));
        geom.setMaterial(mat);
        return geom;
    }

    /** Handicrafts the mesh of a field.
     * @author Dragonstb
     * @since 0.0.1
     * @return Mesh of a field of the game board.
     */
    static Mesh makeFieldMesh() {
        float d = Globals.FIELD_RADIUS;
        float dx = d * .5f * FastMath.sqrt(3);
        float dz = .5f * d;

        float[] positions = new float[]{
              0, 0,   d,
             dx, 0,  dz,
             dx, 0, -dz,
              0, 0,  -d,
            -dx, 0, -dz,
            -dx, 0,  dz
        };

        // all six normals are (0,1,0)
        float[] normals = new float[3*6];
        for (int idx = 0; idx < normals.length; idx++) {
            normals[idx] = idx%3 == 1 ? 1 : 0;
        }

        byte[] indices = new byte[] {
            1, 2, 3,
            3, 4, 5,
            5, 0, 1,
            1, 3, 5
        };

        Mesh mesh = new Mesh();

        mesh.setBuffer(VertexBuffer.Type.Position, 3, positions);
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, normals);
        mesh.setBuffer(VertexBuffer.Type.Index, 3, indices);
        mesh.updateBound();

        return mesh;
    }

}
