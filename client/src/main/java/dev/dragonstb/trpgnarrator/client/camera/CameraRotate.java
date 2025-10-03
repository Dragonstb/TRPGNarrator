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

package dev.dragonstb.trpgnarrator.client.camera;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import dev.dragonstb.trpgnarrator.client.error.ClientErrorCodes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Manages the rotation of the camera around a target. This management is based on two spherical angles. By limiting
 * the latitude (avoiding the poles) the orientation keeps obvious all the time.
 *
 *
 * @author Dragonstb
 * @since 0.0.1;
 */
public class CameraRotate {

    /** Maximum altitude allowed globally. */
    public static final float MAX_LAT = .99f * FastMath.HALF_PI;
    /** Minimum altitude allowed globally. */
    public static final float MIN_LAT = -.99f * FastMath.HALF_PI;

    /** Longitude angle, in rad. Z axis is at 0 and x at PI/4. The angle is unlimited. */
    private float longitude = FastMath.QUARTER_PI;
    /** Latitude angle, in rad. X-Z-plane is at 0, World up direction at +PI/2. The angle is constrained. */
    private float latitude = FastMath.QUARTER_PI;
    /** Maximum latitude for this instance. Must be limited by the global limit. */
    private float maxLat;
    /** Minimum latitude for this instance. Must be limited by the global limit. */
    private float minLat;
    /** Flag if rotation inputs are to be processed. While {@code false}, the angles won't be changed. */
    private boolean rotate = false;
    /** Time stamp of the last change due to input. Used for determining if changes have occured since the last update.
     * As this value is not synced, you could be unlucky, but this should be not so dramatically with cam control. */
    private long lastChange = -1;
    /** Time stamp of the last update. Used for determining if changes have occured since the last update.
     * As this value is not synced, you could be unlucky, but this should be not so dramatically with cam control. */
    private long lastUpdate = -1;
    /** Unit vector of direction. */
    private final Vector3f currentDirection;

    public CameraRotate() {
        this(MIN_LAT, MAX_LAT);
    }

    public CameraRotate(float minLat, float maxLat) {
        String errCode = ClientErrorCodes.C28331;
        if(minLat < MIN_LAT) {
            String msg = "Minimum altitude must not be below constant MIN_LAT.";
            String codedMsg = ClientErrorCodes.assembleCodedMsg(msg, errCode);
            throw new IllegalArgumentException(codedMsg);
        }
        if(maxLat > MAX_LAT) {
            String msg = "Maximum altitude must not be above constant MAX_LAT.";
            String codedMsg = ClientErrorCodes.assembleCodedMsg(msg, errCode);
            throw new IllegalArgumentException(codedMsg);
        }
        if(minLat >= maxLat) {
            String msg = "Minimum altitude must be below maximum altitude.";
            String codedMsg = ClientErrorCodes.assembleCodedMsg(msg, errCode);
            throw new IllegalArgumentException(codedMsg);
        }

        this.maxLat = maxLat;
        this.minLat = minLat;
        currentDirection = new Vector3f();
        updateComponents();
    }



    public Vector3f getCurrentDirection() {
        return currentDirection;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }


    /** Changes the angles of the camera position by the given values. The resulting latitude is clamped into the
     * the allowed interval.
     *
     * @since 0.0.1
     * @author Dragonstb
     * @param dLong Chnage of longitude, in rad.
     * @param dLat Change of latitude, in rad.
     */
    public void rotateBy(float dLong, float dLat) {
        if(!rotate) {
            return;
        }

        longitude += dLong;
        if(longitude > FastMath.TWO_PI) {
            longitude -= FastMath.TWO_PI;
        }
        else if(longitude < 0) {
            longitude += FastMath.TWO_PI;
        }

        latitude += dLat;
        latitude = FastMath.clamp(latitude, minLat, maxLat);

        lastChange = System.currentTimeMillis();
    }

    /** Says if changes have occured sinc ethe last update.
     *
     * @author Dragonstb
     * @since 0.0.1
     * @return Has something changed?
     */
    public boolean hasChanges() {
        return lastChange != lastUpdate;
    }

    /** Updates the direction to the current amgles.
     *
     * @author Dragonstb
     * @since 0.0.1
     * @return Updated direction.
     */
    public Vector3f updateDirection() {
        lastUpdate = lastChange;
        updateComponents();
        return currentDirection;
    }

    /** Updates the components of the direction to the current angles.
     * @since 0.0.1
     * @author Dragonstb
     */
    private void updateComponents() {
        float coslat = FastMath.cos(latitude);
        float sinlat = FastMath.sin(latitude);
        float sinlong = FastMath.sin(longitude);
        float coslong = FastMath.cos(longitude);
        currentDirection.set(coslat*sinlong, sinlat, coslat*coslong);
    }

}
