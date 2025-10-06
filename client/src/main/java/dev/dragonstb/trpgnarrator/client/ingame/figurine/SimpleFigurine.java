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

package dev.dragonstb.trpgnarrator.client.ingame.figurine;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import dev.dragonstb.trpgnarrator.client.AMAccessor;
import dev.dragonstb.trpgnarrator.client.Globals;
import lombok.NonNull;

/** A simple figurine
 *
 * @author Dragonstb
 * @since 0.0.1;
 */
final class SimpleFigurine implements Figurine {

    private final String id;
    private final Node node;

    /**
     * @author Dragonstb
     * @since 0.0.1
     * @param id Id of the figurine.
     * @param color Color of the figurine.
     */
    SimpleFigurine(@NonNull String id, @NonNull ColorRGBA color) {
        this.id = id;
        node = new Node(Globals.FIGURINE_NODE_NAME+id);
        init(color);
    }

    /** Initializes.
     * @author Dragonstb
     * @since 0.0.1
     * @param color
     */
    private void init(ColorRGBA color) {
        float radius = .4f;
        float height = 1.3f;
        Cylinder cylinder = new Cylinder(4, 12, radius, height, true);
        Geometry geom = new Geometry("geom_"+id, cylinder); // TODO: proper naming
        Material mat = new Material(AMAccessor.get(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", color);
        mat.setColor("Specular", color.mult(1.2f));
        mat.setColor("Ambient", color.mult(.25f));
        geom.setMaterial(mat);
        geom.setLocalTranslation(0, height/2f, 0);
        geom.setLocalRotation(new Quaternion().fromAngleNormalAxis(FastMath.HALF_PI, Vector3f.UNIT_X));
        node.attachChild(geom);
    }

    @Override
    public Node getNode() {
        return node;
    }


}
