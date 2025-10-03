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

package dev.dragonstb.trpgnarrator.client.ingame;

import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import dev.dragonstb.trpgnarrator.client.Globals;
import dev.dragonstb.trpgnarrator.client.input.CameraZoom;
import lombok.NonNull;

/**
 *
 * @author Dragonstb
 * @since
 */
public class IngameCamControl extends AbstractControl implements RawInputListener{

    /** The camera controlled by this control. */
    @NonNull private final Camera cam;

    /** Vector from observed object to camera position, in WU and World Space. */
    private final Vector3f shift = Vector3f.UNIT_XYZ.mult(10f);
    /** Camera zoom status and capabilities. */
    private final CameraZoom zoom;

    /** Generates with default values {@code camMinDist = 0, camMaxDist = 50, steps = 20} for the zoom.
     * @since 0.0.1
     * @author Dragonstb
     * @param cam Camera controlled by this control.
     */
    public IngameCamControl(@NonNull Camera cam) {
        this(cam, new CameraZoom(2, 50, (byte)20));
    }

    /** Generates
     * @since 0.0.1
     * @author Dragonstb
     * @param cam Camera controlled by this control.
     * @param zoom Control element for zooming.
     * @throws IllegalArgumentException I
     */
    public IngameCamControl(@NonNull Camera cam, @NonNull CameraZoom zoom) {
        this.cam = cam;
        this.zoom = zoom;
        shift.normalizeLocal().multLocal(zoom.getCurrentDist());
        updateCamLocation();
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        updateCamLocation();
    }

    @Override
    protected void controlUpdate(float tpf) {
        updateCamLocation();
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    private void updateCamLocation() {
        if(zoom.hasChanges()) {
            float dist = zoom.updateDist();
            shift.normalizeLocal().multLocal(dist);
        }

        if(getSpatial() != null ){
            Vector3f targetPos = getSpatial().getLocalTranslation();
            Vector3f newPos = targetPos.add(shift);
            cam.setLocation(newPos);
            cam.lookAt(targetPos, Globals.WORLD_UP);
        }
    }

    @Override
    public void beginInput() {

    }

    @Override
    public void endInput() {

    }

    @Override
    public void onJoyAxisEvent(JoyAxisEvent evt) {

    }

    @Override
    public void onJoyButtonEvent(JoyButtonEvent evt) {

    }

    @Override
    public void onMouseMotionEvent(MouseMotionEvent evt) {
        if (evt.getDeltaWheel() != 0) {
            zoom.changeNextZoomDistanceBy(evt.getDeltaWheel());
        }
    }

    @Override
    public void onMouseButtonEvent(MouseButtonEvent evt) {

    }

    @Override
    public void onKeyEvent(KeyInputEvent evt) {

    }

    @Override
    public void onTouchEvent(TouchEvent evt) {

    }

}
