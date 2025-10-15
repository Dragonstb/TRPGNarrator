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

import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Dragonstb
 */
@ExtendWith(MockitoExtension.class)
public class LocalHostConnectorIT {

    @Mock
    private SynchronousBroker broker1;

    @Mock
    private SynchronousBroker broker2;

    private LocalHostConnector connector;

    @BeforeEach
    public void setUp() {
        connector = new LocalHostConnector();
    }

    @Test
    public void testLinkBroker() {
        connector.linkBroker(broker1);
        connector.linkBroker(broker2);

        // TODO: make a method call from connector to broker verifying that still broker1 is linked, or use reflection to do so
    }

}
