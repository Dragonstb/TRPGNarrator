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

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import lombok.NonNull;

/** Finds the id of the {@link dev.dragonstb.trpgnarrator.client.ingame.board.FieldData data of the field} under the mouse pointer.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
final class MouseFieldPicker implements Callable<Optional<Integer>>{

    // TODO: take care of the fact that this called is called in an asynchonous context and concurrently accesses the board node
    // together with other threads that even change the scene graph of the board node. Also, this method is called every time the mouse
    // pointer moves, which is expected to happen very often.

    private final Node node;
    private final Ray ray;

    MouseFieldPicker(@NonNull Node node, @NonNull Ray ray) {
        // TODO: optimize access when taking care of the concurrent operations
        this.node = node;
        this.ray = ray;
    }

    @Override
    public Optional<Integer> call() throws Exception {
        // TODO: optimize search performance
        List<Spatial> children = node.getChildren();
        CollisionResults collisions = new CollisionResults();

        float dist = Float.POSITIVE_INFINITY;
        Geometry geom = null;


        for (Spatial child: children) {
            child.collideWith(ray, collisions);
            CollisionResult closest = collisions.getClosestCollision();
            if(closest != null && closest.getDistance() < dist && (closest.getGeometry() instanceof FieldGeometry)) {
                geom = closest.getGeometry();
                dist = closest.getDistance();
            }
        }

        if(geom == null) {
            return Optional.empty();
        }

        int id = ((FieldGeometry)geom).getId();
        return Optional.of(id);
    }

}
