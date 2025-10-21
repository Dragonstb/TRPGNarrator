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
package dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 */
public class FigurineDTOTest {

    private final int id = 12345;
    private final ColorRGBA diffuse = new ColorRGBA(.1f, .2f, .3f, 1);
    private final ColorRGBA ambient = new ColorRGBA(.4f, .5f, .6f, 1);
    private final ColorRGBA specular = new ColorRGBA(.7f, .8f, .9f, 1);
    private final Vector3f loc = new Vector3f(-1, -2, -3);
    private final int fieldId = 20;

    private FigurineDTO dto;

    @BeforeEach
    public void setUp() {
        dto = new FigurineDTO(id, diffuse, ambient, specular, loc, fieldId);
    }

    @Test
    public void testGetDiffuseAsColor() {
        ColorRGBA col = dto.getDiffuseAsColor();
        assertNotNull(col, "no colour");
        assertEquals(diffuse, col, "Wrong colour");
    }

    @Test
    public void testGetAmbientAsColor() {
        ColorRGBA col = dto.getAmbientAsColor();
        assertNotNull(col, "no colour");
        assertEquals(ambient, col, "Wrong colour");
    }

    @Test
    public void testGetSpecularAsColor() {
        ColorRGBA col = dto.getSpecularAsColor();
        assertNotNull(col, "no colour");
        assertEquals(specular, col, "Wrong colour");
    }

    @Test
    public void testGetLocationAsVector() {
        Optional<Vector3f> opt = dto.getLocationAsVector();
        assertNotNull(opt, "no optional");
        assertTrue(opt.isPresent(), "No value in optional");

        Vector3f location = opt.get();
        assertEquals(loc, location, "Wrong location");
    }

    @Test
    public void testGetLocationAsVector_noLoc() {
        dto = new FigurineDTO(id, diffuse, ambient, specular, null, fieldId);
        Optional<Vector3f> opt = dto.getLocationAsVector();
        assertNotNull(opt, "no optional");
        assertTrue(opt.isEmpty(), "A value in optional");
    }

    @Test
    public void testGetId() {
        assertEquals(id, dto.getId());
    }

    @Test
    public void testGetDiffuse() {
        Vector3DTO expected = new Vector3DTO(diffuse.toVector3f());
        Vector3DTO colour = dto.getDiffuse();
        assertNotNull(colour, "No colour");
        assertEquals(expected, colour, "Wrong colour");
    }

    @Test
    public void testGetAmbient() {
        Vector3DTO expected = new Vector3DTO(ambient.toVector3f());
        Vector3DTO colour = dto.getAmbient();
        assertNotNull(colour, "No colour");
        assertEquals(expected, colour, "Wrong colour");
    }

    @Test
    public void testGetSpecular() {
        Vector3DTO expected = new Vector3DTO(specular.toVector3f());
        Vector3DTO colour = dto.getSpecular();
        assertNotNull(colour, "No colour");
        assertEquals(expected, colour, "Wrong colour");
    }

    @Test
    public void testGetLocation() {
        Vector3DTO expected = new Vector3DTO(loc);
        Vector3DTO location = dto.getLocation();
        assertNotNull(location, "No location");
        assertEquals(expected, location, "Wrong location");
    }

    @Test
    public void testGetFieldId() {
        assertEquals(fieldId, dto.getFieldId());
    }

    @Test
    public void testIllegalArgs_diffuse() {
        assertThrows(NullPointerException.class, () -> new FigurineDTO(id, null, ambient, specular, loc, fieldId));
    }

    @Test
    public void testIllegalArgs_ambient() {
        assertThrows(NullPointerException.class, () -> new FigurineDTO(id, diffuse, null, specular, loc, fieldId));
    }

    @Test
    public void testIllegalArgs_specular() {
        assertThrows(NullPointerException.class, () -> new FigurineDTO(id, diffuse, ambient, null, loc, fieldId));
    }

    @Test
    public void testEquals_otherEqual() {
        FigurineDTO other = new FigurineDTO(id, diffuse, ambient, specular, loc, fieldId);
        assertTrue(dto.equals(other));
    }

    @Test
    public void testEquals_otherNonEqual() {
        FigurineDTO other = new FigurineDTO(id+1, ambient, specular, diffuse, loc, fieldId+1);
        assertFalse(dto.equals(other));
    }

}
