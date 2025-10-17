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

package dev.dragonstb.trpgnarrator.virtualhost.outwardapi;

import dev.dragonstb.trpgnarrator.virtualhost.board.BoardBuilder;
import dev.dragonstb.trpgnarrator.virtualhost.broker.SyncBrokerFactory;
import lombok.NoArgsConstructor;
import dev.dragonstb.trpgnarrator.virtualhost.broker.SynchronousBroker;
import dev.dragonstb.trpgnarrator.virtualhost.error.VHostErrorCodes;
import dev.dragonstb.trpgnarrator.virtualhost.hostconnector.HostConnector;
import dev.dragonstb.trpgnarrator.virtualhost.hostconnector.HostConnectorBuilder;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

/** Assembles and returns a virtual host.
 *
 * @author Dragonstb
 * @since 0.0.1
 */
@NoArgsConstructor
@Accessors(chain = true)
@Setter
public final class VirtualHostBuilder {

    /** Type of the virtual host. */
    private HostType type = null;

    /** Generates with the given type already set.
     *
     * @since 0.0.1
     * @param type
     */
    public VirtualHostBuilder(@NonNull HostType type) {
        this.type = type;
    }

    public VirtualHost build() throws NullPointerException, UnsupportedOperationException, ClassCastException {
        String errCode = VHostErrorCodes.V46199;
        if(type == null) {
            String msg = "The type of the virtual host must be specified, but it is not.";
            String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
            throw new NullPointerException(use);
        }

        HostConnector connector;
        try {
            connector = new HostConnectorBuilder().build(type);
        } catch (UnsupportedOperationException e) {
            // types web and lan yet unsupported
            String use = VHostErrorCodes.assembleCodedMsg(e.getMessage(), errCode);
            throw new UnsupportedOperationException(use);
        }

        SynchronousBroker broker = SyncBrokerFactory.createBroker(connector);
        connector.linkBroker(broker);

        // the board registers itself to the broker
        new BoardBuilder().setBroker(broker).build();

        VirtualHost host = asVirtualHost(connector);
        return host;
    }

    /** Casts the connector as a virtual host. Currently, all classes implementing HostConnector also implement VirtualHost at the same time
     * and vice versa.
     *
     * @since 0.0.1
     * @author Dragonstb
     * @param connector Connector to be casted.
     * @return The instance casted.
     * @throws ClassCastException The argument does not implements VirtualHost.
     */
    private VirtualHost asVirtualHost(@NonNull HostConnector connector) throws ClassCastException{
        if(!(connector instanceof VirtualHost)) {
            String errCode = VHostErrorCodes.V03107;
            String msg = "Created a host connector which is not also a virtual host.";
            String use = VHostErrorCodes.assembleCodedMsg(msg, errCode);
            throw new ClassCastException(use);
        }

        return (VirtualHost)connector;
    }
}
