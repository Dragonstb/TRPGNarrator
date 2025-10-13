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

import com.jme3.math.Vector3f;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 */
public class ShiftTweenTest {

    private final float length = 2.491f;
    private ShiftTween tween;
    private final Vector3f start = new Vector3f(3, 2, 1);
    private final Vector3f goal = new Vector3f(-3, -4, 7);

    @BeforeEach
    public void setUp() {
        tween = new ShiftTween(start, goal, length);
    }

    @Test
    public void testShifting() {
        float delta = .0001f; // a numerical allowance
        float aThird = length / 3f;
        float aSixth = length / 6f;
        float aHalf = length / 2f;
        Vector3f cur = tween.getCurrentPos();

        float diff = cur.distance(start);
        assertEquals(0, diff, delta, "Too much difference at t = 0");
        assertFalse(tween.isDone(), "Already done at t = 0");

        Vector3f expected = new Vector3f(1, 0, 3);
        tween.progress(aThird);
        cur = tween.getCurrentPos();
        diff = cur.distance(expected);
        assertEquals(0, diff, delta, "Too much difference at t = 1/3 of length");
        assertFalse(tween.isDone(), "Already done at t = 1/3 of length");

        expected = new Vector3f(-0, -1, 4);
        tween.progress(aSixth);
        cur = tween.getCurrentPos();
        diff = cur.distance(expected);
        assertEquals(0, diff, delta, "Too much difference at t = 1/2 of length");
        assertFalse(tween.isDone(), "Already done at t = 1/2 of length");

        tween.progress(aHalf);
        cur = tween.getCurrentPos();
        diff = cur.distance(goal);
        assertEquals(0, diff, delta, "Too much difference at full of length");
        assertTrue(tween.isDone(), "Still not done at full of length");

        tween.progress(aHalf);
        cur = tween.getCurrentPos();
        diff = cur.distance(goal);
        assertEquals(0, diff, delta, "Too much difference beyond length");
        assertTrue(tween.isDone(), "Still not done beyond length");
    }

    @Test
    public void testGetCurrentPos() {
        Vector3f cur = tween.getCurrentPos();
        assertEquals(start, cur);
    }

    @Test
    public void testNoStart() {
        assertThrows(NullPointerException.class, () -> new ShiftTween(null, goal, length));
    }

    @Test
    public void testNoGoal() {
        assertThrows(NullPointerException.class, () -> new ShiftTween(start, null, length));
    }

    @Test
    public void testNegativeLength() {
        assertThrows(IllegalArgumentException.class, () -> new ShiftTween(start, goal, -5f));
    }

    @Test
    public void testNegativeInfinityLength() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class, () -> new ShiftTween(start, goal, Float.NEGATIVE_INFINITY));
        assertTrue(exc.getMessage().contains("?????"));
    }

    @Test
    public void testPositiveInfinityLength() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class, () -> new ShiftTween(start, goal, Float.POSITIVE_INFINITY));
        assertTrue(exc.getMessage().contains("?????"));
    }

    @Test
    public void testNanLength() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class, () -> new ShiftTween(start, goal, Float.NaN));
        assertTrue(exc.getMessage().contains("?????"));
    }

    @Test
    public void testNanStep() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class, () -> tween.progress(Float.NaN));
        assertTrue(exc.getMessage().contains("!!!!!"));
    }

    @Test
    public void testPosInifiteStep() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class, () -> tween.progress(Float.POSITIVE_INFINITY));
        assertTrue(exc.getMessage().contains("!!!!!"));
    }

    @Test
    public void testNegInifiteStep() {
        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class, () -> tween.progress(Float.NEGATIVE_INFINITY));
        assertTrue(exc.getMessage().contains("!!!!!"));
    }

    @Test
    public void testStepToNegativeTime() {
        tween.progress(-5f);
        Vector3f cur = tween.getCurrentPos();
        assertEquals(start, cur);
    }

}
