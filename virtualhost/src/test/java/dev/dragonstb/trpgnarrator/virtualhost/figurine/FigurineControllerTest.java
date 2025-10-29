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
package dev.dragonstb.trpgnarrator.virtualhost.figurine;

import com.jme3.math.Vector3f;
import dev.dragonstb.trpgnarrator.virtualhost.broker.ChannelNames;
import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCommand;
import dev.dragonstb.trpgnarrator.virtualhost.generic.Message;
import dev.dragonstb.trpgnarrator.virtualhost.generic.MessageHeadlines;
import dev.dragonstb.trpgnarrator.virtualhost.generic.messagecontents.McFindPathForFigurine;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.vhcommandparms.FindPathForFigurineParms;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Dragonstb
 */
@ExtendWith(MockitoExtension.class)
public class FigurineControllerTest {

    // TODO: hardcoded stuff for hardcoded figurines. Becomes obsolete once figurines can be mocked here.
    private final String figId = "0";
    private final int figField = 15;
    private final FetchCommand figInitLocationFetch = new FetchCommand(FetchCodes.BOARD_FIELD_LOCATION, figField);
    private final Vector3f figLocation = new Vector3f(1, 0, 3);

    @Mock
    private SynchronousBroker broker;

    private FigurineController controller;

    public FigurineControllerTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        Optional<Object> opt = Optional.of(figLocation);
        List<Optional<Object>> list = new ArrayList<>();
        list.add(opt);
        when(broker.request(ChannelNames.GET_BOARD_DATA, figInitLocationFetch, true)).thenReturn(list);
        // TODO: mock figurines for initializing the figurines once the figurines are not created in hard code anymore.
        controller = new FigurineController(broker);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testReceive_findPath_ok() {
        int toField = figField+9;
        FindPathForFigurineParms parms = new FindPathForFigurineParms(figId, toField);
        Message msg = new Message(MessageHeadlines.PLEASE_FIND_PATH, parms);

        controller.receive(msg);

        McFindPathForFigurine content = new McFindPathForFigurine(figId, figField, toField);
        Message expected = new Message(MessageHeadlines.PLEASE_FIND_PATH, content);
        verify(broker, times(1)).send(expected, ChannelNames.CONCURRENT_MANAGEMENT);
    }

    @Test
    public void testReceive_findPath_nonexistingFigurine() {
        String otherId = figId+"justsomethingthevaluedoesntmatterjustneedsomethigndifferenttofigId";
        int toField = figField+9;
        FindPathForFigurineParms parms = new FindPathForFigurineParms(otherId, toField);
        Message msg = new Message(MessageHeadlines.PLEASE_FIND_PATH, parms);

        controller.receive(msg);

        verify(broker, never()).send(any(), anyString()); // TODO: for now called once in the hardcoded initialization of the controller
    }

}
