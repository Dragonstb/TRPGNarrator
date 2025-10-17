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
package dev.dragonstb.trpgnarrator.client.clientconnector;

import dev.dragonstb.trpgnarrator.client.error.ClientErrorCodes;
import dev.dragonstb.trpgnarrator.client.error.HostConnectionNotReadyException;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.LocalVirtualHost;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
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
public class LocalClientConnectorTest {

    @Mock
    private LocalVirtualHost host;

    private LocalClientConnector client;

    @BeforeEach
    public void setUp() {
        client = new LocalClientConnector();
        client.connectToVirtualHost(host);
    }

    @Test
    public void testGetBoardData() {
        BoardDataDTO dto = mock(BoardDataDTO.class);
        when(host.getBoardData()).thenReturn(dto);
        BoardDataDTO res = client.getBoardData();
        assertEquals(dto, res, "wrong instance");
        verify(host, times(1)).getBoardData();
    }

    @Test
    public void testGetBoardData_noConnection() {
        client = new LocalClientConnector();
        HostConnectionNotReadyException exc = assertThrows(HostConnectionNotReadyException.class, () -> client.getBoardData()
                , "no exception");
        assertTrue(exc.getMessage().contains(ClientErrorCodes.C30737), "missing or unexpected error code");
    }

    @Test
    public void testConnectToVirtualHost_twice() {
        LocalVirtualHost host2 = mock(LocalVirtualHost.class);
        client.getBoardData();
        verify(host, times(1)).getBoardData();
        verify(host2, never()).getBoardData();
    }

}
