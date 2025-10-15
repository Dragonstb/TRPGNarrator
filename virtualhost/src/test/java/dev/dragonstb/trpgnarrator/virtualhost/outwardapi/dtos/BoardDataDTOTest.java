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

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Dragonstb
 */
@ExtendWith(MockitoExtension.class)
public class BoardDataDTOTest {

    private final int id1 = 1;
    private final int id2 = 2;

    @Mock
    private FieldDataDTO field1;

    @Mock
    private FieldDataDTO field2;

    @Mock
    private FieldLinkDTO link;

    private BoardDataDTO board;

    @BeforeEach
    public void setUp() {
        List<FieldDataDTO> fields = new ArrayList<>();
        fields.add(field1);
        fields.add(field2);

        List<FieldLinkDTO> links = new ArrayList<>();
        links.add(link);

        board = new BoardDataDTO(fields, links);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetFields() {
        List<FieldDataDTO> list = board.getFields();
        assertNotNull(list, "no list");
        assertEquals(2, list.size(), "wrong size");
        assertTrue(list.contains(field1), "field 1 missing");
        assertTrue(list.contains(field2), "field 2 missing");
    }

    @Test
    public void testGetLinks() {
        List<FieldLinkDTO> list = board.getLinks();
        assertNotNull(list, "no list");
        assertEquals(1, list.size(), "wrong size");
        assertTrue(list.contains(link), "missing link");
    }

    @Test
    public void testImmutability() {
        List<FieldDataDTO> list = board.getFields();
        FieldDataDTO bfdto = new FieldDataDTO(5, 0, 0, 0);
        assertThrows(UnsupportedOperationException.class, () -> list.add(bfdto));
        assertThrows(UnsupportedOperationException.class, () -> list.removeLast());

        List<FieldLinkDTO> listLinks = board.getLinks();
        FieldLinkDTO fldto = new FieldLinkDTO(8, 9);
        assertThrows(UnsupportedOperationException.class, () -> listLinks.add(fldto));
        assertThrows(UnsupportedOperationException.class, () -> listLinks.removeLast());
    }

}
