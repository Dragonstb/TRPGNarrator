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
import lombok.NonNull;

/** Host connectors are responsible for serializing and deserializing the data that goes to the clients or come from the clients,
 * respectively. The connectors are also responsible for validating the inbound and outbound data.
 *
 * For classes placed at the connection to the world outside of the virtual host, this interface faces towards the inner classes of the
 * virtual host, while {@link dev.dragonstb.trpgnarrator.virtualhost.outwardapi.VirtualHost VirtualHost} is exposed to the outside world.
 *
 * Implementations of host connectors are a strategy depending on the context the virtual host runs in. So there may be different
 * implementations for a local virtual host with direct contact to the client and a virtual host of a web game that has contact to a Spring
 * backend that is connected to the clients.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
public interface HostConnector {

    /** Sets the broker the connector communicates to, if not already set. If a broker has been specified already beforehand, this method
     * does nothing.
     *
     * @param broker Broker to be linked. Must be non-null.
     */
    public void linkBroker(@NonNull SynchronousBroker broker);
}
