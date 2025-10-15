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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 */
public class FieldLinkDTOTest {

    private final int idA = 1;
    private final int idB = 2;

    private FieldLinkDTO dto;

    public FieldLinkDTOTest() {
    }

    @BeforeEach
    public void setUp() {
        dto = new FieldLinkDTO(idA, idB);
    }

    @Test
    public void testGetIdFieldA() {
        assertEquals(idA, dto.getIdFieldA());
    }

    @Test
    public void testGetIdFieldB() {
        assertEquals(idB, dto.getIdFieldB());
    }

    @Test
    public void testEquals_Null() {
        assertFalse(dto.equals(null));
    }

    @Test
    public void testEquals_OtherClass() {
        assertFalse(dto.equals("Hello"));
    }

    @Test
    public void testEquals_Match() {
        FieldLinkDTO dto2 = new FieldLinkDTO(idA, idB);
        assertTrue(dto.equals(dto2));
    }

    @Test
    public void testEquals_MismatchIdFieldA() {
        FieldLinkDTO dto2 = new FieldLinkDTO(idA+1, idB);
        assertFalse(dto.equals(dto2));
    }

    @Test
    public void testEquals_MismatchIdFieldB() {
        FieldLinkDTO dto2 = new FieldLinkDTO(idA, idB+1);
        assertFalse(dto.equals(dto2));
    }

    @Test
    public void testEquals_MismatchAllFields() {
        FieldLinkDTO dto2 = new FieldLinkDTO(idA+1, idB+1);
        assertFalse(dto.equals(dto2));
    }

}
