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

package dev.dragonstb.trpgnarrator.virtualhost.tweens;

import com.jme3.math.Vector3f;
import dev.dragonstb.trpgnarrator.virtualhost.generic.Locateable;
import lombok.NonNull;

/** A tween that shifts objects based on a {@link ShiftTween ShiftTween}.
 *
 * @author Dragonstb
 * @since 0.0.2
 */
public final class ShiftObjectTween extends ActionTween {

    private final ShiftTween tween;
    private final Locateable spatial;

    /** Generates.
     *
     * @author Dragonstb
     * @since 0.0.2
     * @param spatial
     * @param start
     * @param goal
     * @param length
     */
    public ShiftObjectTween(@NonNull Locateable spatial, @NonNull Vector3f start, @NonNull Vector3f goal, float length) {
        this(spatial, new ShiftTween(start, goal, length), length);
    }

    /** Generates.
     *
     * @author Dragonstb
     * @since 0.0.2
     * @param spatial
     * @param tween
     * @param length
     */
    public ShiftObjectTween(@NonNull Locateable spatial, @NonNull ShiftTween tween, float length) {
        super(length);
        this.tween = tween;
        this.spatial = spatial;
    }

    @Override
    public void internalAction(float dt) {
        tween.internalAction(dt);
        Vector3f loc = tween.getCurrentPos();
        spatial.setLocation(loc);
    }

}
