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

import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;
import dev.dragonstb.trpgnarrator.virtualhost.error.VHostErrorCodes;
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.Clock;
import java.util.concurrent.ScheduledThreadPoolExecutor;
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
public class CEMBuilderTest {

    @Mock
    private Clock clock;

    @Mock
    private SynchronousBroker broker;

    @Mock
    private ScheduledThreadPoolExecutor executor;

    private CEMBuilder builder;

    @BeforeEach
    public void setUp() {
        builder = new CEMBuilder();
    }

    @Test
    public void testBuild_ok() {
        builder.setBroker(broker);
        builder.setClock(clock);
        builder.setExecutor(executor);

        ConcurrentEventManager cem = builder.build();

        assertNotNull(cem, "no object built");
        // TODO: connection of cem to broker and clock, but interface CEM does not extends Receiver and/or ClockReceiver for now
    }

    @Test
    public void testBuild_noBroker() {
        builder.setClock(clock);
        builder.setExecutor(executor);

        NullPointerException exc = assertThrows(NullPointerException.class, () -> builder.build(), "No exception thrown");
        String msg = exc.getMessage();
        assertTrue(msg.contains("Broker must be defined"), "Expected message content missing");
        assertTrue(msg.contains(VHostErrorCodes.V31251), "Expected error code missing");
    }

    @Test
    public void testBuild_noClock() {
        builder.setBroker(broker);
        builder.setExecutor(executor);

        NullPointerException exc = assertThrows(NullPointerException.class, () -> builder.build(), "No exception thrown");
        String msg = exc.getMessage();
        assertTrue(msg.contains("Clock must be defined"), "Expected message content missing");
        assertTrue(msg.contains(VHostErrorCodes.V31251), "Expected error code missing");
    }

    @Test
    public void testBuild_noExecutor() {
        builder.setBroker(broker);
        builder.setClock(clock);

        NullPointerException exc = assertThrows(NullPointerException.class, () -> builder.build(), "No exception thrown");
        String msg = exc.getMessage();
        assertTrue(msg.contains("Scheduled Thread Pool Executor must be defined"), "Expected message content missing");
        assertTrue(msg.contains(VHostErrorCodes.V31251), "Expected error code missing");
    }

}
