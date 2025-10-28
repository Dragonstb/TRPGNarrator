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

import com.jme3.math.FastMath;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 */
public class RotateAngleTweenTest {

    private final float length = 1.206f;
    private RotateAngleTween tween;

    @Test
    public void testRotation_EasySetup_Counterclockwise() {
        // goal angle is larger than start angle
        float goal = 3*FastMath.QUARTER_PI;
        float start = FastMath.QUARTER_PI;
        tween = new RotateAngleTween(start, goal, length);
        float diff = .0001f;

        assertEquals(start, tween.getCurrentAngle(), diff, "Wrong angle at t = 0");
        assertFalse(tween.isDone(), "Done already at t = 0");

        tween.progress(.5f*length);
        assertEquals(FastMath.HALF_PI, tween.getCurrentAngle(), diff, "Wrong angle at t = 1/2 of length");
        assertFalse(tween.isDone(), "Done already at t = 1/2 of length");

        tween.progress(.5f*length);
        assertEquals(goal, tween.getCurrentAngle(), diff, "Wrong angle at full length");
        assertTrue(tween.isDone(), "Still not done at full length");

        tween.progress(.5f*length);
        assertEquals(goal, tween.getCurrentAngle(), diff, "Wrong angle beyond full length");
        assertTrue(tween.isDone(), "Still not done beyond full length");
    }

    @Test
    public void testRotation_JumpSetup_Counterclockwise() {
        // goal angle is larger than start angle, but start angle is below zero
        float goal = FastMath.QUARTER_PI;
        float start = -FastMath.QUARTER_PI;
        tween = new RotateAngleTween(start, goal, length);
        float diff = .0001f;

        assertEquals(start, tween.getCurrentAngle(), diff, "Wrong angle at t = 0");
        assertFalse(tween.isDone(), "Done already at t = 0");

        tween.progress(.5f*length);
        assertEquals(0, tween.getCurrentAngle(), diff, "Wrong angle at t = 1/2 of length");
        assertFalse(tween.isDone(), "Done already at t = 1/2 of length");

        tween.progress(.5f*length);
        assertEquals(goal, tween.getCurrentAngle(), diff, "Wrong angle at full length");
        assertTrue(tween.isDone(), "Still not done at full length");

        tween.progress(.5f*length);
        assertEquals(goal, tween.getCurrentAngle(), diff, "Wrong angle beyond full length");
        assertTrue(tween.isDone(), "Still not done beyond full length");
    }

    @Test
    public void testRotation_InvertedSetup_Counterclockwise() {
        // goal angle is lower than start angle
        float goal = FastMath.QUARTER_PI;
        float start = 3*FastMath.QUARTER_PI;
        tween = new RotateAngleTween(start, goal, length);
        float diff = .0001f;

        assertEquals(start, tween.getCurrentAngle(), diff, "Wrong angle at t = 0");
        assertFalse(tween.isDone(), "Done already at t = 0");

        tween.progress(.5f*length);
        assertEquals(FastMath.HALF_PI, tween.getCurrentAngle(), diff, "Wrong angle at t = 1/2 of length");
        assertFalse(tween.isDone(), "Done already at t = 1/2 of length");

        tween.progress(.5f*length);
        assertEquals(goal, tween.getCurrentAngle(), diff, "Wrong angle at full length");
        assertTrue(tween.isDone(), "Still not done at full length");

        tween.progress(.5f*length);
        assertEquals(goal, tween.getCurrentAngle(), diff, "Wrong angle beyond full length");
        assertTrue(tween.isDone(), "Still not done beyond full length");
    }

    @Test
    public void testRotation_toNegativeTime() {
        float goal = 3*FastMath.QUARTER_PI;
        float start = FastMath.QUARTER_PI;
        tween = new RotateAngleTween(start, goal, length);
        float diff = .0001f;

        tween.progress(-1f);
        assertEquals(start, tween.getCurrentAngle(), diff, "Wrong angle at t = -1");
        assertFalse(tween.isDone(), "Done already at t = -1");
    }

    @Test
    public void testPosInfiniteStartAngle() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class,
                () -> new RotateAngleTween(Float.POSITIVE_INFINITY, 1, length));
        assertTrue(exc.getMessage().contains("!!!!!"), "does not contain error code");
        assertTrue(exc.getMessage().contains("start"), "does not contain start");
    }

    @Test
    public void testNegInfiniteStartAngle() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class,
                () -> new RotateAngleTween(Float.NEGATIVE_INFINITY, 1, length));
        assertTrue(exc.getMessage().contains("!!!!!"), "does not contain error code");
        assertTrue(exc.getMessage().contains("start"), "does not contain start");
    }

    @Test
    public void testNanStartAngle() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class,
                () -> new RotateAngleTween(Float.NaN, 1, length));
        assertTrue(exc.getMessage().contains("!!!!!"), "does not contain error code");
        assertTrue(exc.getMessage().contains("start"), "does not contain start");
    }

    @Test
    public void testPosInfiniteGoalAngle() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class,
                () -> new RotateAngleTween(1, Float.POSITIVE_INFINITY, length));
        assertTrue(exc.getMessage().contains("!!!!!"), "does not contain error code");
        assertTrue(exc.getMessage().contains("goal"), "does not contain goal");
    }

    @Test
    public void testNegInfiniteGoalAngle() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class,
                () -> new RotateAngleTween(1, Float.NEGATIVE_INFINITY, length));
        assertTrue(exc.getMessage().contains("!!!!!"), "does not contain error code");
        assertTrue(exc.getMessage().contains("goal"), "does not contain goal");
    }

    @Test
    public void testNanGoalAngle() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class,
                () -> new RotateAngleTween(1, Float.NaN, length));
        assertTrue(exc.getMessage().contains("!!!!!"), "does not contain error code");
        assertTrue(exc.getMessage().contains("goal"), "does not contain goal");
    }

    @Test
    public void testPosInfiniteLength() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class,
                () -> new RotateAngleTween(1, 1, Float.POSITIVE_INFINITY));
        assertTrue(exc.getMessage().contains("?????"), "does not contain error code");
    }

    @Test
    public void testNegInfiniteLength() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class,
                () -> new RotateAngleTween(1, 1, Float.NEGATIVE_INFINITY));
        assertTrue(exc.getMessage().contains("?????"), "does not contain error code");
    }

    @Test
    public void testNanLength() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class,
                () -> new RotateAngleTween(1, 1, Float.NaN));
        assertTrue(exc.getMessage().contains("?????"), "does not contain error code");
    }

}
