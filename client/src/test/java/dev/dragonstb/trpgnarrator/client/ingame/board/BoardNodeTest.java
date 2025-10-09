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
import com.jme3.scene.Geometry;
import dev.dragonstb.trpgnarrator.client.Globals;
import dev.dragonstb.trpgnarrator.testslices.WithAssetManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Dragonstb
 */
@ExtendWith(MockitoExtension.class)
public class BoardNodeTest {

    private FieldData fieldA;
    private FieldData fieldB;
    private Map<Integer, FieldData> map;

    @Mock
    private BoardData boardData;

    private BoardNode boardNode;

    @BeforeAll
    public static void setUpClass() {
        WithAssetManager.initAssetManager();
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        fieldA = new FieldData(1, new Vector3f(1, 2, 3));
        fieldB = new FieldData(2, new Vector3f(-3, -2, -1));
        map = new HashMap<>();
        map.put(fieldA.getId(), fieldA);
        map.put(fieldB.getId(), fieldB);
        when(boardData.getFields()).thenReturn(map);

        boardNode = new BoardNode(boardData);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetFieldGeometry_ok() {
        int id = fieldA.getId();
        Optional<Geometry> opt = boardNode.getFieldGeometry(id);

        assertNotNull(opt, "No optional in da house");
        assertTrue(opt.isPresent(), "No value present");
        int actual = opt.get().getUserData(Globals.FIELD_ID);
        assertEquals(id, actual, "Wrong id");
    }

    @Test
    public void testGetFieldGeometry_not_ok() {
        Optional<Geometry> opt = boardNode.getFieldGeometry(-3); // any invalid id

        assertNotNull(opt, "No optional in da house");
        assertTrue(opt.isEmpty(), "Unexpected value");
    }

    @Test
    public void testGetCurrentlyHighlightedFieldId_ok() {
        int id = fieldA.getId();
        Optional<Geometry> opt = boardNode.getFieldGeometry(id);
        if(opt.isEmpty()) {
            fail("no geometry");
            return;
        }

        Geometry geom = opt.get();
        boardNode.highlightField(geom);

        Optional<Integer> idOpt = boardNode.getCurrentlyHighlightedFieldId();

        assertNotNull(idOpt);
        assertTrue(idOpt.isPresent());
        int actual = opt.get().getUserData(Globals.FIELD_ID);
        assertEquals(id, actual, "Wrong id");
    }

    @Test
    public void testGetCurrentlyHighlightedFieldId_not_ok() {
        Optional<Integer> idOpt = boardNode.getCurrentlyHighlightedFieldId();

        assertNotNull(idOpt, "No opt");
        assertTrue(idOpt.isEmpty(), "present");
    }

}
