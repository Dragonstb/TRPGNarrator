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

import dev.dragonstb.trpgnarrator.client.camera.CameraZoom;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 * @since 0.0.1;
 */
public class CameraZoomTest {

    private final float minDist = 1;
    private final float maxDist = 2;
    private final byte steps = 2;
    private CameraZoom zoom;

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        zoom = new CameraZoom(minDist, maxDist, steps);
    }

    @Test
    public void testInvalidStepNuumber() {
        assertThrows(IllegalArgumentException.class, () -> new CameraZoom(minDist, maxDist, (byte)1));
    }

    @Test
    public void testInvalidMinDist() {
        assertThrows(IllegalArgumentException.class, () -> new CameraZoom(CameraZoom.MINDIST/2f, maxDist, steps));
    }

    @Test
    public void testChangeZoomDistanceByTooFarBelow() {
        int stepBy = (2*steps+5) * CameraZoom.STEPSIZE;
        zoom.changeNextZoomDistanceBy(stepBy);
        float actual = zoom.updateDist();
        assertEquals(minDist, actual, .01f);
    }

    @Test
    public void testChangeZoomDistanceByTooFarAbove() {
        int stepBy = (2*steps+5) * CameraZoom.STEPSIZE;
        zoom.changeNextZoomDistanceBy(-stepBy);
        float actual = zoom.updateDist();
        assertEquals(maxDist, actual, .01f);
    }

}
