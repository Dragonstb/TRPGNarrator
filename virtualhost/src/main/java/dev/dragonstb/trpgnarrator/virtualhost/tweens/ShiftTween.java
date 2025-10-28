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
import lombok.Getter;
import lombok.NonNull;

/** A tween that linearly interpolates from one point in space to another, but never further than the goal point.
 *
 * @since 0.0.1
 * @author Dragonstb
 */
public final class ShiftTween extends ActionTween{

    private final Vector3f goal;
    private final Vector3f start;
    private final Vector3f diff;
    @Getter @NonNull private final Vector3f currentPos;

    /**
     *
     * @param start Starting location.
     * @param goal Goal.
     * @param length Duration in seconds.
     */
    public ShiftTween(@NonNull Vector3f start, @NonNull Vector3f goal, float length) {
        super(length);
        this.goal = new Vector3f(goal);
        this.start = new Vector3f(start);
        diff = new Vector3f( goal.subtract(start) );
        currentPos = new Vector3f(start);
    }

    @Override
    public void internalAction(float dt) {
        if(!isDone()){
            currentPos.set(diff);
            float time = Math.max(0, getTime());
            currentPos.multLocal( time / getLength() );
            currentPos.addLocal(start);
        }
        else{
            currentPos.set(goal);
        }
    }

}
