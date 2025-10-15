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
package dev.dragonstb.trpgnarrator.virtualhost.board;

import com.jme3.math.Vector3f;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FieldDataDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 */
public class FieldDataTest {

    private final Vector3f loc = new Vector3f(1, 2, 3);
    private final int id = -6723488;

    private FieldData field;

    @BeforeEach
    public void setUp() {
        field = new FieldData(id, loc);
    }

//    @Test
    public void testAddLink() {
        // TBD
    }

    @Test
    public void testAsDTO() {
        FieldDataDTO dto = field.asDTO();
        assertNotNull(dto, "no dto");
        assertEquals(id, dto.getId(), "wrong id");
        assertEquals(loc, dto.getLocationAsVector(), "wrong location");
    }

    @Test
    public void testGetId() {
        assertEquals(id, field.getId());
    }

    @Test
    public void testGetLocation() {
        assertEquals(loc, field.getLocation());
    }

//    @Test
    public void testGetLinks() {
        // TBD
    }

}
