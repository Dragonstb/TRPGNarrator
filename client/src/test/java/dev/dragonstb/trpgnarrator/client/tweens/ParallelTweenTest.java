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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 */
public class ParallelTweenTest {

    private final float delta = .0001f;
    private final Vector3f start1 = new Vector3f(0, 0, 0);
    private final Vector3f goal1 = new Vector3f(1, 1, 1);
    private final Vector3f start2 = new Vector3f(10, 10, 10);
    private final Vector3f goal2 = new Vector3f(13, 13, 13);
    private final float length1 = 1;
    private final float length2 = 3;
    private ShiftTween shiftTween1;
    private ShiftTween shiftTween2;
    private ParallelTween pTween;

    @BeforeEach
    public void setUp() {
        shiftTween1 = new ShiftTween(start1, goal1, length1);
        shiftTween2 = new ShiftTween(start2, goal2, length2);
        pTween = new ParallelTween(shiftTween1, shiftTween2);
    }

    @Test
    public void testGetMaximumLength() {
        assertEquals(length2, pTween.getLength(), delta);
    }

    @Test
    public void testProgress() {
        float actual1, actual2;

        pTween.progress(.5f);
        actual1 = new Vector3f(.5f, .5f, .5f).distance(shiftTween1.getCurrentPos());
        actual2 = new Vector3f(10.5f, 10.5f, 10.5f).distance(shiftTween2.getCurrentPos());
        assertEquals(0, actual1, delta, "shift tween 1 in unexpected position at t = .5");
        assertEquals(0, actual2, delta, "shift tween 2 in unexpected position at t = .5");
        assertFalse(shiftTween1.isDone(), "Shift tween 1 already done at t = 0.5");
        assertFalse(shiftTween2.isDone(), "Shift tween 2 already done at t = 0.5");
        assertFalse(pTween.isDone(), "parallel tween already done at t = 0.5");

        pTween.progress(.5f);
        actual1 = goal1.distance(shiftTween1.getCurrentPos());
        actual2 = new Vector3f(11f, 11f, 11f).distance(shiftTween2.getCurrentPos());
        assertEquals(0, actual1, delta, "shift tween 1 in unexpected position at t = 1");
        assertEquals(0, actual2, delta, "shift tween 2 in unexpected position at t = 1");
        assertTrue(shiftTween1.isDone(), "Shift tween 1 still not done at t = 1");
        assertFalse(shiftTween2.isDone(), "Shift tween 2 already done at t = 1");
        assertFalse(pTween.isDone(), "parallel tween already done at t = 1");

        pTween.progress(1f);
        actual1 = goal1.distance(shiftTween1.getCurrentPos());
        actual2 = new Vector3f(12f, 12f, 12f).distance(shiftTween2.getCurrentPos());
        assertEquals(0, actual1, delta, "shift tween 1 in unexpected position at t = 2");
        assertEquals(0, actual2, delta, "shift tween 2 in unexpected position at t = 2");
        assertTrue(shiftTween1.isDone(), "Shift tween 1 still not done at t = 2");
        assertFalse(shiftTween2.isDone(), "Shift tween 2 already done at t = 2");
        assertFalse(pTween.isDone(), "parallel tween already done at t = 2");

        pTween.progress(1f);
        actual1 = goal1.distance(shiftTween1.getCurrentPos());
        actual2 = goal2.distance(shiftTween2.getCurrentPos());
        assertEquals(0, actual1, delta, "shift tween 1 in unexpected position at t = 3");
        assertEquals(0, actual2, delta, "shift tween 2 in unexpected position at t = 3");
        assertTrue(shiftTween1.isDone(), "Shift tween 1 still not done at t = 3");
        assertTrue(shiftTween2.isDone(), "Shift tween 2 still not done at t = 3");
        assertTrue(pTween.isDone(), "parallel tween still not done at t = 3");

    }

    @Test
    public void testProgressBeyondLength() {
        float actual1, actual2;

        pTween.progress(pTween.getLength()+1);
        actual1 = goal1.distance(shiftTween1.getCurrentPos());
        actual2 = goal2.distance(shiftTween2.getCurrentPos());
        assertEquals(0, actual1, delta, "shift tween 1 in unexpected position");
        assertEquals(0, actual2, delta, "shift tween 2 in unexpected position");
        assertTrue(shiftTween1.isDone(), "Shift tween 1 still not done");
        assertTrue(shiftTween2.isDone(), "Shift tween 2 still not done");
    }

    @Test
    public void testProgressToATimeBeforeTime() {
        float actual1, actual2;

        pTween.progress(-1f);
        actual1 = start1.distance(shiftTween1.getCurrentPos());
        actual2 = start2.distance(shiftTween2.getCurrentPos());
        assertEquals(0, actual1, delta, "shift tween 1 in unexpected position");
        assertEquals(0, actual2, delta, "shift tween 2 in unexpected position");
        assertFalse(shiftTween1.isDone(), "Shift tween 1 already done");
        assertFalse(shiftTween2.isDone(), "Shift tween 2 already done");
    }

    @Test
    public void testNoTweens() {
        assertThrows(IllegalArgumentException.class, () -> new ParallelTween(new ActionTween[]{}));
    }

    @Test
    public void testNullArg() {
        ActionTween tween = null;
        assertThrows(NullPointerException.class, () -> new ParallelTween(tween));
    }
}
