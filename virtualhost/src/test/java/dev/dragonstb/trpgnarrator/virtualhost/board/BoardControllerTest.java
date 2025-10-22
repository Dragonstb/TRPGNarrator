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
import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;
import dev.dragonstb.trpgnarrator.virtualhost.error.VHostErrorCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCommand;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import java.util.ArrayList;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Dragonstb
 */
@ExtendWith(MockitoExtension.class)
public class BoardControllerTest {

    @Mock
    BoardData data;

    @Mock
    SynchronousBroker broker;

    private BoardController board;

    @BeforeEach
    public void setUp() {
        board = new BoardController(broker, data);
    }

//    @Test
//    public void testReceive() {
//    }

    @Test
    public void testRequest_boardData() {
        BoardDataDTO dto = new BoardDataDTO(new ArrayList<>(), new ArrayList<>());
        when(data.asDTO()).thenReturn(dto);

        String cmd = FetchCodes.BOARD_DATA;
        FetchCommand fetch = new FetchCommand(cmd, null);

        Optional<Object> opt = board.request(fetch);
        assertNotNull(opt, "No return");
        assertTrue(opt.isPresent(), "Nothing there");

        Object obj = opt.get();
        assertTrue(obj instanceof BoardDataDTO, "Incorrect class: "+obj.getClass().getSimpleName());
        assertTrue(obj == dto, "Wrong object");
    }

    @Test
    public void testRequest_fieldLocation_ok() {
        int fieldId = 5;
        Vector3f loc = new Vector3f(1, 2, 3);
        when(data.getLocationOfField(fieldId)).thenReturn(loc);

        String cmd = FetchCodes.BOARD_FIELD_LOCATION;
        FetchCommand fetch = new FetchCommand(cmd, fieldId);

        Optional<Object> opt = board.request(fetch);
        assertNotNull(opt, "No return");
        assertTrue(opt.isPresent(), "Nothing there");

        Object obj = opt.get();
        assertTrue(obj instanceof Vector3f, "Incorrect class: "+obj.getClass().getSimpleName());
        assertTrue(obj == loc, "Wrong object");
    }

    @Test
    public void testRequest_fieldLocation_nope() {
        String fail = "nope";

        String cmd = FetchCodes.BOARD_FIELD_LOCATION;
        FetchCommand fetch = new FetchCommand(cmd, fail);

        ClassCastException exc = assertThrows(ClassCastException.class, () -> board.request(fetch), "No exception thrown");
        String msg = exc.getMessage();
        assertTrue(
                msg.contains("Expected id of field to be an integer, but got instance of class "
                        + fail.getClass().getSimpleName() + " instead"),
                "Wrong message");
        assertTrue(msg.contains(VHostErrorCodes.V78642), "Missign or wrong error code");
    }

}
