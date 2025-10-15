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
package dev.dragonstb.trpgnarrator.virtualhost.broker;

import dev.dragonstb.trpgnarrator.virtualhost.hostconnector.HostConnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

/** Integration test of {@link SyncBrokerImp} and {@link BrokerChannel}
 *
 * @author Dragonstb
 * @since 0.0.1
 */
@ExtendWith(MockitoExtension.class)
public class SyncBrokerImpAndBrokerChannelIT {

    private final String channel1 = "channel1";
    private final String channel2 = "channel2";

    @Mock
    private HostConnector connector;

    @Mock
    private Receiver receiverA;
    @Mock
    private Receiver receiverB;

    private SyncBrokerImp broker;

    @BeforeEach
    public void setUp() {
        broker = new SyncBrokerImp(connector);
    }

    @Test
    public void testRegisterToChannel_sendToSameChannel() {
        String msg = "hellocfng78fhgj7n7803d7fcyf8ihcguyc73cc9u8";
        broker.registerToChannel(receiverA, channel1);
        broker.send(msg, channel1);
        verify(receiverA).receive(msg);
        verify(receiverB, never()).receive(any());
    }

    @Test
    public void testRegisterToChannelTwice_sendToSameChannel() {
        String msg = "hellocfng78fhgj7n7803d7fcyf8ihcguyc73cc9u8";
        broker.registerToChannel(receiverA, channel1);
        broker.registerToChannel(receiverA, channel1);
        broker.send(msg, channel1);
        verify(receiverA, times(1)).receive(msg);
        verify(receiverB, never()).receive(any());
    }

    @Test
    public void testRegisterToChannel_sendToOtherChannel() {
        String msg = "hellocfng78fhgj7n7803d7fcyf8ihcguyc73cc9u8";
        broker.registerToChannel(receiverA, channel1);
        broker.send(msg, channel2);
        verify(receiverA, never()).receive(any());
        verify(receiverB, never()).receive(any());
    }

    @Test
    public void testDeregisterFromChannel() {
        String msg = "hellocfng78fhgj7n7803d7fcyf8ihcguyc73cc9u8";
        broker.registerToChannel(receiverA, channel1);
        broker.deregisterFromChannel(receiverA, channel1);
        broker.send(msg, channel1);
        verify(receiverA, never()).receive(any());
        verify(receiverB, never()).receive(any());
    }

}
