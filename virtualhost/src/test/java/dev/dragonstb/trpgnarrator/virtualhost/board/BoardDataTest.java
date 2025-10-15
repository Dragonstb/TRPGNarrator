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

import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FieldDataDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FieldLinkDTO;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
public class BoardDataTest {

    @Mock
    private SynchronousBroker broker;

    @Test
    public void testAsDto() {
        // TODO: create a simple board here once the board is created from data rather than hardcoded in the init()
        BoardData data = new BoardData(broker);
        BoardDataDTO dto = data.asDTO();

        // the init() creates 110 fields with ids from 0 to 109
        List<FieldDataDTO> fieldDtos = dto.getFields();
        assertEquals(110, fieldDtos.size(), "wrong number of fields");

        List<Integer> ids = fieldDtos.stream().map(fdto->fdto.getId()).sorted().collect(Collectors.toList());
        assertEquals(0, ids.getFirst(), "Wrong lowest id");
        assertEquals(fieldDtos.size()-1, ids.getLast(), "Wrong highest id");

        Set<Integer> uniqueIds = ids.stream().collect(Collectors.toSet());
        assertEquals(fieldDtos.size(), uniqueIds.size(), "Field ids not unique");

        // the init() creates 9*21 + 10*10 = 289
        List<FieldLinkDTO> linkDtos = dto.getLinks();
        assertEquals(289, linkDtos.size(), "Wrong number of links");

        // TODO: but are they the correct links?

    }

}
