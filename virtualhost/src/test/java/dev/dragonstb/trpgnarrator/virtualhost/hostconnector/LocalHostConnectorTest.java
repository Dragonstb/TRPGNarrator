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
package dev.dragonstb.trpgnarrator.virtualhost.hostconnector;

import dev.dragonstb.trpgnarrator.virtualhost.broker.ChannelNames;
import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;
import dev.dragonstb.trpgnarrator.virtualhost.error.VHostErrorCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCommand;
import dev.dragonstb.trpgnarrator.virtualhost.generic.Message;
import dev.dragonstb.trpgnarrator.virtualhost.generic.MessageHeadlines;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.VHCommand;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.VHCommands;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FigurineDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.FigurinesListDTO;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.vhcommandparms.FindPathForFigurineParms;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Dragonstb
 */
@ExtendWith(MockitoExtension.class)
public class LocalHostConnectorTest {

    @Mock
    private SynchronousBroker broker;

    @Mock
    private SynchronousBroker broker2;

    private LocalHostConnector connector;
    private BoardDataDTO dto;

    @BeforeEach
    public void setUp() {
        dto = new BoardDataDTO(new ArrayList<>(), new ArrayList<>());
        connector = new LocalHostConnector();
        connector.linkBroker(broker);
    }

    @Test
    public void testLinkBroker_twice() {
        connector.linkBroker(broker2);
        connector.request("a", new FetchCommand("b", null), true);

        verify(broker, times(1)).request(any(), any(), anyBoolean());
        verify(broker2, never()).request(any(), any(), anyBoolean());
    }

    @Test
    public void testGetBoardData_ok() {
        List<Optional<Object>> list = new ArrayList<>();
        list.add(Optional.of(dto));
        when(broker.request(any(), any(), anyBoolean())).thenReturn(list);

        BoardDataDTO res = connector.getBoardData();
        assertNotNull(res, "result is null");
        assertEquals(dto, res, "no the expected DTO");
    }

    @Test
    public void GetBoardData_emptyListFromBroker() {
        List<Optional<Object>> list = new ArrayList<>();
        when(broker.request(any(), any(), anyBoolean())).thenReturn(list);

        RuntimeException exc = assertThrows(RuntimeException.class, () -> connector.getBoardData(), "no exception");
        String msg = exc.getMessage();
        assertTrue(msg.contains(VHostErrorCodes.V16231), "Missing error code");
        assertTrue(msg.contains("No board data elements."), "Missing text, got: "+msg);
    }

    @Test
    public void GetBoardData_emptyOptionalFromBroker() {
        List<Optional<Object>> list = new ArrayList<>();
        list.add(Optional.empty());
        when(broker.request(any(), any(), anyBoolean())).thenReturn(list);

        RuntimeException exc = assertThrows(RuntimeException.class, () -> connector.getBoardData(), "no exception");
        String msg = exc.getMessage();
        assertTrue(msg.contains(VHostErrorCodes.V16231), "Missing error code");
        assertTrue(msg.contains("Missing board data."), "Missing text, got: "+msg);
    }

    @Test
    public void GetBoardData_wrongClassFromBroker() {
        List<Optional<Object>> list = new ArrayList<>();
        list.add(Optional.of("hello"));
        when(broker.request(any(), any(), anyBoolean())).thenReturn(list);

        RuntimeException exc = assertThrows(RuntimeException.class, () -> connector.getBoardData(), "no exception");
        String msg = exc.getMessage();
        assertTrue(msg.contains(VHostErrorCodes.V16231), "Missing error code");
        assertTrue(msg.contains("Expected a BoardDataDTO, but got an instance of String instead."), "Missing text, got: "+msg);
    }

    @Test
    public void getBoardData_directVirtualHostInterface() {
        List<Optional<Object>> list = new ArrayList<>();
        list.add(Optional.of(dto));
        when(broker.request(any(), any(), anyBoolean())).thenReturn(list);

        VHCommand command = new VHCommand(VHCommands.fetchBoard, null);
        Object obj = connector.dealRequest(command);
        assertNotNull(obj, "result is null");
        assertTrue(obj instanceof BoardDataDTO, "wrong class");

        BoardDataDTO res = (BoardDataDTO)obj;
        assertEquals(dto, res, "no the expected DTO");
    }

    @Test
    public void testGetFigurineData_ok() {
        List<FigurineDTO> figs = new ArrayList<>();
        FigurinesListDTO expect = new FigurinesListDTO(figs);
        List<Optional<Object>> list = new ArrayList<>();
        list.add(Optional.of(expect));

        FetchCommand command = new FetchCommand(FetchCodes.FIGURINE_FULL_LIST, null);
        String channelName = ChannelNames.GET_FIGURINE_DATA;
        when(broker.request(channelName, command, true)).thenReturn(list);

        FigurinesListDTO res = connector.getFigurineList();
        assertNotNull(res, "result is null");
        assertEquals(expect, res, "no the expected List");
    }

    @Test
    public void testSendFindPathForFigurine_ok() {
        String figId = "hello";
        int toField = 19;
        FindPathForFigurineParms parms = new FindPathForFigurineParms(figId, toField);
        VHCommand cmd = new VHCommand(VHCommands.setPathForFigurine, parms);

        Object obj = connector.dealRequest(cmd);

        Message msg = new Message(MessageHeadlines.PLEASE_FIND_PATH, parms);
        verify(broker, times(1)).send(msg, ChannelNames.GET_FIGURINE_DATA);

        assertNotNull(obj, "No return");
        assertTrue(obj instanceof Boolean, "No a Boolean");
        assertTrue((Boolean)obj, "Unexpected false");
    }
}
