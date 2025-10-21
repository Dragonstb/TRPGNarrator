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

import com.jme3.math.Vector3f;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @since 0.0.2
 * @author Dragonstb
 */
public class Vector3DTOTest {

    private final Vector3f vec = new Vector3f(1, 2, 3);

    private Vector3DTO dto;

    @BeforeEach
    public void setUp() {
        dto = new Vector3DTO(vec);
    }

    @Test
    public void testFloatsConstructor() {
        dto = new Vector3DTO(vec.x, vec.y, vec.z);
        assertEquals(vec.x, dto.getX(), "Wrong x");
        assertEquals(vec.y, dto.getY(), "Wrong y");
        assertEquals(vec.z, dto.getZ(), "Wrong z");
    }

    @Test
    public void testGetAsVector() {
        Vector3f res = dto.getAsVector();
        assertNotNull(res, "no vector returned");
        assertEquals(vec, res, "unexxpected vector");
    }

    @Test
    public void testGetX() {
        float x = dto.getX();
        assertEquals(x, vec.x);
    }

    @Test
    public void testGetY() {
        float y = dto.getY();
        assertEquals(y, vec.y);
    }

    @Test
    public void testGetZ() {
        float z = dto.getZ();
        assertEquals(z, vec.z);
    }

    @Test
    public void testIllegalArg() {
        assertThrows(NullPointerException.class, () -> new Vector3DTO(null));
    }

    @Test
    public void testEquals_otherEqual() {
        Vector3DTO other = new Vector3DTO(vec);
        assertTrue(dto.equals(other));
    }

    @Test
    public void testEquals_otherNonEqual() {
        Vector3DTO other = new Vector3DTO(vec.x+1, vec.y+1, vec.z+1);
        assertFalse(dto.equals(other));
    }
}
