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

import com.jme3.input.event.MouseMotionEvent;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import dev.dragonstb.trpgnarrator.client.camera.CameraZoom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Testing integration of IngameCamControl and its supporting instances.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public class IngameCamControlIT {

    private Spatial target;
    private IngameCamControl control;
    private Camera cam;

    public IngameCamControlIT() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        cam = new Camera(1920, 1080);
        target = new Node("target");
        target.setLocalTranslation(0, 0, 0);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testDefaultWithoutErrors() {
        // Initializing with default values must not throw any exception
        assertDoesNotThrow( ()-> new IngameCamControl(cam) );
    }

    @Test
    public void testMoveToClosestDistance() {
        float minDist = 5;
        byte steps = 10;
        CameraZoom zoom = new CameraZoom(minDist, minDist*5, steps);
        control = new IngameCamControl(cam, zoom);
        target.addControl(control);

        // one tick of the mouse wheel increases deltaWheel by STEPSIZE. This has to be simulated here.
        int dWheel = steps * CameraZoom.STEPSIZE;
        MouseMotionEvent evt = new MouseMotionEvent(0, 0, 0, 0, 0, dWheel);
        control.onMouseMotionEvent(evt);
        control.update(0.1f);

        float actual = cam.getLocation().subtract(target.getLocalTranslation()).length();
        assertEquals(minDist, actual, .01f, "Camera not in closest position");
    }

    @Test
    public void testMoveToFarthestDistance() {
        float maxDist = 50;
        byte steps = 10;
        CameraZoom zoom = new CameraZoom(maxDist/5f, maxDist, steps);
        control = new IngameCamControl(cam, zoom);
        target.addControl(control);

        // one tick of the mouse wheel increases deltaWheel by STEPSIZE. This has to be simulated here.
        int dWheel = -steps * CameraZoom.STEPSIZE;
        MouseMotionEvent evt = new MouseMotionEvent(0, 0, 0, 0, 0, dWheel);
        control.onMouseMotionEvent(evt);
        control.update(0.1f);

        float actual = cam.getLocation().subtract(target.getLocalTranslation()).length();
        assertEquals(maxDist, actual, .01f, "Camera not in farthest position");
    }

}
