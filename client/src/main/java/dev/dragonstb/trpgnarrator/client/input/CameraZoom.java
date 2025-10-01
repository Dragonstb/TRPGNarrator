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

package dev.dragonstb.trpgnarrator.client.input;

import dev.dragonstb.trpgnarrator.client.error.ClientErrorCodes;

/** Manages the status of a camera zoom. The zooming is realized by changing the distance of the camera to its target,
 * not by changing the field of view angle. The number of possible distances can be specified at construction. The
 * distances at each position follow a power law, with the gaps becoming larger when the camera distance increases.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public final class CameraZoom {

    /** Possible distances of the camera from the camera target, in WU. */
    private final float[] dists;
    /** Index of current camera distance in {@code dists}. */
    private byte curIdx;
    /** Index of camera distance we switch to in the next update loop. */
    private byte nextIdx;

    /** Generates.
     *
     * @param minDist Distance of camera when closest to target.
     * @param maxDist Distance of camera when furthest from target.
     * @param numDists Number of possible camera distances.
     * @throws IllegalArgumentException
     */
    public CameraZoom(float minDist, float maxDist, byte numDists) throws IllegalArgumentException {
        String errCode = ClientErrorCodes.C04278;
        if (numDists < 2) {
            String msg = "Mouse zoom requires at least two available distances, but got "+numDists+".";
            String codedMsg = ClientErrorCodes.assembleCodedMsg(msg, errCode);
            throw new IllegalArgumentException(codedMsg);
        }
        if (minDist < .001f) {
            String msg = "Minimum distance of mouse zoom needs to be well positive (above .001), but is "+minDist+".";
            String codedMsg = ClientErrorCodes.assembleCodedMsg(msg, errCode);
            throw new IllegalArgumentException(codedMsg);
        }

        dists = new float[numDists];

        double base = Math.pow(maxDist/minDist, 1d/(dists.length-1));
        for (int i = 0; i < dists.length; i++) {
            dists[i]=(float)(minDist*Math.pow(base, i));
        }
        curIdx = nextIdx = (byte)(dists.length/2);
    }

    /**
     * @since 0.0.1
     * @author Dragonstb
     * @return The current camera distance in WU.
     */
    public float getCurrentDist() {
        return dists[curIdx];
    }

    /** Changes the upcoming zoom distance index by the given number of steps
     * @since 0.0.1
     * @author Dragonstb
     * @param steps Number of steps.
     */
    public void changeZoomDistanceBy(int steps) {
        nextIdx -= (byte)(steps/120);
        // TODO: a utilty math class offering clamp(byte, byte, byte)
        if(nextIdx < 0){
            nextIdx = 0;
        }
        else if(nextIdx >= dists.length) {
            nextIdx = (byte)(dists.length-1);
        }
    }

    /** Says if significant changes have been made.
     * @since 0.0.1
     * @author Dragonstb
     * @return Needs update?
     */
    public boolean hasChanges() {
        return curIdx != nextIdx;
    }

    /** Changes the current index to the demanded one.
     * @author Dragonstb
     * @since 0.0.1
     * @return New camera distance.
     */
    public float updateDist() {
        curIdx = nextIdx;
        return getCurrentDist();
    }

}
