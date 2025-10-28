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
package dev.dragonstb.trpgnarrator.virtualhost.concurrentevents;

import com.jme3.math.Vector3f;
import dev.dragonstb.trpgnarrator.virtualhost.broker.ChannelNames;
import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;
import dev.dragonstb.trpgnarrator.virtualhost.error.VHostErrorCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCodes;
import dev.dragonstb.trpgnarrator.virtualhost.generic.FetchCommand;
import dev.dragonstb.trpgnarrator.virtualhost.generic.Message;
import dev.dragonstb.trpgnarrator.virtualhost.generic.MessageHeadlines;
import dev.dragonstb.trpgnarrator.virtualhost.generic.fetchparms.PathfindingConfig;
import dev.dragonstb.trpgnarrator.virtualhost.generic.messagecontents.McFindPathForFigurine;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
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
public class CEManagementTest {

    @Mock
    private Clock clock;

    @Mock
    private SynchronousBroker broker;

    @Mock
    private ScheduledThreadPoolExecutor executor;

    private CEManagement cem;

    @BeforeEach
    public void setUp() {
        cem = new CEManagement(clock, broker, executor);
    }

    @Test
    public void testReceive_findPath_ok() {
        int fromField = 0;
        int toField = 10;
        String figId = "figurine";
        McFindPathForFigurine fpff = new McFindPathForFigurine(figId, fromField, toField);
        Message msg = new Message(MessageHeadlines.PLEASE_FIND_PATH, fpff);

        PathfindingConfig pfConf = new PathfindingConfig(fromField, toField);
        FetchCommand cmd = new FetchCommand(FetchCodes.BOARD_PATHFINDER, pfConf);

        Callable<List<Vector3f>> pathfinder = mock(Callable.class);
        List<Optional<Object>> objList = new ArrayList<>();
        objList.add(Optional.of(pathfinder));
        when(broker.request(ChannelNames.GET_BOARD_DATA, cmd, true)).thenReturn(objList);

        cem.receive(msg);
        verify(broker, times(1)).request(ChannelNames.GET_BOARD_DATA, cmd, true);
        verify(executor, times(1)).submit(pathfinder);
    }

    @Test
    public void testReceive_findPath_wrongContent() {
        String problem = "nope";
        Message msg = new Message(MessageHeadlines.PLEASE_FIND_PATH, problem);

        ClassCastException exc = assertThrows(ClassCastException.class, () -> cem.receive(msg), "no exception thrown");
        String errMsg = exc.getMessage();
        assertTrue(errMsg.contains("Expected content to be a FindPathForFigurine, but got an instance of class "
                    + problem.getClass().getSimpleName() + " instead"), "Expected message missing");
        assertTrue(errMsg.contains(VHostErrorCodes.V42664), "Expected error code missing");
    }

    @Test
    public void testConstructor_ok() {
        verify(broker, times(1)).registerToChannel(cem, ChannelNames.CONCURRENT_MANAGEMENT);
    }

    @Test
    public void testConstructor_missingClock() {
        assertThrows(NullPointerException.class, () -> new CEManagement(null, broker, executor));
    }

    @Test
    public void testConstructor_missingBroker() {
        assertThrows(NullPointerException.class, () -> new CEManagement(clock, null, executor));
    }
    @Test
    public void testConstructor_missingExecutor() {
        assertThrows(NullPointerException.class, () -> new CEManagement(clock, broker, null));
    }
}
