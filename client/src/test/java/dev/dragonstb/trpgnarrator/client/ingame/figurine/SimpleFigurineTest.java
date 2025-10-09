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
package dev.dragonstb.trpgnarrator.client.ingame.figurine;

import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import dev.dragonstb.trpgnarrator.client.Globals;
import dev.dragonstb.trpgnarrator.testslices.WithAssetManager;
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
public class SimpleFigurineTest {

    @BeforeAll
    public static void setUpClass() {
        WithAssetManager.initAssetManager();
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testInit() {
        ColorRGBA col = new ColorRGBA(.5f, .5f, .5f, 1f);
        String id = "oops";
        SimpleFigurine fig = new SimpleFigurine(id, col);
        Node node = fig.getNode();

        assertEquals(Globals.FIGURINE_NODE_NAME+id, node.getName(), "invalid name of figurine root node.");
        assertFalse(node.getChildren().isEmpty(), "No children at figurine root node.");

        Geometry geom = (Geometry)node.getChild(0);
        Material mat = geom.getMaterial();
        String expected = "Vector4 Diffuse : "+String.valueOf(col.r)+" "+String.valueOf(col.g)
                +" "+String.valueOf(col.b)+" "+String.valueOf(col.a);
        MatParam expectedParam = mat.getParam("Diffuse");
        assertNotNull(expectedParam, "Diffuse not set.");
        assertEquals(expected, expectedParam.toString());
    }

    @Test
    public void testSetLocalTranslation() {
        ColorRGBA col = new ColorRGBA(.5f, .5f, .5f, 1f);
        String id = "oops";
        SimpleFigurine fig = new SimpleFigurine(id, col);
        Node node = fig.getNode();

        Vector3f newPos = new Vector3f(1, 2, 3);
        fig.setLocalTranslation(newPos);
        assertEquals(newPos, node.getLocalTranslation(), "First translation check has failed");

        newPos = new Vector3f(-3, -2, -1);
        fig.setLocalTranslation(newPos);
        assertEquals(newPos, node.getLocalTranslation(), "Second translation check has failed");

    }

    @Test
    public void testGetCurrentFieldId_onboard() {
        Node node = new Node();
        SimpleFigurine fig = new SimpleFigurine("test", ColorRGBA.Blue);
        node.attachChild(fig.getNode());
        int fieldId = 123;

        fig.setCurrentFieldId(fieldId);
        Optional<Integer> opt = fig.getCurrentFieldId();

        assertNotNull(opt, "no optional at all");
        assertTrue(opt.isPresent(), "no value present");
        assertEquals(fieldId, opt.get(), "wrong value");
    }

    @Test
    public void testGetCurrentFieldId_offboard() {
        SimpleFigurine fig = new SimpleFigurine("test", ColorRGBA.Blue);
        int fieldId = 123;

        fig.setCurrentFieldId(fieldId);
        Optional<Integer> opt = fig.getCurrentFieldId();

        assertNotNull(opt, "no optional at all");
        assertTrue(opt.isEmpty(), "not empty");
    }
}
