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
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FieldDataDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FieldLinkDTO;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public class BoardDataTest {

    private BoardData boardData;

    private final FieldDataDTO fieldDto1 = new FieldDataDTO(0, 0, 0, 1);
    private final FieldDataDTO fieldDto2 = new FieldDataDTO(1, 1, 0, 0);
    private final FieldDataDTO fieldDto3 = new FieldDataDTO(2, -1, 0, 0);
    private final FieldLinkDTO linkDto1 = new FieldLinkDTO(0, 1);
    private final FieldLinkDTO linkDto2 = new FieldLinkDTO(1, 2);
    private final FieldLinkDTO linkDto3 = new FieldLinkDTO(2, 0);
    private final List<FieldDataDTO> fieldDtos = Arrays.asList(fieldDto1, fieldDto2, fieldDto3);
    private final List<FieldLinkDTO> linkDtos = Arrays.asList(linkDto1, linkDto2, linkDto3);
    private final BoardDataDTO boardDto = new BoardDataDTO(fieldDtos, linkDtos);

    @BeforeEach
    public void setUp() {
        boardData = new BoardData(boardDto);
    }

    @Test
    public void testGetLocationOfField_Ok() {
        FieldDataDTO dto = fieldDto2;
        int id = dto.getId();

        Optional<Vector3f> opt = boardData.getLocationOfField(id);
        assertTrue(opt.isPresent(), "Optional turned out to be empty.");
        assertEquals(dto.getLocationAsVector(), opt.get(), "wrong location");
    }

    @Test
    public void testGetLocationOfField_NoFieldWithThatId() {
        int id = -15;

        Optional<Vector3f> opt = boardData.getLocationOfField(id);
        assertTrue(opt.isEmpty(), "Optional turned out to be filled.");
    }

}
