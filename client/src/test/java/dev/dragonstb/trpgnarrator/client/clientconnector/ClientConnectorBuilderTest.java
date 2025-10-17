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
import dev.dragonstb.trpgnarrator.virtualhost.outwardapi.HostType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 */
public class ClientConnectorBuilderTest {

    private ClientConnectorBuilder builder;

    @BeforeEach
    public void setUp() {
        builder = new ClientConnectorBuilder();
    }

    @Test
    public void testBuild_local() {
        builder.setType(HostType.local);
        ClientForIngame client = builder.build();
        assertNotNull(client, "no client");
        assertTrue(client instanceof LocalClientConnector, "wrong type of client");
    }

    @Test
    public void testBuils_web() {
        // TODO: update once web clients become implemented
        builder.setType(HostType.web);
        UnsupportedOperationException exc = assertThrows(UnsupportedOperationException.class, () -> builder.build());
        String msg = exc.getMessage();
        assertTrue(msg.contains(ClientErrorCodes.C97700), "missing or unexpected error code");
        assertTrue(msg.contains("Web hosts"), "unexpected message content");
    }

    @Test
    public void testBuils_LAN() {
        // TODO: update once LAN clients become implemented
        builder.setType(HostType.lan);
        UnsupportedOperationException exc = assertThrows(UnsupportedOperationException.class, () -> builder.build());
        String msg = exc.getMessage();
        assertTrue(msg.contains(ClientErrorCodes.C97700), "missing or unexpected error code");
        assertTrue(msg.contains("LAN hosts"), "unexpected message content");
    }

}
