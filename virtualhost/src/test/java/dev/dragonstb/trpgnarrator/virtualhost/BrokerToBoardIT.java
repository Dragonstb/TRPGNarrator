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

package dev.dragonstb.trpgnarrator.virtualhost;

import dev.dragonstb.trpgnarrator.virtualhost.board.Board;
import dev.dragonstb.trpgnarrator.virtualhost.board.BoardBuilder;
import dev.dragonstb.trpgnarrator.virtualhost.broker.ChannelNames;
import dev.dragonstb.trpgnarrator.virtualhost.broker.SyncBrokerFactory;
import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCommand;
import dev.dragonstb.trpgnarrator.virtualhost.generic.Globals;
import dev.dragonstb.trpgnarrator.virtualhost.hostconnector.HostConnector;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.dtos.BoardDataDTO;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Integration test of a broker with a board in a functional arrangement.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
@ExtendWith(MockitoExtension.class)
public class BrokerToBoardIT {

    @Mock
    private HostConnector connector;

    private Board board;
    private SynchronousBroker broker;

    @BeforeEach
    public void setUp() {
        broker = SyncBrokerFactory.createBroker(connector);
        board = new BoardBuilder().setBroker(broker).build();
    }

    @Test
    public void testGetBoardData() {
        String channelName = ChannelNames.GET_BOARD_DATA;
        FetchCommand fetch = new FetchCommand(FetchCodes.BOARD_DATA, null);
        List<Optional<Object>> list = broker.request(channelName, fetch, true);

        assertEquals(1, list.size(), "wrong size of list");
        Optional<Object> opt = list.getFirst();
        assertTrue(opt.isPresent(), "no data around");
        assertTrue(opt.get() instanceof BoardDataDTO, "wrong class of data");
    }

    @Test
    public void testGetNothing() {
        String channelName = ChannelNames.GET_BOARD_DATA;
        FetchCommand fetch = new FetchCommand(Globals.EMPTY_STRING, null);
        List<Optional<Object>> list = broker.request(channelName, fetch, true); // sksip the expected empty optional

        assertTrue(list.isEmpty());
    }

}
