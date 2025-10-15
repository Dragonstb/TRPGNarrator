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

import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FieldLinkDTO;
import org.junit.jupiter.api.BeforeEach;
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
public class FieldLinkTest {

    private final int idA = 1;
    private final int idB = 2;

    @Mock
    private FieldData fieldA;

    @Mock
    private FieldData fieldB;

    private FieldLink link;

    @BeforeEach
    public void setUp() {
        link = new FieldLink(fieldA, fieldB);
    }

    @Test
    public void testAsDTO() {
        when(fieldA.getId()).thenReturn(idA);
        when(fieldB.getId()).thenReturn(idB);

        FieldLinkDTO dto = link.asDTO();
        assertEquals(idA, dto.getIdFieldA(), "wrong id of field A");
        assertEquals(idB, dto.getIdFieldB(), "wrong id of field B");
    }

    @Test
    public void testGetFieldA() {
        FieldData field = link.getFieldA();
        assertTrue(field == fieldA);
    }

    @Test
    public void testGetFieldB() {
        FieldData field = link.getFieldB();
        assertTrue(field == fieldB);
    }

    @Test
    public void testNullArgs() {
        assertThrows(NullPointerException.class, () -> new FieldLink(null, fieldB), "No exception when field A is null");
        assertThrows(NullPointerException.class, () -> new FieldLink(fieldA, null), "No exception when field B is null");
        assertThrows(NullPointerException.class, () -> new FieldLink(null, null), "No exception when bot fields are null");
    }

}
