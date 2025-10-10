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

import com.jme3.math.FastMath;
import lombok.Getter;

/** Changes an angle to a target angle with a given speed.
 * @since 0.0.1
 * @author Dragonstb
 */
public class RotateAngleTween extends ActionTween{

    private final float startAngle;
    private final float diff;
    @Getter private float currentAngle;

    public RotateAngleTween(float startAngle, float goalAngle, float length) {
        super(length);

        // TODO: such common calculations can end up in a specialized class.
        // TODO: we are assuming that the angles are properly in their value, not 5290*PI or so. diff should be between -PI and +PI after
        // in the end.
        float pdiff = goalAngle - startAngle;
        if(pdiff > FastMath.PI) {
            pdiff -= FastMath.TWO_PI;
        }
        else if(pdiff < -FastMath.PI) {
            pdiff += FastMath.TWO_PI;
        }

        this.startAngle = startAngle;
        this.diff = pdiff;
        currentAngle = startAngle;
    }

    @Override
    public void internalAction(float dt) {
        if(!isDone()){
            currentAngle = startAngle + diff * ( getTime() / getLength() );
        }
        else{
            currentAngle = startAngle + diff;
        }
    }

}
