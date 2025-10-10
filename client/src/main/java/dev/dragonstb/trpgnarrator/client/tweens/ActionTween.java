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

package dev.dragonstb.trpgnarrator.client.tweens;

import lombok.Getter;

/** A tween transits something from one state into another.
 *
 * TODO: These classes are going to be massively used by the virtual host when manipulating the scene. As such, this entire package will
 * move to either the virtual host or to a common module at some point.
 *
 * @since 0.0.1
 * @author Dragonstb
 */
public abstract class ActionTween {

    /** Length, in seconds. */
    @Getter private final float length;
    /** Time elapsed, in seconds. */
    @Getter private float time;
    /** Is done? */
    @Getter private boolean done = false;

    public ActionTween(float length) {
        this.length = length;
        time = 0;
    }

    /** Progresses the tween by the given amount of time. Reports if the tween completes after this step.
     * @since 0.0.1
     * @param dt Size of time step, in seconds.
     * @return Done after this step?
     */
    public boolean progress(float dt){
        time += dt;
        done = time >= length;
        internalAction(dt);
        return done;
    }

    public abstract void internalAction(float dt);

}
