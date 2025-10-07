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
package dev.dragonstb.trpgnarrator.client.ingame.board;

import com.jme3.math.Vector3f;
import java.util.Optional;
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
public class BoardDataTest {

    private BoardData boardData;

    public BoardDataTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        boardData = new BoardData();
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetLocationOfField_Ok() {
        int id = 15;

        Optional<Vector3f> opt = boardData.getLocationOfField(id);
        assertTrue(opt.isPresent(), "Optional turned out to be empty.");

        // TODO: assert that vector is correct once the board is constructed based on a data model rather than hardcoded during the init.
    }

    @Test
    public void testGetLocationOfField_NoFieldWithThatId() {
        int id = -15;

        Optional<Vector3f> opt = boardData.getLocationOfField(id);
        assertTrue(opt.isEmpty(), "Optional turned out to be filled.");
    }

}
